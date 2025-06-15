package com.comando.delta.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comando.delta.enums.TypeEquipment;
import com.comando.delta.model.AssignEquipment;

@Repository
public interface IAssignEquipmentRepository extends JpaRepository<AssignEquipment, Long>{    
    @Query("SELECT ae FROM AssignEquipment ae WHERE ae.assignEquipment.typeEquipment = :type AND ae.isActive = true")
    List<AssignEquipment> findAssignEquipmentListByTypeEquipmentAndTrueStatus(@Param("type") TypeEquipment type);

    @Query("SELECT ae FROM AssignEquipment ae WHERE ae.assignEquipment.typeEquipment = :type AND ae.isActive = false")
    List<AssignEquipment> findAssignEquipmentListByTypeEquipmentAndFalseStatus(@Param("type") TypeEquipment type);

    @Query("SELECT ae FROM AssignEquipment ae WHERE ae.assignEquipment.number = :number OR ae.sim.number = :number")
    Optional<AssignEquipment> findAssignEquipmentBySimNumberOrEquipNumber(@Param("number") String number);    
}
