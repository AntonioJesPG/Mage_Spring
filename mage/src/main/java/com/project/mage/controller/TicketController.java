package com.project.mage.controller;

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
import com.project.mage.model.Ticketusuario;
import com.project.mage.model.Tipoticket;
import com.project.mage.model.Usuario;
import com.project.mage.model.DTO;
import com.project.mage.model.repositories.CategoriaTicketRepository;
import com.project.mage.model.repositories.TicketRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class TicketController {

	@Autowired
	TicketRepository tr;
	@Autowired
	CategoriaTicketRepository ctr;
	
	Fechador fechador = new Fechador();
	
	//Obtiene los tickets con id_categoria == idCategoria
	@GetMapping("/tickets/obtenerCategoria")
	public List<DTO> obtenerTicketsCategoria(int idCategoria) {
		
		DTO dto = new DTO();
		
		List<Ticketusuario> tickets = tr.obtenerPorCategoria(idCategoria);
		List<DTO> objetos = new ArrayList<>();
		
		for(Ticketusuario t : tickets) {
			objetos.add(getDTOFromTicket(t));
		}
		
		return objetos;	
		
	}
	
	//Obtiene todos los tickets marcados como resueltos
	@GetMapping("/tickets/obtenerResueltos")
	public List<DTO> obtenerTicketsResueltos() {
		
		DTO dto = new DTO();
		
		List<Ticketusuario> tickets = tr.obtenerResueltos();
		List<DTO> objetos = new ArrayList<>();
		
		for(Ticketusuario t : tickets) {
			objetos.add(getDTOFromTicket(t));
		}
		
		return objetos;	
		
	}
	
	//Obtiene los tickets del usuario indicado
	@GetMapping("/tickets/obtenerPorUsuario")
	public List<DTO> obtenerTicketsUsuario(int idUsuario) {
		
		DTO dto = new DTO();
		
		List<Ticketusuario> tickets = tr.obtenerParaUsuario(idUsuario);
		List<DTO> objetos = new ArrayList<>();
		
		for(Ticketusuario t : tickets) {
			objetos.add(getDTOFromTicket(t));
		}
		
		return objetos;	
		
	}
	
	//Obtiene el ticket con la id indicada
	@GetMapping("/tickets/obtenerTicket")
	public DTO obtenerTicket(int id) {
		
		DTO dto = new DTO();
		
		Ticketusuario ticket = tr.findById(id).get();
		
		dto = getDTOFromTicket(ticket);
		
		return dto;	
		
	}
	
	//Genera un dto para el ticket proporcionado
	private DTO getDTOFromTicket(Ticketusuario t) {
		DTO dto = new DTO();
		if(t != null) {
			dto.put("id", t.getId());
			dto.put("id_usuario", t.getUsuario().getId());
			dto.put("email", t.getUsuario().getEmail());
			dto.put("tipo_ticket", t.getTipoticket().getDescripcion());
			dto.put("mensaje", t.getMensaje());
			dto.put("estado", t.getEstado());
			dto.put("fecha_creacion", fechador.formatDate(t.getFechaCreacion()));
		}
		
		return dto;
	}
	
	//Elimina el ticket con la id proporcionada
	@GetMapping("/tickets/eliminar")
	public DTO eliminarTicket(int idTicket) {
		
		tr.deleteById(idTicket);
		
		DTO dto = new DTO();
		dto.put("result", "error");
		
		if(tr.findById(idTicket).isEmpty()) {
			dto.put("result", "exito");
		}
		
		return dto;
		
	}
	
	//Marca el status == resuelto para el ticket con la id indicada
	@GetMapping("/tickets/resolver")
	public DTO resolverTicket(int idTicket) {
		
		tr.resolverTicket(idTicket);
		
		DTO dto = new DTO();
		dto.put("result", "error");
		
		
		if(!tr.findById(idTicket).isEmpty()) {
			dto.put("result", "exito");
		}
		
		return dto;
		
	}
	
	//Obtiene las distintas categorias que existen para clasificar los tickets
	@GetMapping("/tickets/obtenerCategorias")
	public List<DTO> obtenerCategorias(){
		
		DTO dto = new DTO();
		
		List<Tipoticket> tt = (List<Tipoticket>) ctr.findAll();
		List<DTO> objetos = new ArrayList<>();
		
		for(Tipoticket t : tt) {
			objetos.add(getDTOFromTipoTicket(t));
		}
		
		return objetos;	
		
	}
	
	//Función usada para crear un ticket con los datos proporcionados
	@PostMapping("/tickets/crearTicket")
	public DTO crearTicket (@RequestBody DatosTicket datos) {
		
		DTO dto = new DTO();
		
		Ticketusuario t = new Ticketusuario();
			
		int id = tr.getLastTicket() +1;
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String fecha = formatter.format(date);
		
		tr.insertarTicket(id, datos.id_usuario, datos.id_ticket, datos.mensaje, "0", fecha);
		dto.put("result", "ticket_creado");
		
		
		return  dto;
	}
	
	//Función usada para modificar un ticket con los datos proporcionados
	@PostMapping("/tickets/modificarTicket")
	public DTO modificarTicket (@RequestBody DatosTicketModificar datos) {
		
		DTO dto = new DTO();
		
		Ticketusuario t = tr.findById(datos.id).get();
			
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String fecha = formatter.format(date);
		
		tr.modificarTicket(datos.mensaje, fecha, datos.id);
		dto.put("result", "ticket_creado");
		
		
		return  dto;
	}
	
	//Obtiene un dto para el tipo de ticket proporcionado
	private DTO getDTOFromTipoTicket(Tipoticket t) {
		DTO dto = new DTO();
		if(t != null) {
			dto.put("id", t.getId());
			dto.put("descripcion", t.getDescripcion());
		}
		
		return dto;
	}
		
}

class DatosTicket {
	int id;
	int id_usuario;
	int id_ticket;
	String mensaje;
	
	public DatosTicket(int id_usuario, int id_ticket, String mensaje) {
		super();
		this.id_usuario = id_usuario;
		this.id_ticket = id_ticket;
		this.mensaje = mensaje;
	}
	
}

class DatosTicketModificar {
	int id;
	String mensaje;
	
	public DatosTicketModificar(int id, String mensaje) {
		super();
		this.id = id;
		this.mensaje = mensaje;
	}
}


