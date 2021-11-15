package com.project.mage.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the categoria database table.
 * 
 */
@Entity
@NamedQuery(name="Categoria.findAll", query="SELECT c FROM Categoria c")
public class Categoria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String descripcion;

	//bi-directional many-to-one association to Categoriaproducto
	@OneToMany(mappedBy="categoria")
	private List<Categoriaproducto> categoriaproductos;

	public Categoria() {
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

	public List<Categoriaproducto> getCategoriaproductos() {
		return this.categoriaproductos;
	}

	public void setCategoriaproductos(List<Categoriaproducto> categoriaproductos) {
		this.categoriaproductos = categoriaproductos;
	}

	public Categoriaproducto addCategoriaproducto(Categoriaproducto categoriaproducto) {
		getCategoriaproductos().add(categoriaproducto);
		categoriaproducto.setCategoria(this);

		return categoriaproducto;
	}

	public Categoriaproducto removeCategoriaproducto(Categoriaproducto categoriaproducto) {
		getCategoriaproductos().remove(categoriaproducto);
		categoriaproducto.setCategoria(null);

		return categoriaproducto;
	}

}