package com.project.mage.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.mage.model.Lineacesta;
import com.project.mage.model.Lineapedido;
import com.project.mage.model.Ticketusuario;
import com.project.mage.model.Usuario;

@Repository
public interface LineaCestaRepository extends CrudRepository<Lineacesta,Integer>{
	
	//public Ticketusuario findById_ticketAndEstado(int id_ticket, int estado);
	
	@Transactional
	@Query(value = "Select * from lineacesta where id_producto = ? ", nativeQuery = true)
	public Lineacesta obtenerLineaProducto(int id_producto);
	
	@Transactional
	@Query(value = "Select id from lineacesta ORDER BY id DESC LIMIT 1; ", nativeQuery = true)
	public int getLastLineaCesta();
	
	@Transactional
	@Modifying
	@Query(value = "Insert into lineacesta values (?,?,?)", nativeQuery = true)
	public void agregarLineaCesta(int id,int id_producto, float precio_compra);
	
	@Transactional
	@Modifying
	@Query(value = "Update lineacesta set precio_compra = ? where id_producto = ?", nativeQuery = true)
	public void modificarLineaCesta(float precio_compra, int id_producto);
	
}
