package com.comando.delta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comando.delta.model.AssignPerson;

@Repository
public interface IAssignPersonRepository extends JpaRepository<AssignPerson, Long>{
    @Query("SELECT ap FROM AssignPerson ap WHERE ap.isActive = true")
    List<AssignPerson> findAssignPersonListByTrueStatus();

    @Query("SELECT ap FROM AssignPerson ap WHERE ap.isActive = false")
    List<AssignPerson> findAssignPersonListByFalseStatus();

    @Query("SELECT ap FROM AssignPerson ap WHERE ap.sim.id = :id AND ap.isActive = true")
    AssignPerson findAssignPersonBySimId(@Param("id") Long id);

    // @Query("SELECT ap FROM AssignPerson ap WHERE ap.assignEquipment.number = :number OR ap.sim.number = :number")
    // Optional<AssignPerson> findAssignPersonBySimNumberOrEquipmentNumber(@Param("number") String number);    
}
