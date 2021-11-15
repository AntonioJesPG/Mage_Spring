package com.project.mage.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.mage.model.Imagenesproducto;
import com.project.mage.model.Requisitosproducto;
import com.project.mage.model.Ticketusuario;
import com.project.mage.model.Usuario;

@Repository
public interface ImagenRepository extends CrudRepository<Imagenesproducto,Integer>{
	
	@Transactional
	@Query(value = "Select * from imagenesproducto where id_producto = ?", nativeQuery = true)
	public Imagenesproducto obtenerImagenProducto(int id_producto);
	
	@Query(value = "SELECT id FROM imagenesproducto ORDER BY id DESC LIMIT 1;", nativeQuery = true)
	public int getLastImagen();
	
	@Transactional
	@Modifying
	@Query(value = "Update imagenesproducto set imagen = ? where id_producto = ?", nativeQuery = true)
	public void modificarImagen(byte[] imagen, int id_producto);
	
	@Transactional
	@Modifying
	@Query(value = "Insert into imagenesproducto values (?,?,?,?)", nativeQuery = true)
	public void insertarImagen(int id, int id_producto, byte[] imagen, int tipo);
}
