package com.project.mage.model.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.mage.model.Rolusuario;

@Repository
public interface RolusuarioRepository extends CrudRepository<Rolusuario,Integer>{
	
	@Transactional
	@Modifying
	@Query(value = "Update rolusuario set id_rol = ? where id_usuario = ?", nativeQuery = true)
	public void modificarRol(int id_rol, int id);

	@Query(value = "SELECT id FROM rolusuario ORDER BY id DESC LIMIT 1;", nativeQuery = true)
	public int getLastRolUsuario();
	
	@Transactional
	@Modifying
	@Query(value = "Insert into rolusuario values (?,?,?)", nativeQuery = true)
	public void insertarRolUsuario(int id, int id_usuario, int id_rol);
	
	@Transactional
	@Modifying
	@Query(value = "Delete from rolusuario where id_usuario = ?", nativeQuery = true)
	public void eliminarRolUsuario(int id_usuario);

}
