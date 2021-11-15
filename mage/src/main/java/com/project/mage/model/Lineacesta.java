package com.project.mage.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


/**
 * The persistent class for the lineacesta database table.
 * 
 */
@Entity
@NamedQuery(name="Lineacesta.findAll", query="SELECT l FROM Lineacesta l")
public class Lineacesta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="precio_compra")
	private int precioCompra;

	//bi-directional many-to-one association to Cesta
	@OneToMany(mappedBy="lineacesta")
	private List<Cesta> cestas;

	//bi-directional many-to-one association to Producto
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_producto")
	private Producto producto;

	public Lineacesta() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPrecioCompra() {
		return this.precioCompra;
	}

	public void setPrecioCompra(int precioCompra) {
		this.precioCompra = precioCompra;
	}

	public List<Cesta> getCestas() {
		return this.cestas;
	}

	public void setCestas(List<Cesta> cestas) {
		this.cestas = cestas;
	}

	public Cesta addCesta(Cesta cesta) {
		getCestas().add(cesta);
		cesta.setLineacesta(this);

		return cesta;
	}

	public Cesta removeCesta(Cesta cesta) {
		getCestas().remove(cesta);
		cesta.setLineacesta(null);

		return cesta;
	}

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

}