package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.RoleEntity;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long>{

}
