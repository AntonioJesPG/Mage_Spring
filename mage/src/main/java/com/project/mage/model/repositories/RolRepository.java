package com.project.mage.model.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.mage.model.Rol;
import com.project.mage.model.Rolusuario;

@Repository
public interface RolRepository extends CrudRepository<Rol,Integer>{

}
