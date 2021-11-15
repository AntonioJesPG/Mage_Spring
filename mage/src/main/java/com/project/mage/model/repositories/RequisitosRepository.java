package com.project.mage.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.mage.model.Requisitosproducto;
import com.project.mage.model.Ticketusuario;
import com.project.mage.model.Usuario;

@Repository
public interface RequisitosRepository extends CrudRepository<Requisitosproducto,Integer>{
	
	//public Ticketusuario findById_ticketAndEstado(int id_ticket, int estado);
	
	@Transactional
	@Query(value = "Select * from requisitosproducto where id_producto = ? and tipo = 'Mínimos'", nativeQuery = true)
	public Requisitosproducto obtenerMinimos(int id_producto);
	
	@Transactional
	@Query(value = "Select * from requisitosproducto where id_producto = ? and tipo = 'Recomendados'", nativeQuery = true)
	public Requisitosproducto obtenerRecomendados(int id_producto);
	
	@Query(value = "SELECT id FROM requisitosproducto ORDER BY id DESC LIMIT 1;", nativeQuery = true)
	public int getLastRequisito();
	
	@Transactional
	@Modifying
	@Query(value = "Update requisitosproducto set so = ? , procesador = ?, memoria = ?, gpu = ?, almacenamiento = ? where tipo='Mínimos' and id_producto = ?", nativeQuery = true)
	public void modificarRequisitosMin(String so, String procesador, String memoria, String gpu, String almacenamiento, int id_producto);
	
	@Transactional
	@Modifying
	@Query(value = "Update requisitosproducto set so = ? , procesador = ?, memoria = ?, gpu = ?, almacenamiento = ? where tipo='Recomendados' and id_producto = ?", nativeQuery = true)
	public void modificarRequisitosRec(String so, String procesador, String memoria, String gpu, String almacenamiento, int id_producto);
	
	@Transactional
	@Modifying
	@Query(value = "Insert into requisitosproducto values (?,?,?,?,?,?,?,?)", nativeQuery = true)
	public void insertarRequisitos(int id, int id_producto, String tipo, String so, String procesador, String memoria, String gpu, String almacenamiento);
}
