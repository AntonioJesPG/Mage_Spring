package com.project.mage.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.mage.AutenticadorJWT;
import com.project.mage.model.Producto;
import com.project.mage.model.Requisitosproducto;
import com.project.mage.model.Tipoticket;
import com.project.mage.model.Usuario;
import com.project.mage.model.Categoria;
import com.project.mage.model.Categoriaproducto;
import com.project.mage.model.Cesta;
import com.project.mage.model.DTO;
import com.project.mage.model.Imagenesproducto;
import com.project.mage.model.Lineacesta;
import com.project.mage.model.Lineapedido;
import com.project.mage.model.Pedido;
import com.project.mage.model.repositories.CategoriaProductoRepository;
import com.project.mage.model.repositories.CategoriaRepository;
import com.project.mage.model.repositories.CestaRepository;
import com.project.mage.model.repositories.ImagenRepository;
import com.project.mage.model.repositories.LineaCestaRepository;
import com.project.mage.model.repositories.LineaPedidoRepository;
import com.project.mage.model.repositories.PedidoRepository;
import com.project.mage.model.repositories.ProductoRepository;
import com.project.mage.model.repositories.RequisitosRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ProductoController {

	@Autowired
	ProductoRepository pr;
	
	@Autowired
	CategoriaRepository cr;
	
	@Autowired
	CategoriaProductoRepository cpr;
	
	@Autowired
	RequisitosRepository reqr;
	
	@Autowired
	ImagenRepository imgr;
	
	@Autowired
	LineaPedidoRepository lpr;
	
	@Autowired
	PedidoRepository pedr;
	
	Fechador fechador = new Fechador();
	
	@Autowired
	CestaRepository cestr;
	
	//Obtiene una lista con todos los productos
	@GetMapping("/producto/obtenerLista")
	public List<DTO> obtenerProductos() {
		
		DTO dto = new DTO();
		
		List<Producto> productos = (List<Producto>) pr.findAll();
		List<DTO> objetos = new ArrayList<>();
		
		for(Producto p : productos) {
			objetos.add(getDTOFromProducto(p));
		}
		
		return objetos;	
		
	}
	
	@GetMapping("/producto/buscar")
	public List<DTO> busqueda(String nombre) {
		
		DTO dto = new DTO();
		
		List<Producto> productos = (List<Producto>) pr.findByNombre(nombre);
		List<DTO> objetos = new ArrayList<>();
		
		objetos = obtenerListaProductosDTO(productos);
		
		return objetos;	
		
	}
	
	//Obtiene el producto identificado por la id proporcionada
	@GetMapping("/producto/obtenerProducto")
	public DTO obtenerProducto(int id) {
		
		DTO dto = new DTO();
		
		Producto p = pr.findById(id).get();
			
		dto = getDTOFromProducto(p);
		
		return dto;	
		
	}
	
	//Obtiene los datos del producto que se quiere modificar
	@GetMapping("/producto/obtenerProductoModificar")
	public DTO obtenerProductoModificar(int id) {
		
		DTO dto = new DTO();
		
		Producto p = pr.findById(id).get();
			
		dto = getDTOFromProducto(p);
		
		dto.put("fecha_salida", p.getFechaSalida());
		
		return dto;	
		
	}
	
	//Obtiene la categoria para el producto indicado
	@GetMapping("/producto/obtenerCategoria")
	public DTO obtenerCategoria(int id) {
		
		DTO dto = new DTO();
		
		Categoriaproducto cp = cpr.obtenerCategoriaPorProducto(id);
		Categoria c = cp.getCategoria();
		dto.put("id", c.getId());
		dto.put("descripcion", c.getDescripcion());
		
		return dto;	
		
	}
	
	//Obtiene los requisitos minimos para el producto con la siguiente id
	@GetMapping("/producto/obtenerRequisitosMin")
	public DTO obtenerRequisitosMin(int id_producto) {
		
		DTO dto = new DTO();
		
		Requisitosproducto r = reqr.obtenerMinimos(id_producto);
		dto.put("so", r.getSo());
		dto.put("procesador", r.getProcesador());
		dto.put("memoria", r.getMemoria());
		dto.put("gpu", r.getGpu());
		dto.put("almacenamiento", r.getAlmacenamiento());

		return dto;	
		
	}
	
	//Obtiene los requisitos recomendados para el producto con la siguiente id
	@GetMapping("/producto/obtenerRequisitosRec")
	public DTO obtenerRequisitosRec(int id_producto) {
		
		DTO dto = new DTO();
		
		Requisitosproducto r = reqr.obtenerRecomendados(id_producto);
		dto.put("so", r.getSo());
		dto.put("procesador", r.getProcesador());
		dto.put("memoria", r.getMemoria());
		dto.put("gpu", r.getGpu());
		dto.put("almacenamiento", r.getAlmacenamiento());

		return dto;	
		
	}
	
	//Obtiene la imagen para el producto con la siguiente id
	@GetMapping("/producto/obtenerImagen")
	public DTO obtenerImagen(int id_producto) {
		
		DTO dto = new DTO();
		
		
		Imagenesproducto img = imgr.obtenerImagenProducto(id_producto);
		
		if(img != null) {
			dto.put("img", img.getImagen());
		}else {
			dto.put("error", "no_image");
		}

		return dto;	
		
	}
	
	//Genera un dto para el producto proporcionado
	private DTO getDTOFromProducto(Producto p) {
		DTO dto = new DTO();
		if(p != null) {
			dto.put("id", p.getId());
			dto.put("nombre", p.getNombre());
			dto.put("precio", p.getPrecio());
			dto.put("descripcion", p.getDescripcion());
			dto.put("video", p.getVideo());
			dto.put("fecha_salida", fechador.formatDate(p.getFechaSalida()));
			dto.put("desarrolladora", p.getDesarrolladora());
			dto.put("descuento", p.getDescuento());
			dto.put("activo", p.getActivo());
		}
		
		return dto;
	}
	
	//Obtiene un listado con los productos destacados
	@GetMapping("/producto/destacados")
	public List<DTO> obtenerProductosDestacados(){
		
		Producto p = new Producto();
		List<Producto> productos = new ArrayList<Producto>();
		long cantidad = this.pr.count();
		int random = 0;
		
		//Estos productos destacados son 4
		for(int i = 0; i < 4 ; i++) {
			random = (int) (Math.random()*(cantidad-1)+2);
			p = this.pr.findById(random).get();	
			if(!productos.contains(p) && p.getActivo() == true ) {
				productos.add(p);
			}else {
				i--;
			}
			
		}
		
		return obtenerListaProductosDTO(productos);

	}
	
	//Obtiene el primer producto de la bbdd
	@GetMapping("/producto/obtenerPrimerDestacado")
	public DTO obtenerPrimerDestacado(){
		
		Producto p = new Producto();
		List<Producto> productos = new ArrayList<Producto>();
		
		p = this.pr.getPrimerDestacado();	
		productos.add(p);
			
		DTO dto = obtenerListaProductosDTO(productos).get(0);
		
		return dto;

	}
	
	//Obtiene los 4 productos con mayor descuento de la bbdd
	@GetMapping("/producto/grandesDescuentos")
	public List<DTO> obtenerGrandesDescuentos(){
		
		Producto p = new Producto();
		List<Producto> productos = this.pr.getGrandesDescuentos();
		
		return obtenerListaProductosDTO(productos);

	}
	
	//Obtiene todos los productos de la bbdd que se encuentran activos
	@GetMapping("/producto/catalogo")
	public List<DTO> obtenerCatalogo(){
		
		Producto p = new Producto();
		List<Producto> productos = this.pr.getCatalogo();
		
		return obtenerListaProductosDTO(productos);

	}
	
	//Obtiene los últimos 5 productos agregados a la bbdd
	@GetMapping("/producto/nuevos")
	public List<DTO> obtenerNuevos(){
		
		Producto p = new Producto();
		List<Producto> productos = this.pr.getNuevos();
		
		return obtenerListaProductosDTO(productos);

	}
	
	//Obtiene los 5 ultimos productos de la bbdd que tienen un precio menor a 20 euros
	@GetMapping("/producto/menos20")
	public List<DTO> obtenerMenos20(){
		
		Producto p = new Producto();
		List<Producto> productos = this.pr.getMenos20();
		
		return obtenerListaProductosDTO(productos);

	}
	
	//Obtiene los 5 ultimos productos de la bbdd que tienen un precio menor a 10 euros
	@GetMapping("/producto/menos10")
	public List<DTO> obtenerMenos10(){
		
		Producto p = new Producto();
		List<Producto> productos = this.pr.getMenos10();
		
		return obtenerListaProductosDTO(productos);

	}
	
	//A partir de una lista de productos genera una lista de DTO con los detalles de cada producto
	private List<DTO> obtenerListaProductosDTO(List<Producto> productos){
		List<DTO> objetos = new ArrayList<DTO>();
		DTO dto = new DTO();
		Imagenesproducto img;
		for(Producto p : productos) {
			dto.put("id", p.getId());
			dto.put("nombre", p.getNombre());
			dto.put("precio", p.getPrecio());
			dto.put("descripcion", p.getDescripcion());
			dto.put("video", p.getVideo());
			dto.put("fecha_salida", fechador.formatDate(p.getFechaSalida()));
			dto.put("desarrolladora", p.getDesarrolladora());
			dto.put("descuento", p.getDescuento());
			dto.put("activo", p.getActivo());
			dto.put("img", null);
			img = imgr.obtenerImagenProducto(p.getId());
			if(img != null) {
				dto.put("img", img.getImagen());
			}
			objetos.add(dto);
			dto = new DTO();
		}
		
		return objetos;
	}
	
	//Obtiene una lista de las categorias de un producto
	@GetMapping("/producto/obtenerCategorias")
	public List<DTO> obtenerCategorias(){
		
		DTO dto = new DTO();
		
		List<Categoria> tt = (List<Categoria>) cr.findAll();
		List<DTO> objetos = new ArrayList<>();
		
		for(Categoria c : tt) {
			objetos.add(getDTOFromCategoria(c));
		}
		
		return objetos;	
		
	}
	
	//Obtiene un dto para una categoria proporcionada
	public DTO getDTOFromCategoria(Categoria c) {
		
		DTO dto = new DTO();
		if(c != null) {
			dto.put("id", c.getId());
			dto.put("descripcion", c.getDescripcion());
		}
		
		return dto;
		
	}
	
	//Activa el producto identificado por idProducto si se encuentra en status == desactivado
	@GetMapping("/producto/activar")
	public DTO activarProducto(int idProducto){
		
		DTO dto = new DTO();
		
		Producto p = pr.findById(idProducto).get();
		dto.put("result", "error_activo");
		
		if(p.getActivo() == false) {
			this.pr.activarProducto(idProducto);
			dto.put("result", "correcto");
		}
		
		
		return dto;	
		
	}
	
	//Desactiva el producto identificado por idProducto si se encuentra en status == activado
	@GetMapping("/producto/desactivar")
	public DTO desactivar(int idProducto){
		
		DTO dto = new DTO();
		
		Producto p = pr.findById(idProducto).get();
		dto.put("result", "error_activo");
		
		if(p.getActivo() == true) {
			this.pr.desactivarProducto(idProducto);
			dto.put("result", "correcto");
		}
		
		
		return dto;	
		
	}
	
	//Crea un producto con los datos que se nos proporciona
	//Al requerir distintas tablas para crear el producto deben de crearse todas
	@PostMapping("/producto/crear")
	public DTO crearProducto(@RequestBody DatosProductoCrear datos) throws ParseException{
		DTO dto = new DTO();
		
		int id_producto = pr.getLastProducto() + 1;
		
		Producto p = new Producto();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String fecha = formatter.format(datos.fecha_salida);
		Date f = formatter.parse(fecha);
		
		//Insertamos el producto base
		pr.insertarProducto(id_producto,datos.nombre,datos.precio,datos.descripcion,datos.video,f,datos.desarrolladora,0,1);
		
		p = pr.findById(id_producto).get();
	
		//Una vez se ha insertado el producto base se insertan los requisitos minimos
		reqr.insertarRequisitos(reqr.getLastRequisito()+1,id_producto,"Mínimos",datos.so_min,datos.procesador_min,datos.memoria_min,datos.gpu_min,datos.almacenamiento_min);
	
		//Una vez se insertan los requisitos minimos se insertan los recomendados
		reqr.insertarRequisitos(reqr.getLastRequisito()+1,id_producto,"Recomendados",datos.so_rec,datos.procesador_rec,datos.memoria_rec,datos.gpu_rec,datos.almacenamiento_rec);
		
		//Se inserta la categoria del producto
		cpr.insertarCategoriaProducto(cpr.getLastCategoriaProducto()+1,id_producto,Integer.parseInt(datos.id_categoria));
		
		byte[] image = Base64.decodeBase64((String) datos.imagen);
		
		int id_img = 0;
		
		if(imgr.count() != 0) {
			id_img = imgr.getLastImagen() + 1;
		}
		
		//Inserta la imagen del producto
		imgr.insertarImagen(id_img,id_producto,image,0);
		
		dto.put("result", "correcto");
		
		return dto;
	}
	
	//Modifica el producto con los datos proporcionados
	@PostMapping("/producto/modificar")
	public DTO modificarProducto(@RequestBody DatosProductoModificar datos) throws ParseException{
		DTO dto = new DTO();
				
		Producto p = pr.findById(datos.id).get();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String fecha = formatter.format(datos.fecha_salida);
		Date f = formatter.parse(fecha);
		
		pr.modificarProducto(datos.nombre,datos.precio,datos.descripcion,datos.video,f,datos.desarrolladora, datos.descuento, datos.id);
	
		reqr.modificarRequisitosMin(datos.so_min, datos.procesador_min, datos.memoria_min, datos.gpu_min, datos.almacenamiento_min, datos.id);
	
		reqr.modificarRequisitosRec(datos.so_rec, datos.procesador_rec, datos.memoria_rec, datos.gpu_rec, datos.almacenamiento_rec, datos.id);
		
		cpr.modificarCategoriaProducto(Integer.parseInt(datos.id_categoria),datos.id);
		
		System.out.println(datos.imagen);
		
		byte[] image = Base64.decodeBase64((String) datos.imagen);
		
		int id_img = 0;
		
		if(imgr.obtenerImagenProducto(datos.id) != null) {
			imgr.modificarImagen(image,datos.id);
		}else {
			if(imgr.count() != 0) {
				id_img = imgr.getLastImagen() + 1;
			}
			imgr.insertarImagen(id_img,datos.id,image,0);
		}
		
		
		dto.put("result", "correcto");
		
		return dto;
	}
	
	//Función que nos permite comprobar si un producto existe entre los pedidos del usuario
	@PostMapping("/producto/comprobarComprado")
	public DTO comprobarComprado (@RequestBody DatosProductoUsuario datos) {
		
		DTO dto = new DTO();
		
		dto.put("resultado", "no_existe");
		
		System.out.println(datos.id_producto);
		
		Lineapedido lp = lpr.obtenerLineaProducto(datos.id_producto);
		if(lp != null) {
			System.out.println(lp.getId());
			int idLineaPedido = lpr.obtenerLineaProducto(datos.id_producto).getId();
			
			Pedido p = pedr.obtenerPedido(idLineaPedido, datos.id_usuario);
			
			if(p != null) {
				dto.put("resultado", "existe");
			}
		}
		
		return  dto;
	}

	
	}

class DatosProductoUsuario {
	int id_producto;
	int id_usuario;
	public DatosProductoUsuario(int id_producto, int id_usuario) {
		super();
		this.id_producto = id_producto;
		this.id_usuario = id_usuario;
	}
	
	
}

//Obtenemos los datos del producto que quiere crearse, los recibimos por POST en angular
class DatosProductoCrear {
	String nombre;
	Float precio;
	String descripcion;
	String video;
	Date fecha_salida;
	String desarrolladora;
	String id_categoria;
	String imagen;
	String so_min;
	String procesador_min;
	String memoria_min;
	String gpu_min;
	String almacenamiento_min;
	String so_rec;
	String procesador_rec;
	String memoria_rec;
	String gpu_rec;
	String almacenamiento_rec;
	
	public DatosProductoCrear(String nombre, Float precio, String descripcion, String video, Date	 fecha_salida,
			String desarrolladora, String id_categoria, String imagen, String so_min, String procesador_min,
			String memoria_min, String gpu_min, String almacenamiento_min, String so_rec, String procesador_rec,
			String memoria_rec, String gpu_rec, String almacenamiento_rec) {
		super();
		this.nombre = nombre;
		this.precio = precio;
		this.descripcion = descripcion;
		this.video = video;
		this.fecha_salida = fecha_salida;
		this.desarrolladora = desarrolladora;
		this.id_categoria = id_categoria;
		this.imagen = imagen;
		this.so_min = so_min;
		this.procesador_min = procesador_min;
		this.memoria_min = memoria_min;
		this.gpu_min = gpu_min;
		this.almacenamiento_min = almacenamiento_min;
		this.so_rec = so_rec;
		this.procesador_rec = procesador_rec;
		this.memoria_rec = memoria_rec;
		this.gpu_rec = gpu_rec;
		this.almacenamiento_rec = almacenamiento_rec;
	}
	
	
	
}

//Obtenemos los datos del producto que quiere modificarse, los recibimos por POST en angular
class DatosProductoModificar {
	int id;
	String nombre;
	Float precio;
	String descripcion;
	String video;
	Date fecha_salida;
	String desarrolladora;
	int descuento;
	String id_categoria;
	String imagen;
	String so_min;
	String procesador_min;
	String memoria_min;
	String gpu_min;
	String almacenamiento_min;
	String so_rec;
	String procesador_rec;
	String memoria_rec;
	String gpu_rec;
	String almacenamiento_rec;
	
	public DatosProductoModificar(int id,String nombre, Float precio, String descripcion, String video, Date	 fecha_salida,
			String desarrolladora, int descuento, String id_categoria, String imagen, String so_min, String procesador_min,
			String memoria_min, String gpu_min, String almacenamiento_min, String so_rec, String procesador_rec,
			String memoria_rec, String gpu_rec, String almacenamiento_rec) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
		this.descripcion = descripcion;
		this.video = video;
		this.fecha_salida = fecha_salida;
		this.desarrolladora = desarrolladora;
		this.descuento = descuento;
		this.id_categoria = id_categoria;
		this.imagen = imagen;
		this.so_min = so_min;
		this.procesador_min = procesador_min;
		this.memoria_min = memoria_min;
		this.gpu_min = gpu_min;
		this.almacenamiento_min = almacenamiento_min;
		this.so_rec = so_rec;
		this.procesador_rec = procesador_rec;
		this.memoria_rec = memoria_rec;
		this.gpu_rec = gpu_rec;
		this.almacenamiento_rec = almacenamiento_rec;
	}
	
	
	
}




