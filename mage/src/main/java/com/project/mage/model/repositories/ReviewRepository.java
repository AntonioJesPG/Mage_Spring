package com.project.mage.model.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.mage.model.Producto;
import com.project.mage.model.Review;

@Repository
public interface ReviewRepository extends CrudRepository<Review,Integer>{
	
	
	@Transactional
	@Modifying
	@Query(value = "Update review set mensaje = ? where id = ?", nativeQuery = true)
	public void modificarComentario(String mensaje, int id);
	
	@Transactional
	@Modifying
	@Query(value = "Update review set mensaje = ?, valoracion = ? where id = ?", nativeQuery = true)
	public void modificarComentarioUsuario(String mensaje, int valoracion, int id);
	
	@Transactional
	@Modifying
	@Query(value = "Insert into review values (?,?,?,?,?,?)", nativeQuery = true)
	public void insertarReview(int id, int id_usuario, int id_producto,String mensaje, Date fecha, int valoracion);
	
	@Query(value = "SELECT * FROM review WHERE id_producto = ?", nativeQuery = true)
	public List<Review> getComentariosProducto(int id_producto);
	
	@Query(value = "SELECT * FROM review WHERE id_producto = ? AND id_usuario = ?", nativeQuery = true)
	public Review findByIdUsuarioProducto(int id_producto, int id_usuario);
	
	@Transactional
	@Query(value = "Select id from review ORDER BY id DESC LIMIT 1; ", nativeQuery = true)
	public int getLastReview();
	
	@Transactional
	@Modifying
	@Query(value = "Delete from review where id_usuario = ?", nativeQuery = true)
	public void eliminarReviewUsuario(int id_usuario);
}
