package com.project.mage.model.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.mage.model.Lineapedido;
import com.project.mage.model.Pedido;
import com.project.mage.model.Ticketusuario;
import com.project.mage.model.Usuario;

@Repository
public interface PedidoRepository extends CrudRepository<Pedido,Integer>{
	
	//public Ticketusuario findById_ticketAndEstado(int id_ticket, int estado);
	
	@Transactional
	@Query(value = "Select * from pedido where id_linea_pedido = ? && id_usuario = ? ", nativeQuery = true)
	public Pedido obtenerPedido(int id_linea_pedido, int id_usuario);
	
	@Transactional
	@Query(value = "Select * from pedido where codigo = ? ", nativeQuery = true)
	public List<Pedido> obtenerPedidosCodigo(String codigo);
	
	@Transactional
	@Query(value = "Select * from pedido where id_usuario = ? group by(codigo)", nativeQuery = true)
	public List<Pedido> obtenerPedidos(int id_usuario);
	
	@Transactional
	@Query(value = "Select precio_compra_pedido from pedido where id_usuario = ? and codigo = ?", nativeQuery = true)
	public List<Float> obtenerPreciosPedido(int id_usuario, String codigo);
	
	@Query(value = "SELECT * FROM pedido WHERE codigo = ? ORDER BY id LIMIT 1;", nativeQuery = true)
	public Pedido comprobarCodigo(String codigo);
	
	@Transactional
	@Query(value = "Select id from pedido ORDER BY id DESC LIMIT 1; ", nativeQuery = true)
	public int getLastPedido();
	
	@Transactional
	@Modifying
	@Query(value = "Insert into pedido values (?,?,?,?,?,?)", nativeQuery = true)
	public void agregarPedido(int id,int id_usuario,int id_linea_pedido, float precio_compra_pedido, Date fecha, String codigo);
	
	@Transactional
	@Modifying
	@Query(value = "Delete from pedido where id_usuario = ?", nativeQuery = true)
	public void eliminarPedido(int id_usuario);
}
