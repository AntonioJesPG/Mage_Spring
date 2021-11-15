package com.project.mage.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.mage.AutenticadorJWT;
import com.project.mage.model.Producto;
import com.project.mage.model.Usuario;
import com.project.mage.model.Review;
import com.project.mage.model.Ticketusuario;
import com.project.mage.model.DTO;
import com.project.mage.model.repositories.ProductoRepository;
import com.project.mage.model.repositories.ReviewRepository;
import com.project.mage.model.repositories.UsuarioRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ReviewController {

	@Autowired
	ReviewRepository rr;
	
	@Autowired
	UsuarioRepository ur;
	
	@Autowired
	ProductoRepository pr;
	
	Fechador fechador = new Fechador();
	
	//Obtiene un listado con todos los comentarios que existen en la bbdd
	@GetMapping("/comentarios/obtenerComentarios")
	public List<DTO> obtenerReview() {
		
		DTO dto = new DTO();
		
		List<Review> reviews = (List<Review>) rr.findAll();
		List<DTO> objetos = new ArrayList<>();
		
		for(Review r : reviews) {
			objetos.add(getDTOFromReview(r));
		}
		
		return objetos;	
		
	}
	
	//Obtiene un listado con los comentarios para el producto proporcionadod
	@GetMapping("/comentarios/obtenerComentariosProducto")
	public List<DTO> obtenerReviewProducto(int idProducto) {
		
		DTO dto = new DTO();
		
		List<Review> reviews = rr.getComentariosProducto(idProducto);
		List<DTO> objetos = new ArrayList<>();
		
		for(Review r : reviews) {
			objetos.add(getDTOFromReview(r));
		}
		
		return objetos;	
		
	}
	
	//Obtiene un comentario con la id que se nos proporciona
	@GetMapping("/comentarios/obtenerComentarioId")
	public DTO obtenerReview(int id) {
		
		DTO dto = new DTO();
		
		Review r = rr.findById(id).get();
		
		dto = getDTOFromReview(r);
		
		return dto;	
		
	}
	
	//Elimina un comentario identificado por idComentario
	@GetMapping("/comentarios/eliminar")
	public DTO eliminarReview(int idComentario) {
		
		DTO dto = new DTO();
		dto.put("result", "no_ejecutado");
		Review r = rr.findById(idComentario).get();
		
		if(r != null) {
			rr.delete(r);
			dto.put("result", "comentario_eliminado");
		}
		
		return dto;	
		
	}
	
	//Obtiene el comentario del usuario indicado para el producto indicado
	@PostMapping("/comentarios/obtenerComentarioIdUsuario")
	public DTO obtenerReviewUsuario(@RequestBody DatosProductoUsuario datos){
		
		DTO dto = new DTO();
		
		Review r = rr.findByIdUsuarioProducto(datos.id_producto, datos.id_usuario);
		
		dto = getDTOFromReview(r);
		
		return dto;	
		
	}
	
	//Función usada para crear un comentario en un producto
	@PostMapping("/comentarios/comentar")
	public DTO comentar (@RequestBody DatosComentario datos) throws ParseException {
		
		DTO dto = new DTO();
		int id = 1;
		
		if(rr.count() > 0) {
			id = rr.getLastReview() + 1;
		}
		//Se obtiene la fecha de creación del comentario
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String fecha = formatter.format(date);
		Date f = formatter.parse(fecha);
		
		rr.insertarReview(id,datos.id_usuario, datos.id_producto, datos.mensaje, f , datos.valoracion);

		dto.put("result", "comentario_creado");
		
		
		return  dto;
	}

	//Función usada para modificar un comentario ya existente (Realizado por la administración)
	@PostMapping("/comentarios/modificarComentario")
	public DTO modificarReview (@RequestBody DatosComentarioModificar datos) {
		
		DTO dto = new DTO();
		
		Review r = rr.findById(datos.id).get();

		
		rr.modificarComentario(datos.mensaje, datos.id);
		dto.put("result", "comentario_modificado");
		
		
		return  dto;
	}
	
	//Función usada para modificar un comentario ya existente (Realizado por el creador del comentario)
	@PostMapping("/comentarios/modificarComentarioUsuario")
	public DTO modificarReviewUsuario (@RequestBody DatosComentarioModificarUsuario datos) {
		
		DTO dto = new DTO();
		
		Review r = rr.findById(datos.id).get();

		rr.modificarComentarioUsuario(datos.mensaje, datos.valoracion, datos.id);
		dto.put("result", "comentario_modificado");
		
		
		return  dto;
	}
	
	
	private DTO getDTOFromReview(Review r) {
		DTO dto = new DTO();
		if(r != null) {
			dto.put("id", r.getId());
			dto.put("id_usuario", r.getUsuario().getId());
			dto.put("id_producto", r.getProducto().getId());
			dto.put("username", r.getUsuario().getUsername());
			dto.put("nombre_producto", r.getProducto().getNombre());
			dto.put("mensaje", r.getMensaje());
			dto.put("valoracion", r.getValoracion());
			dto.put("fecha_creacion", fechador.formatDate(r.getFechaCreacion()));
		}
		
		return dto;
	}
	
	
	
}

class DatosComentarioModificar {
	int id;
	String mensaje;
	
	public DatosComentarioModificar(int id, String mensaje) {
		super();
		this.id = id;
		this.mensaje = mensaje;
	}
}

class DatosComentario {
	int id_producto;
	int id_usuario;
	String mensaje;
	int valoracion;
	
	public DatosComentario(int id_producto, int id_usuario, String mensaje, int valoracion) {
		super();
		this.id_producto = id_producto;
		this.id_usuario = id_usuario;
		this.mensaje = mensaje;
		this.valoracion = valoracion;
	}
	
}

class DatosComentarioModificarUsuario {
	int id;
	String mensaje;
	int valoracion;
	
	public DatosComentarioModificarUsuario(int id, String mensaje, int valoracion) {
		super();
		this.id = id;
		this.mensaje = mensaje;
		this.valoracion = valoracion;
	}
}




