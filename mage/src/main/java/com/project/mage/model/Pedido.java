package com.project.mage.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the pedido database table.
 * 
 */
@Entity
@NamedQuery(name="Pedido.findAll", query="SELECT p FROM Pedido p")
public class Pedido implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String codigo;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	//bi-directional many-to-one association to LineaPedido
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_linea_pedido")
	private Lineapedido lineapedido;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_usuario")
	private Usuario usuario;

	@Column(name="precio_compra_pedido")
	private float precioCompraPedido;

	public Pedido() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Lineapedido getLineaPedido() {
		return this.lineapedido;
	}

	public void setLineaPedido(Lineapedido lineapedido) {
		this.lineapedido = lineapedido;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public float getPrecioCompraPedido() {
		return this.precioCompraPedido;
	}

	public void setPrecioCompraPedido(float precioCompraPedido) {
		this.precioCompraPedido = precioCompraPedido;
	}

}