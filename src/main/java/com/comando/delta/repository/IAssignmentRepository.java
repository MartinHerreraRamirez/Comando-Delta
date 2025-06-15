package com.comando.delta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comando.delta.model.Assignment;

@Repository
public interface IAssignmentRepository extends JpaRepository<Assignment, Long>{
    @Query("SELECT a FROM Assignment a WHERE a.isActive = true ORDER BY a.id DESC")
    List<Assignment> findActiveAssignmentList();   

    @Query("SELECT a FROM Assignment a WHERE a.assignEquipment.number = :number ORDER BY a.id DESC")
    List<Assignment> findAssignmentListByEquipmentNumber(@Param("number") String number);

    @Query("SELECT a FROM Assignment a WHERE a.sim.number = :number ORDER BY a.id DESC")
    List<Assignment> findAssigmentListByPhoneNumber(@Param("number") String number);

    @Query("SELECT a FROM Assignment a WHERE a.assignmentType = :type AND a.isActive = false ORDER BY a.id DESC")
    List<Assignment> findAssignmentListByTypeAndStatusInactive(@Param("type") String type);

    @Query("SELECT a FROM Assignment a WHERE a.assignmentType = :type AND a.isActive = true ORDER BY a.id DESC")
    List<Assignment> findAssignmentListByTypeAndStatusActive(@Param("type") String type);

    @Query("SELECT a FROM Assignment a WHERE a.assignmentType = :type ORDER BY a.id DESC")
    List<Assignment> findAssignmentListByType(@Param("type") String type);

    
}
