package com.project.mage.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the producto database table.
 * 
 */
@Entity
@NamedQuery(name="Producto.findAll", query="SELECT p FROM Producto p")
public class Producto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private boolean activo;

	private String desarrolladora;

	private String descripcion;

	private int descuento;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_salida")
	private Date fechaSalida;

	private String nombre;

	private float precio;

	private String video;

	//bi-directional many-to-one association to Categoriaproducto
	@OneToMany(mappedBy="producto")
	private List<Categoriaproducto> categoriaproductos;

	//bi-directional many-to-one association to Imagenesproducto
	@OneToMany(mappedBy="producto")
	private List<Imagenesproducto> imagenesproductos;

	//bi-directional many-to-one association to Lineacesta
	@OneToMany(mappedBy="producto")
	private List<Lineacesta> lineacestas;

	//bi-directional many-to-one association to Lineapedido
	@OneToMany(mappedBy="producto")
	private List<Lineapedido> lineapedidos;

	//bi-directional many-to-one association to Requisitosproducto
	@OneToMany(mappedBy="producto")
	private List<Requisitosproducto> requisitosproductos;

	//bi-directional many-to-one association to Review
	@OneToMany(mappedBy="producto")
	private List<Review> reviews;

	public Producto() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean getActivo() {
		return this.activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getDesarrolladora() {
		return this.desarrolladora;
	}

	public void setDesarrolladora(String desarrolladora) {
		this.desarrolladora = desarrolladora;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getDescuento() {
		return this.descuento;
	}

	public void setDescuento(int descuento) {
		this.descuento = descuento;
	}

	public Date getFechaSalida() {
		return this.fechaSalida;
	}

	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public float getPrecio() {
		return this.precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public String getVideo() {
		return this.video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public List<Categoriaproducto> getCategoriaproductos() {
		return this.categoriaproductos;
	}

	public void setCategoriaproductos(List<Categoriaproducto> categoriaproductos) {
		this.categoriaproductos = categoriaproductos;
	}

	public Categoriaproducto addCategoriaproducto(Categoriaproducto categoriaproducto) {
		getCategoriaproductos().add(categoriaproducto);
		categoriaproducto.setProducto(this);

		return categoriaproducto;
	}

	public Categoriaproducto removeCategoriaproducto(Categoriaproducto categoriaproducto) {
		getCategoriaproductos().remove(categoriaproducto);
		categoriaproducto.setProducto(null);

		return categoriaproducto;
	}

	public List<Imagenesproducto> getImagenesproductos() {
		return this.imagenesproductos;
	}

	public void setImagenesproductos(List<Imagenesproducto> imagenesproductos) {
		this.imagenesproductos = imagenesproductos;
	}

	public Imagenesproducto addImagenesproducto(Imagenesproducto imagenesproducto) {
		getImagenesproductos().add(imagenesproducto);
		imagenesproducto.setProducto(this);

		return imagenesproducto;
	}

	public Imagenesproducto removeImagenesproducto(Imagenesproducto imagenesproducto) {
		getImagenesproductos().remove(imagenesproducto);
		imagenesproducto.setProducto(null);

		return imagenesproducto;
	}

	public List<Lineacesta> getLineacestas() {
		return this.lineacestas;
	}

	public void setLineacestas(List<Lineacesta> lineacestas) {
		this.lineacestas = lineacestas;
	}

	public Lineacesta addLineacesta(Lineacesta lineacesta) {
		getLineacestas().add(lineacesta);
		lineacesta.setProducto(this);

		return lineacesta;
	}

	public Lineacesta removeLineacesta(Lineacesta lineacesta) {
		getLineacestas().remove(lineacesta);
		lineacesta.setProducto(null);

		return lineacesta;
	}

	public List<Lineapedido> getLineapedidos() {
		return this.lineapedidos;
	}

	public void setLineapedidos(List<Lineapedido> lineapedidos) {
		this.lineapedidos = lineapedidos;
	}

	public Lineapedido addLineapedido(Lineapedido lineapedido) {
		getLineapedidos().add(lineapedido);
		lineapedido.setProducto(this);

		return lineapedido;
	}

	public Lineapedido removeLineapedido(Lineapedido lineapedido) {
		getLineapedidos().remove(lineapedido);
		lineapedido.setProducto(null);

		return lineapedido;
	}

	public List<Requisitosproducto> getRequisitosproductos() {
		return this.requisitosproductos;
	}

	public void setRequisitosproductos(List<Requisitosproducto> requisitosproductos) {
		this.requisitosproductos = requisitosproductos;
	}

	public Requisitosproducto addRequisitosproducto(Requisitosproducto requisitosproducto) {
		getRequisitosproductos().add(requisitosproducto);
		requisitosproducto.setProducto(this);

		return requisitosproducto;
	}

	public Requisitosproducto removeRequisitosproducto(Requisitosproducto requisitosproducto) {
		getRequisitosproductos().remove(requisitosproducto);
		requisitosproducto.setProducto(null);

		return requisitosproducto;
	}

	public List<Review> getReviews() {
		return this.reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public Review addReview(Review review) {
		getReviews().add(review);
		review.setProducto(this);

		return review;
	}

	public Review removeReview(Review review) {
		getReviews().remove(review);
		review.setProducto(null);

		return review;
	}

}