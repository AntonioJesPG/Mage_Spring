package com.project.mage.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.mage.model.Lineapedido;
import com.project.mage.model.Ticketusuario;
import com.project.mage.model.Usuario;

@Repository
public interface LineaPedidoRepository extends CrudRepository<Lineapedido,Integer>{
	
	//public Ticketusuario findById_ticketAndEstado(int id_ticket, int estado);
	
	@Transactional
	@Query(value = "Select * from lineapedido where id_producto = ? ", nativeQuery = true)
	public Lineapedido obtenerLineaProducto(int id_producto);
	
	@Transactional
	@Query(value = "Select id from lineapedido ORDER BY id DESC LIMIT 1; ", nativeQuery = true)
	public int getLastLineaPedido();
	
	@Transactional
	@Modifying
	@Query(value = "Insert into lineapedido values (?,?,?)", nativeQuery = true)
	public void agregarLineaPedido(int id,int id_producto, float precio_compra);
	
	@Transactional
	@Modifying
	@Query(value = "Update lineapedido set precio_compra = ? where id_producto = ?", nativeQuery = true)
	public void modificarLineaPedido(float precio_compra, int id_producto);
	
}
