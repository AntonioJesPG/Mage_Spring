package com.project.mage.model.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.mage.model.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario,Integer>{
	
	public Usuario findByEmail(String email);
	
	public Usuario findByDni(String dni);
	
	public Usuario findByUsername(String username);
	
	@Query(value = "SELECT * FROM usuario WHERE email = ? AND password = ?;", nativeQuery = true)
	public Usuario findByEmailAndPassword(String email, String password);
	
	@Query(value = "SELECT id FROM usuario ORDER BY id DESC LIMIT 1;", nativeQuery = true)
	public int getLastUsuario();
	
	@Transactional
	@Modifying
	@Query(value = "Insert into usuario values (?,?,?,?,?,?,?,?,?,?)", nativeQuery = true)
	public void insertarUsuario(int id, String username, String nombre, String primer_apellido, String segundo_apellido, String dni, String email, String password, Float saldo, String imagen);

	@Transactional
	@Modifying
	@Query(value = "Update usuario set username = ? , nombre = ?, primer_apellido = ?, segundo_apellido = ?, dni = ? where id = ?", nativeQuery = true)
	public void modificarUsuario(String username, String nombre, String primer_apellido, String segundo_apellido, String dni, int id);
	
	@Transactional
	@Modifying
	@Query(value = "Update usuario set username = ? , nombre = ?, primer_apellido = ?, segundo_apellido = ? where id = ?", nativeQuery = true)
	public void modificarUsuarioNormal(String username, String nombre, String primer_apellido, String segundo_apellido, int id);
}
