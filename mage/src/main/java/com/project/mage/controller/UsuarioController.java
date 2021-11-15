package com.project.mage.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.mage.AutenticadorJWT;
import com.project.mage.model.Usuario;
import com.project.mage.model.DTO;
import com.project.mage.model.Review;
import com.project.mage.model.Rol;
import com.project.mage.model.Rolusuario;
import com.project.mage.model.Tipoticket;
import com.project.mage.model.repositories.CestaRepository;
import com.project.mage.model.repositories.PedidoRepository;
import com.project.mage.model.repositories.ReviewRepository;
import com.project.mage.model.repositories.RolRepository;
import com.project.mage.model.repositories.RolusuarioRepository;
import com.project.mage.model.repositories.TicketRepository;
import com.project.mage.model.repositories.UsuarioRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UsuarioController {

	@Autowired
	UsuarioRepository ur;
	@Autowired
	RolusuarioRepository rolr;
	@Autowired
	RolRepository rr;
	@Autowired
	ReviewRepository reviewr;
	@Autowired
	CestaRepository cestar;
	@Autowired
	PedidoRepository pedidor;
	@Autowired
	TicketRepository ticketr;
	
	//Función usada para logear al usuario en la web
		@PostMapping("/usuario/autentica")
		public DTO autenticarUsuario (@RequestBody DatosAutenticacionUsuario datos) {
			
			DTO dto = new DTO();
			Usuario u = this.ur.findByEmailAndPassword(datos.email, datos.password);
			if ( u != null) {
				dto.put("jwt", AutenticadorJWT.codificaJWT(u));
			}
			return  dto;
		}
		
		@GetMapping("/usuario/obtenerLogeado")
		public DTO getUsuarioLogeado(HttpServletRequest request) {
			
			int idUsuario = AutenticadorJWT.getIdUsuarioDesdeJwtIncrustadoEnRequest(request);
			
			Usuario u = ur.findById(idUsuario).get();
			
			return getDTOFromUsuario(u);
			
		}
		
		//Comprueba si los datos de registro proporcionados ya existen en el sistema
		public DTO comprobarDatosExistentes(String email, String dni, String username) {
			
			DTO dto = new DTO();
			
			Usuario u = new Usuario();
			
			if( ur.findByEmail(email) != null) {
				dto.put("result", "error_email");
			}else if (ur.findByDni(dni) != null) {
				dto.put("result", "error_dni");
			} else if(ur.findByUsername(username) != null) {
				dto.put("result", "error_username");
			} else {
				dto = null;
			}
			
			return dto;
			
		}
		
		//Función usada para crear al usuario con rol básico en la web
			@PostMapping("/usuario/crearUsuarioNormal")
			public DTO crearUsuarioNormal (@RequestBody DatosUsuario datos) {
				
				DTO dto = new DTO();
				
				Usuario u = new Usuario();

				dto = comprobarDatosExistentes(datos.email, datos.dni, datos.username);
				
				if( dto == null) {
					
					dto = new DTO();
					
					int id = ur.getLastUsuario() +1;
					
					ur.insertarUsuario(id, datos.username, datos.nombre, datos.primer_apellido, datos.segundo_apellido, datos.dni, datos.email, datos.password, (float) 0.0, "");
					
					int idRolesUsuario = rolr.getLastRolUsuario() + 1;
					rolr.insertarRolUsuario(idRolesUsuario, id, 1);
					
					dto.put("result", "usuario_creado");
				}
				
				return  dto;
			}
			
			//Función usada para crear al usuario con un rol determinado en la web
			@PostMapping("/usuario/crearUsuarioRoles")
			public DTO crearUsuarioNormalRoles (@RequestBody DatosUsuarioRol datos) {
				
				DTO dto = new DTO();
				
				Usuario u = new Usuario();

				dto = comprobarDatosExistentes(datos.email, datos.dni, datos.username);
				
				if( dto == null) {
					dto = new DTO();
					
					int id = ur.getLastUsuario() +1;
					
					ur.insertarUsuario(id, datos.username, datos.nombre, datos.primer_apellido, datos.segundo_apellido, datos.dni, datos.email, datos.password, (float) 0.0, "");
					
					int idRolesUsuario = rolr.getLastRolUsuario() + 1;
					rolr.insertarRolUsuario(idRolesUsuario, id, datos.id_rol);
					
					dto.put("result", "usuario_creado");
				}
				
				return  dto;
			}
			
			//Obtenemos al usuario con la id proporcionada
			@GetMapping("/usuario/obtenerUsuario")
			public DTO obtenerUsuario(int id) {
				
				DTO dto = new DTO();
				
				Usuario u = ur.findById(id).get();
				DTO objeto = getDTOFromUsuario(u);
				
				return objeto;	
				
			}
			
			//Obtiene la lista de todos los usuarios
			@GetMapping("/usuario/obtenerUsuarios")
			public List<DTO> obtenerUsuarios() {
				
				DTO dto = new DTO();
				
				List<Usuario> usuarios = (List<Usuario>) ur.findAll();
				List<DTO> objetos = new ArrayList<>();
				
				for(Usuario u : usuarios) {
					objetos.add(getDTOFromUsuario(u));
				}
				
				return objetos;	
				
			}
			
			//Elimina al usuario proporcionado
			@GetMapping("/usuario/eliminar")
			public DTO eliminarUsuario(int idUsuario) {
				
				rolr.eliminarRolUsuario(idUsuario);
				reviewr.eliminarReviewUsuario(idUsuario);
				ticketr.eliminarTicketUsuario(idUsuario);
				cestar.eliminarCestaUsuario(idUsuario);
				pedidor.eliminarPedido(idUsuario);
				
				ur.deleteById(idUsuario);
				
				DTO dto = new DTO();
				dto.put("result", "error");
				
				if(ur.findById(idUsuario).isEmpty()) {
					dto.put("result", "exito");
				}
				
				return dto;
				
			}
			
			//Obtiene un dto para el usuario proporcionado
			private DTO getDTOFromUsuario(Usuario u) {
				DTO dto = new DTO();
				if(u != null) {
					dto.put("id", u.getId());
					dto.put("email", u.getEmail());
					dto.put("password", u.getPassword());
					dto.put("username", u.getUsername());
					dto.put("nombre", u.getNombre());
					dto.put("primer_apellido", u.getPrimerApellido());
					dto.put("segundo_apellido", u.getSegundoApellido());
					dto.put("id_rol", u.getRolusuarios().get(0).getRol().getId());				
					dto.put("nombre_rol", u.getRolusuarios().get(0).getRol().getDescripcion());		
					
					dto.put("dni", u.getDni());
					dto.put("saldo", u.getSaldo());
					dto.put("imagen", u.getImagen());
				}
				
				return dto;
			}
			
			//Obtiene un listado de los distintos roles que puede tener un usuario
			@GetMapping("/usuario/obtenerRoles")
			public List<DTO> obtenerRoles(){
				
				DTO dto = new DTO();
				
				List<Rol> roles = (List<Rol>) rr.findAll();
				List<DTO> objetos = new ArrayList<>();
				
				for(Rol r : roles) {
					objetos.add(getDTOFromRol(r));
				}
				
				return objetos;	
				
			}
			
			private DTO getDTOFromRol(Rol r) {
				DTO dto = new DTO();
				if(r != null) {
					dto.put("id", r.getId());
					dto.put("descripcion", r.getDescripcion());
				}
				
				return dto;
			}
			
			//Función usada para modificar al usuario proporcionado
			@PostMapping("/usuario/modificarUsuario")
			public DTO modificarUsuario (@RequestBody DatosUsuarioModificar datos) {
				
				DTO dto = new DTO();
				boolean error = false;
				
				Usuario u = ur.findById(datos.id).get();
				
				if( !datos.username.equals(u.getUsername())) {
					if(ur.findByUsername(datos.username) != null) {
						error = true;
						dto.put("result", "error_username");
					}
				}
				if( !datos.dni.equals(u.getDni())) {
					if(ur.findByDni(datos.dni) != null) {
						error = true;
						dto.put("result", "error_dni");
					}
				}
				
				if(error == false) {
				ur.modificarUsuario(datos.username, datos.nombre, datos.primer_apellido, datos.segundo_apellido, datos.dni, datos.id);
				rolr.modificarRol(datos.id_rol, datos.id);

				dto.put("result", "usuario_modificado");
				}
				
				return  dto;
			}
			
			//Función usada para modificar al usuario proporcionado
			@PostMapping("/usuario/modificarUsuarioNormal")
			public DTO modificarUsuarioNormal (@RequestBody DatosUsuarioModificar datos) {
				
				DTO dto = new DTO();
				boolean error = false;
				
				Usuario u = ur.findById(datos.id).get();
				
				if( !datos.username.equals(u.getUsername())) {
					if(ur.findByUsername(datos.username) != null) {
						error = true;
						dto.put("result", "error_username");
					}
				}
				
				if(error == false) {
				ur.modificarUsuarioNormal(datos.username, datos.nombre, datos.primer_apellido, datos.segundo_apellido, datos.id);

				dto.put("result", "usuario_modificado");
				}
				
				return  dto;
			}
	
}

//Obtenemos los datos del usuario que quiere logearse, los recibimos por POST en angular
class DatosAutenticacionUsuario {
	String email;
	String password;

	public DatosAutenticacionUsuario(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}
}

//Obtenemos los datos del usuario que quiere crearse, los recibimos por POST en angular
class DatosUsuario {
	String email;
	String password;
	String username;
	String nombre;
	String primer_apellido;
	String segundo_apellido;
	String dni;

	public DatosUsuario(String email, String password, String username, String nombre, String primer_apellido, String segundo_apellido, String dni) {
		super();
		this.email = email;
		this.password = password;
		this.username = username;
		this.nombre = nombre;
		this.primer_apellido = primer_apellido;
		this.segundo_apellido = segundo_apellido;
		this.dni = dni;
	}
}

//Obtenemos los datos del usuario que quiere crearse, los recibimos por POST en angular
class DatosUsuarioRol {
	String email;
	String password;
	String username;
	String nombre;
	String primer_apellido;
	String segundo_apellido;
	String dni;
	int id_rol;

	public DatosUsuarioRol(String email, String password, String username, String nombre, String primer_apellido, String segundo_apellido, String dni, int id_rol) {
		super();
		this.email = email;
		this.password = password;
		this.username = username;
		this.nombre = nombre;
		this.primer_apellido = primer_apellido;
		this.segundo_apellido = segundo_apellido;
		this.dni = dni;
		this.id_rol = id_rol;
	}
}

//Obtenemos los datos del usuario que quiere crearse, los recibimos por POST en angular
class DatosUsuarioModificar {
	int id;
	String username;
	String nombre;
	String primer_apellido;
	String segundo_apellido;
	String dni;
	int id_rol;

	public DatosUsuarioModificar(int id, String username, String nombre, String primer_apellido, String segundo_apellido, String dni, int id_rol) {
		super();
		this.id = id;
		this.username = username;
		this.nombre = nombre;
		this.primer_apellido = primer_apellido;
		this.segundo_apellido = segundo_apellido;
		this.dni = dni;
		this.id_rol = id_rol;
	}
}
