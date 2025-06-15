package com.comando.delta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.comando.delta.model.Dependency;

@Repository
public interface IDependencyRepository extends JpaRepository<Dependency, Long> {

    @Query("SELECT d FROM Dependency d ORDER BY d.id DESC")
    List<Dependency> findDependencyList();
}
