package com.comando.delta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.comando.delta.model.Image;

@Repository
public interface IImageRepository extends JpaRepository<Image, Long>{
    
}
