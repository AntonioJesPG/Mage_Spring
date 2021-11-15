package com.project.mage.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;


/**
 * The persistent class for the usuario database table.
 * 
 */
@Entity
@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String dni;

	private String email;

	@Lob
	private byte[] imagen;

	private String nombre;

	private String password;

	@Column(name="primer_apellido")
	private String primerApellido;

	private float saldo;

	@Column(name="segundo_apellido")
	private String segundoApellido;

	private String username;

	//bi-directional many-to-one association to Cesta
	@OneToMany(mappedBy="usuario")
	private List<Cesta> cestas;

	//bi-directional many-to-one association to Pedido
	@OneToMany(mappedBy="usuario")
	private List<Pedido> pedidos;

	//bi-directional many-to-one association to Review
	@OneToMany(mappedBy="usuario")
	@JsonManagedReference
	private List<Review> reviews;

	//bi-directional many-to-one association to Rolusuario
	@OneToMany(mappedBy="usuario")
	private List<Rolusuario> rolusuarios;

	//bi-directional many-to-one association to Ticketusuario
	@OneToMany(mappedBy="usuario")
	private List<Ticketusuario> ticketusuarios;

	public Usuario() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDni() {
		return this.dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getImagen() {
		return this.imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPrimerApellido() {
		return this.primerApellido;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public float getSaldo() {
		return this.saldo;
	}

	public void setSaldo(float saldo) {
		this.saldo = saldo;
	}

	public String getSegundoApellido() {
		return this.segundoApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Cesta> getCestas() {
		return this.cestas;
	}

	public void setCestas(List<Cesta> cestas) {
		this.cestas = cestas;
	}

	public Cesta addCesta(Cesta cesta) {
		getCestas().add(cesta);
		cesta.setUsuario(this);

		return cesta;
	}

	public Cesta removeCesta(Cesta cesta) {
		getCestas().remove(cesta);
		cesta.setUsuario(null);

		return cesta;
	}

	public List<Pedido> getPedidos() {
		return this.pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	public Pedido addPedido(Pedido pedido) {
		getPedidos().add(pedido);
		pedido.setUsuario(this);

		return pedido;
	}

	public Pedido removePedido(Pedido pedido) {
		getPedidos().remove(pedido);
		pedido.setUsuario(null);

		return pedido;
	}

	public List<Review> getReviews() {
		return this.reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public Review addReview(Review review) {
		getReviews().add(review);
		review.setUsuario(this);

		return review;
	}

	public Review removeReview(Review review) {
		getReviews().remove(review);
		review.setUsuario(null);

		return review;
	}

	public List<Rolusuario> getRolusuarios() {
		return this.rolusuarios;
	}

	public void setRolusuarios(List<Rolusuario> rolusuarios) {
		this.rolusuarios = rolusuarios;
	}

	public Rolusuario addRolusuario(Rolusuario rolusuario) {
		getRolusuarios().add(rolusuario);
		rolusuario.setUsuario(this);

		return rolusuario;
	}

	public Rolusuario removeRolusuario(Rolusuario rolusuario) {
		getRolusuarios().remove(rolusuario);
		rolusuario.setUsuario(null);

		return rolusuario;
	}

	public List<Ticketusuario> getTicketusuarios() {
		return this.ticketusuarios;
	}

	public void setTicketusuarios(List<Ticketusuario> ticketusuarios) {
		this.ticketusuarios = ticketusuarios;
	}

	public Ticketusuario addTicketusuario(Ticketusuario ticketusuario) {
		getTicketusuarios().add(ticketusuario);
		ticketusuario.setUsuario(this);

		return ticketusuario;
	}

	public Ticketusuario removeTicketusuario(Ticketusuario ticketusuario) {
		getTicketusuarios().remove(ticketusuario);
		ticketusuario.setUsuario(null);

		return ticketusuario;
	}

}