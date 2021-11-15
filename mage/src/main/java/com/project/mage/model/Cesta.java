package com.project.mage.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the cesta database table.
 * 
 */
@Entity
@NamedQuery(name="Cesta.findAll", query="SELECT c FROM Cesta c")
public class Cesta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	//bi-directional many-to-one association to Lineacesta
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_linea_cesta")
	private Lineacesta lineacesta;

	//bi-directional many-to-one association to Usuario
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_usuario")
	private Usuario usuario;

	public Cesta() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Lineacesta getLineacesta() {
		return this.lineacesta;
	}

	public void setLineacesta(Lineacesta lineacesta) {
		this.lineacesta = lineacesta;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}