package com.project.mage.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.mage.model.Categoria;
import com.project.mage.model.Categoriaproducto;
import com.project.mage.model.Tipoticket;


@Repository
public interface CategoriaProductoRepository extends CrudRepository<Categoriaproducto,Integer>{
	
	@Query(value = "SELECT id FROM categoriaproducto ORDER BY id DESC LIMIT 1;", nativeQuery = true)
	public int getLastCategoriaProducto();
	
	@Query(value = "SELECT * FROM categoriaproducto WHERE id_producto = ?;", nativeQuery = true)
	public Categoriaproducto obtenerCategoriaPorProducto(int id_producto);
	
	@Transactional
	@Modifying
	@Query(value = "Insert into categoriaproducto values (?,?,?)", nativeQuery = true)
	public void insertarCategoriaProducto(int id, int id_producto, int id_categoria);
	
	@Transactional
	@Modifying
	@Query(value = "Update categoriaproducto set id_categoria = ? where id_producto = ?", nativeQuery = true)
	public void modificarCategoriaProducto(int id_categoria, int id_producto);
}
