package com.project.mage.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.mage.model.Ticketusuario;
import com.project.mage.model.Usuario;

@Repository
public interface TicketRepository extends CrudRepository<Ticketusuario,Integer>{
	
	//public Ticketusuario findById_ticketAndEstado(int id_ticket, int estado);
	
	@Transactional
	@Query(value = "Select * from ticketusuario where id_ticket = ? and estado = 0", nativeQuery = true)
	public List<Ticketusuario> obtenerPorCategoria(int id_ticket);
	
	@Transactional
	@Query(value = "Select * from ticketusuario where id_usuario = ?", nativeQuery = true)
	public List<Ticketusuario> obtenerParaUsuario(int id_usuario);
	
	@Transactional
	@Query(value = "Select * from ticketusuario where estado = 1", nativeQuery = true)
	public List<Ticketusuario> obtenerResueltos();
	
	@Transactional
	@Modifying
	@Query(value = "Update ticketusuario set estado = 1 where id = ?", nativeQuery = true)
	public void resolverTicket(int idTicket);
	
	@Query(value = "SELECT id FROM ticketusuario ORDER BY id DESC LIMIT 1;", nativeQuery = true)
	public int getLastTicket();
	
	@Transactional
	@Modifying
	@Query(value = "Insert into ticketusuario values (?,?,?,?,?,?)", nativeQuery = true)
	public void insertarTicket(int id, int id_usuario, int id_ticket, String mensaje, String estado, String fecha);
	
	@Transactional
	@Modifying
	@Query(value = "Update ticketusuario set mensaje = ? , fecha_creacion = ? where id = ?", nativeQuery = true)
	public void modificarTicket(String mensaje, String fecha, int id);
	
	@Transactional
	@Modifying
	@Query(value = "Delete from ticketusuario where id_usuario = ?", nativeQuery = true)
	public void eliminarTicketUsuario(int id_usuario);
}
