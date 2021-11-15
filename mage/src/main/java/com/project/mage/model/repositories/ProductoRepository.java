package com.project.mage.model.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.mage.model.Producto;

@Repository
public interface ProductoRepository extends CrudRepository<Producto,Integer>{
	@Transactional
	@Modifying
	@Query(value = "Update producto set activo = 1 where id = ?", nativeQuery = true)
	public void activarProducto(int idProducto);
	
	@Transactional
	@Modifying
	@Query(value = "Update producto set activo = 0 where id = ?", nativeQuery = true)
	public void desactivarProducto(int idProducto);
	
	@Query(value = "SELECT id FROM producto ORDER BY id DESC LIMIT 1;", nativeQuery = true)
	public int getLastProducto();
	
	@Query(value = "SELECT * FROM producto WHERE activo = 1 ORDER BY id LIMIT 1;", nativeQuery = true)
	public Producto getPrimerDestacado();
	
	@Query(value = "SELECT * FROM producto WHERE activo = 1 ORDER BY descuento DESC LIMIT 4;", nativeQuery = true)
	public List<Producto> getGrandesDescuentos();
	
	@Query(value = "SELECT * FROM producto WHERE activo = 1 ORDER BY id DESC LIMIT 5;", nativeQuery = true)
	public List<Producto> getNuevos();
	
	@Query(value = "SELECT * FROM producto WHERE activo = 1", nativeQuery = true)
	public List<Producto> getCatalogo();
	
	@Query(value = "SELECT * FROM producto WHERE activo = 1 AND precio < 10 ORDER BY id LIMIT 5;", nativeQuery = true)
	public List<Producto> getMenos10();
	
	@Query(value = "SELECT * FROM producto WHERE activo = 1 AND precio > 10  AND precio < 20  ORDER BY id LIMIT 5;", nativeQuery = true)
	public List<Producto> getMenos20();
	
	@Query(value = "Select * from producto  where nombre like %?%", nativeQuery = true)
	public List<Producto> findByNombre(String nombre);
	
	@Transactional
	@Modifying
	@Query(value = "Insert into producto values (?,?,?,?,?,?,?,?,?)", nativeQuery = true)
	public void insertarProducto(int id, String nombre, float precio, String descripcion, String video, Date fecha_salida, String desarrolladora, int descuento, int activo);
	
	@Transactional
	@Modifying
	@Query(value = "Update producto set nombre = ?, precio = ?, descripcion = ?, video = ?, fecha_salida = ?, desarrolladora = ?, descuento = ? where id = ?", nativeQuery = true)
	public void modificarProducto(String nombre, Float precio, String descripcion, String video, Date fecha_salida, String desarrolladora, int descuento, int id);
}
