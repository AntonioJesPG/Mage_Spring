package com.project.mage.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
import com.project.mage.model.Cesta;
import com.project.mage.model.DTO;
import com.project.mage.model.Imagenesproducto;
import com.project.mage.model.Lineacesta;
import com.project.mage.model.Lineapedido;
import com.project.mage.model.Pedido;
import com.project.mage.model.Producto;
import com.project.mage.model.repositories.CategoriaTicketRepository;
import com.project.mage.model.repositories.CestaRepository;
import com.project.mage.model.repositories.ImagenRepository;
import com.project.mage.model.repositories.LineaCestaRepository;
import com.project.mage.model.repositories.LineaPedidoRepository;
import com.project.mage.model.repositories.PedidoRepository;
import com.project.mage.model.repositories.ProductoRepository;
import com.project.mage.model.repositories.TicketRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class CestaController {

	@Autowired
	CestaRepository cr;
	
	@Autowired
	LineaCestaRepository lcr;
	
	@Autowired
	ProductoRepository pr;
	
	@Autowired
	ImagenRepository ir;
	
	@Autowired
	PedidoRepository pedr;
	
	@Autowired
	LineaPedidoRepository lpedr;

	//Cuenta la cantidad de productos que hay en la cesta del usuario
	@GetMapping("/cesta/contador")
	public DTO contadorCestaUsuario(int id) {
		
		DTO dto = new DTO();
		int cantidad = 0;
		
		//Se comprueba si existen entradas en la cesta para evitar errores
		if(cr.count() != 0) {
			List<Cesta> cestaUsuario = cr.obtenerCesta(id);
			cantidad = cestaUsuario.size();
		}
		
		dto.put("productos", cantidad);
		
		return dto;	
		
	}
	
	//Comprueba si un producto ya se encuentra en la cesta del usuario
	@PostMapping("/cesta/comprobarCesta")
	public DTO comprobarCesta (@RequestBody DatosProductoUsuario datos) {
		
		DTO dto = new DTO();
		
		dto.put("resultado", "no_existe");
		
		//Primero se comprueba si existe una linea de cesta con el producto ya que antes de crear una entrada de cesta se crea la linea
		Lineacesta lc = lcr.obtenerLineaProducto(datos.id_producto);
		if(lc != null) {
			int idLineaCesta = lc.getId();
			Cesta c = cr.obtenerCestaFromLinea(datos.id_usuario,idLineaCesta);
			
			//Ahora se comprueba si existe una cesta con ese producto
			if(c != null) {
				dto.put("resultado", "existe");
			}
		}
		
		return  dto;
	}
	
	//Agrega un producto a la cesta del usuario
	@PostMapping("/cesta/agregarProducto")
	public DTO agregarProducto (@RequestBody DatosProductoUsuario datos) {
		
		DTO dto = new DTO();
		DTO dto2 = new DTO();
		
		Producto p = pr.findById(datos.id_producto).get();
		float precio = p.getPrecio();
		
		//Comprueba que el producto tiene descuento y si lo tiene calcula el nuevo precio
		if(p.getDescuento() > 0) {
			precio = precio - ((precio * p.getDescuento())/100);
		}
		
		Lineacesta lc = lcr.obtenerLineaProducto(datos.id_producto);
		
		int id_linea_cesta = 1;
		
		//Comprobamos si la linea de la cesta ya existe para modificarla y si no existe la creamos
		if(lc != null) {
			id_linea_cesta = lc.getId();
			
			lcr.modificarLineaCesta(precio, p.getId());
			
		}else {
			id_linea_cesta = lcr.getLastLineaCesta() + 1;
			lcr.agregarLineaCesta(id_linea_cesta,datos.id_producto, precio);
		}
		
		int id_cesta = 1;
		
		if(cr.count() > 0) {
			id_cesta = cr.getLastCesta() + 1;
		}
		cr.agregarCesta(id_cesta, datos.id_usuario, id_linea_cesta);
		dto.put("resultado", "correcto");
		
		return  dto;
	}
	
	//Compra la cesta del usuario creando las entradas necesarias en pedidos y vaciando la cesta
	@GetMapping("/cesta/comprar")
	public DTO comprarCesta(int idUsuario) throws ParseException {
		
		DTO dto = new DTO();
		Producto p = new Producto();
		List<Cesta> cesta = cr.obtenerCesta(idUsuario);
		
		//Generamos un codigo aleatorio para el pedido
		String codigo = generarCodigo();
		Pedido ped = pedr.comprobarCodigo(codigo);
		
		//Este codigo se genera hasta que no exista ninguno igual en la bbdd
		while(ped != null) {
			codigo = generarCodigo();
			ped = pedr.comprobarCodigo(codigo);
		}
		
		//Tomamos la fecha en la que se crea el pedido
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String fecha = formatter.format(date);
		Date f = formatter.parse(fecha);
		
		int id = 1;
		Lineapedido lineaP;
		
		//Pasamos las entradas de la cesta al pedido
		for(Cesta c : cesta) {
			lineaP = comprobarLineaPedido(c.getLineacesta());		
			if(pedr.count() > 0) {
				id = pedr.getLastPedido() + 1;
			}
			pedr.agregarPedido(id, idUsuario, lineaP.getId() , lineaP.getPrecioCompra(), f , codigo);
		}
		
		//Una vez se han creado todas las entradas del pedido, la cesta se considera comprada y se vacia
		cr.eliminarCestaUsuario(idUsuario);
		
		dto.put("resultado", "correcto");
		
		
		return dto;	
		
	}
	
	//Comprueba si ya existe una linea de pedido con el producto de la linea cesta proporcionada, si ya existe modifica el precio sino crea uno
	public Lineapedido comprobarLineaPedido(Lineacesta lc) {
		
		Lineapedido lp = lpedr.obtenerLineaProducto(lc.getProducto().getId());
		int id = 1;
		
		
		if(lp != null) {
			lpedr.modificarLineaPedido(lc.getProducto().getId(), lc.getPrecioCompra());
		}else {
			if(lpedr.count() > 0) {
				id = lpedr.getLastLineaPedido() + 1;
			}
			lpedr.agregarLineaPedido(id,lc.getProducto().getId(), lc.getPrecioCompra());
			lp = lpedr.obtenerLineaProducto(lc.getProducto().getId());
		}
		
		return lp;
		
	}
	
	//Genera un codigo aleatorio para el pedido
	public String generarCodigo() {
		 
		//El codigo va a tener 9 letras aleatorias
	    int leftLimit = 65; // letter 'A'
	    int rightLimit = 90; // letter 'Z'
	    int targetStringLength = 9;
	    Random random = new Random();
	    StringBuilder buffer = new StringBuilder(targetStringLength);
	    for (int i = 0; i < targetStringLength; i++) {
	        int randomLimitedInt = leftLimit + (int) 
	          (random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) randomLimitedInt);
	    }
	    String generatedString = buffer.toString();

	    return generatedString;
	    
	}
	
	//Obtiene la cesta para el usuario indicado
	@GetMapping("/cesta/obtenerCesta")
	public List<DTO> obtenerCestaUsuario(int id_usuario) {
		
		List<Cesta> cesta_usuario = cr.obtenerCesta(id_usuario);
		List<DTO> objetos = new ArrayList<>();
		
		for(Cesta c : cesta_usuario) {
			objetos.add(getDTOFromCestaUsuario(c));
		}
			
		return objetos;	
		
	}
	
	//Elimina todos los productos de la cesta del usuario indicado
	@GetMapping("/cesta/vaciarCesta")
	public DTO vaciarCesta(int id_usuario) {
		
		DTO dto = new DTO();
		
		
		cr.eliminarCestaUsuario(id_usuario);
		
		dto.put("result", "correcto");	
		
		return dto;	
		
	}
	
	//Elimina una entrada de la cesta del usuario
	@PostMapping("/cesta/eliminarProductoCesta")
	public DTO eliminarLineaCesta (@RequestBody DatosProductoUsuario datos) {
		
		DTO dto = new DTO();
		
		Lineacesta linea_cesta = lcr.obtenerLineaProducto(datos.id_producto);
		
		cr.eliminarLineaCestaUsuario(datos.id_usuario, linea_cesta.getId());
		
		dto.put("result", "correcto");	
		
		return  dto;
	}
	
	//Obtiene un dto con los datos de la cesta del usuario
	private DTO getDTOFromCestaUsuario(Cesta c) {
		DTO dto = new DTO();
		Producto p = c.getLineacesta().getProducto();
		Imagenesproducto img = ir.obtenerImagenProducto(p.getId());
		float precio = p.getPrecio() - ((p.getPrecio() * p.getDescuento())/100);
		
		if(img != null) {
			dto.put("img", img.getImagen());
		}else {
			dto.put("img", null);
		}
		if(c != null) {
			dto.put("id", c.getId());
			dto.put("id_usuario", c.getUsuario().getId());
			dto.put("id_producto", p.getId());
			dto.put("nombre_producto", p.getNombre());
			dto.put("precio", precio);
		}
		
		return dto;
	}
	
}
