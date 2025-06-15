package com.comando.delta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comando.delta.model.Individual;

@Repository
public interface IIndividualRepository extends JpaRepository<Individual, Long>{
    @Query("SELECT i FROM Individual i ORDER BY i.id DESC")
    List<Individual> findIndividualList();
    
    @Query("SELECT COUNT(i) > 0 FROM Individual i WHERE i.dependency.id = :dependencyId")
    boolean isDependencyInUse(@Param("dependencyId") Long dependencyId);

    @Query("SELECT i FROM Individual i WHERE i.personalFile = :personalFile")
    Individual findIndividualByPersonalFile(@Param("personalFile") String personalFile);

    @Query("SELECT i FROM Individual i WHERE LOWER(i.dependency.name) = LOWER(:dependencyName) ORDER BY i.id DESC")
    List<Individual> findIndividualListByDependency(@Param("dependencyName") String dependencyName);

    @Query("SELECT i FROM Individual i WHERE LOWER(i.dependency.name) = LOWER(:loanIndividualDependency) ORDER BY i.id DESC")
    List<Individual> findIndividualListByLoan(@Param("loanIndividualDependency") String loanIndividualDependency);   
}
