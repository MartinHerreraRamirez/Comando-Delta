package com.comando.delta.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comando.delta.enums.TypeEquipment;
import com.comando.delta.model.Equipment;

@Repository
public interface IEquipmentRepository extends JpaRepository<Equipment, Long>{
    @Query("SELECT e FROM Equipment e ORDER BY e.id DESC")
    List<Equipment> findEquipmentList();

    @Query("SELECT e FROM Equipment e WHERE LOWER(e.number) = LOWER(:number) ORDER BY e.id DESC")
    List<Equipment> findEquipmentListByNumber(@Param("number") String number);

    @Query("SELECT e FROM Equipment e WHERE e.typeEquipment = :type AND e.haveSim = false ORDER BY e.id DESC")
    List<Equipment> findActiveEquipmentListByType(@Param("type") TypeEquipment type);

    @Query("SELECT e FROM Equipment e WHERE e.typeEquipment = :type ORDER BY e.id DESC")
    List<Equipment> findEquipmentListByType(@Param("type") TypeEquipment type);

    @Query("SELECT e FROM Equipment e WHERE e.typeEquipment = :type AND e.isAvailable = true ORDER BY e.id DESC")
    List<Equipment> findEquipmentListByFalseStatusisAvailable(@Param("type") TypeEquipment type);

    @Query("SELECT COUNT(e) > 0 FROM Equipment e WHERE e.number = :equipmentNumber AND e.typeEquipment = :equipmentType")
    Boolean existenceEquipment(@Param("equipmentNumber") String equipmentNumber, @Param("equipmentType") TypeEquipment equipmentType);   
    
    @Query("SELECT COUNT(e) > 0 FROM Equipment e WHERE e.number = :equipmentNumber AND e.typeEquipment = :equipmentType AND (:id IS NULL OR e.id != :id)")
    Boolean existenceUpdateEquipment(@Param("equipmentNumber") String equipmentNumber, @Param("equipmentType") TypeEquipment equipmentType, @Param("id") Long id);

    @Query("SELECT e FROM Equipment e WHERE LOWER(e.number) = LOWER(:number)")
    Equipment findEquipmentByNumber(@Param("number") String number);
    
}
