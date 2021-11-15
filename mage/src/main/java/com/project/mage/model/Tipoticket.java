package com.project.mage.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the tipoticket database table.
 * 
 */
@Entity
@NamedQuery(name="Tipoticket.findAll", query="SELECT t FROM Tipoticket t")
public class Tipoticket implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String descripcion;

	//bi-directional many-to-one association to Ticketusuario
	@OneToMany(mappedBy="tipoticket")
	private List<Ticketusuario> ticketusuarios;

	public Tipoticket() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Ticketusuario> getTicketusuarios() {
		return this.ticketusuarios;
	}

	public void setTicketusuarios(List<Ticketusuario> ticketusuarios) {
		this.ticketusuarios = ticketusuarios;
	}

	public Ticketusuario addTicketusuario(Ticketusuario ticketusuario) {
		getTicketusuarios().add(ticketusuario);
		ticketusuario.setTipoticket(this);

		return ticketusuario;
	}

	public Ticketusuario removeTicketusuario(Ticketusuario ticketusuario) {
		getTicketusuarios().remove(ticketusuario);
		ticketusuario.setTipoticket(null);

		return ticketusuario;
	}

}