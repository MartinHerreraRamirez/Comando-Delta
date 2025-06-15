package com.comando.delta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.comando.delta.model.Hierarchy;

@Repository
public interface IHierarchyRepository extends JpaRepository<Hierarchy, Long>{
    @Query("SELECT h FROM Hierarchy h ORDER BY h.id DESC")
    List<Hierarchy> findHierarchyList();
}
