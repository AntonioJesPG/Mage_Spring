package com.project.mage.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.mage.model.Tipoticket;


@Repository
public interface CategoriaTicketRepository extends CrudRepository<Tipoticket,Integer>{
	

}
