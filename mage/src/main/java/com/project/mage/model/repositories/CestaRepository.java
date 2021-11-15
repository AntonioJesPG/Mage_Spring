package com.project.mage.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.mage.model.Categoria;
import com.project.mage.model.Cesta;
import com.project.mage.model.Imagenesproducto;
import com.project.mage.model.Tipoticket;


@Repository
public interface CestaRepository extends CrudRepository<Cesta,Integer>{
	
	@Transactional
	@Query(value = "Select * from cesta where id_usuario = ?", nativeQuery = true)
	public List<Cesta> obtenerCesta(int id_usuario);
	
	@Transactional
	@Query(value = "Select * from cesta where id_usuario = ? AND id_linea_cesta = ?", nativeQuery = true)
	public Cesta obtenerCestaFromLinea(int id_usuario, int id_linea_cesta);
	
	@Transactional
	@Modifying
	@Query(value = "Delete from cesta where id_usuario = ?", nativeQuery = true)
	public void eliminarCestaUsuario(int id_usuario);
	
	@Transactional
	@Modifying
	@Query(value = "Delete from cesta where id_usuario = ? AND id_linea_cesta = ?", nativeQuery = true)
	public void eliminarLineaCestaUsuario(int id_usuario, int id_linea_cesta);
	
	@Transactional
	@Query(value = "Select id from cesta ORDER BY id DESC LIMIT 1; ", nativeQuery = true)
	public int getLastCesta();
	
	@Transactional
	@Modifying
	@Query(value = "Insert into cesta values (?,?,?)", nativeQuery = true)
	public void agregarCesta(int id,int id_usuario,int id_linea_cesta);
	
	
}
