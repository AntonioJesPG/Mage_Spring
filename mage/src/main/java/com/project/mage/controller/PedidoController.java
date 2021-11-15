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
public class PedidoController {

	@Autowired
	PedidoRepository pedr;
	
	@Autowired
	LineaPedidoRepository lpr;
	
	@Autowired
	ProductoRepository pr;
	
	@Autowired
	ImagenRepository ir;
	
	Fechador fechador = new Fechador();
	
	//Devuelve el listado de pedidos que contienen el codigo proporcionado
	@GetMapping("/pedido/obtenerPedidosCodigo")
	public List<DTO> obtenerPedidosCodigo(String codigo) {
		
		List<Pedido> pedidos = pedr.obtenerPedidosCodigo(codigo);
		List<DTO> objetos = new ArrayList<>();
		
		for(Pedido p : pedidos) {
			objetos.add(getDTOFromPedidoCodigo(p));
		}
			
		return objetos;	
		
	}
	
	//Nos devuelve todos los pedidos del usuario con id == id_usuario
	@GetMapping("/pedido/obtenerPedidos")
	public List<DTO> obtenerPedido(int id_usuario) {
		
		List<Pedido> pedidos = pedr.obtenerPedidos(id_usuario);
		List<DTO> objetos = new ArrayList<>();
		
		for(Pedido p : pedidos) {
			objetos.add(getDTOFromPedido(p));
		}
			
		return objetos;	
		
	}
	
	//Nos devuelve todos los productos que el usuario ha comprado
	@GetMapping("/pedido/obtenerComprados")
	public List<DTO> obtenerProductosComprados(int id_usuario) {
		
		List<Pedido> pedidos = pedr.obtenerPedidos(id_usuario);
		List<DTO> objetos = new ArrayList<>();
		
		for(Pedido p : pedidos) {
			objetos.add(getDTOProductoFromPedido(p));
		}
			
		return objetos;	
		
	}
	
	//Calcula el precio total del pedido cuyo codigo es igual al proporcionado
	public float obtenerPrecioTotalPedido(int id_usuario, String codigo) {
		List<Float> precios = new ArrayList<Float>();
		
		Float precioTotal = 0.0f;
		
		precios = pedr.obtenerPreciosPedido(id_usuario, codigo);
		
		for(float p : precios) {
			precioTotal = precioTotal + p;
		}
		
		return precioTotal;
	}
	
	//Obtenemos un dto con los datos del pedido
	private DTO getDTOFromPedido(Pedido p) {
		DTO dto = new DTO();
		Producto producto = p.getLineaPedido().getProducto();
		float precio = obtenerPrecioTotalPedido(p.getUsuario().getId(),p.getCodigo());

		if(p != null) {
			dto.put("id", p.getId());
			dto.put("id_usuario", p.getUsuario().getId());
			dto.put("codigo", p.getCodigo());
			dto.put("fecha", fechador.formatDate(p.getFecha()));
			dto.put("precio_total", precio);
		}
		
		return dto;
	}
	
	//Obtenemos un dto con los datos del producto de ese pedido
	private DTO getDTOProductoFromPedido(Pedido p) {
		DTO dto = new DTO();
		Producto producto = p.getLineaPedido().getProducto();
		Imagenesproducto img = ir.obtenerImagenProducto(producto.getId());;
		if(p != null) {
			dto.put("id", producto.getId());
			dto.put("nombre", producto.getNombre());
			dto.put("img", null);
			if(img != null) {
				dto.put("img", img.getImagen());
			}
		}
		
		return dto;
	}
	
	//Devuelve otro tipo de DTO con los datos del pedido y la imagen del producto
	private DTO getDTOFromPedidoCodigo(Pedido p) {
		DTO dto = new DTO();
		Producto producto = p.getLineaPedido().getProducto();

		Imagenesproducto img = ir.obtenerImagenProducto(producto.getId());
		float precio = p.getPrecioCompraPedido();
		
		if(img != null) {
			dto.put("img", img.getImagen());
		}else {
			dto.put("img", null);
		}
		if(p != null) {
			dto.put("nombre_producto", producto.getNombre());
			dto.put("precio_compra", precio);
		}
		
		return dto;
	}
	
}
