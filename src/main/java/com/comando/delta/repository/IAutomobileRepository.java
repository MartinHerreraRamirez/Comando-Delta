package com.comando.delta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comando.delta.model.Automobile;

@Repository
public interface IAutomobileRepository extends JpaRepository<Automobile, Long>{
    
    @Query("SELECT a FROM Automobile a ORDER BY a.id DESC")
    List<Automobile> findAutomobileList();

    @Query("SELECT a FROM Automobile a WHERE a.id = :id")
    Automobile findAutomobileById(@Param("id") Long id);

    @Query("SELECT a FROM Automobile a WHERE a.number = :number")
    Automobile findAutomobileByNumber(@Param("number") String number);
}
