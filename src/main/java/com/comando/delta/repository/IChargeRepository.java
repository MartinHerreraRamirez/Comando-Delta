package com.comando.delta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comando.delta.model.Charge;

@Repository
public interface IChargeRepository extends JpaRepository<Charge, Long> {    
    @Query("SELECT c FROM Charge c ORDER BY c.id DESC")
    List<Charge> findChargeList();

    @Query("SELECT c FROM Charge c WHERE c.typeCharge = 'TECNICA' ORDER BY c.id DESC")
    List<Charge> findChargeListByTechnicalType();

    @Query("SELECT c FROM Charge c WHERE c.typeCharge = 'BRIGADA' ORDER BY c.id DESC")
    List<Charge> findChargeListByBrigadeType();

    @Query("SELECT c FROM Charge c WHERE c.typeCharge = 'ADMINISTRATIVO' ORDER BY c.id DESC")
    List<Charge> findChargeListByAdministrativeType();    

    @Query("SELECT c FROM Charge c WHERE c.file = :file")
    Charge findOneChargeByNumberFile(@Param("file") String file);

    @Query("SELECT COUNT(c) > 0 FROM Charge c WHERE c.file = :file")
    Boolean existenceCharge(@Param("file") String file);  
    
    @Query("SELECT COUNT(c) FROM Charge c WHERE c.file = :file")
    Long existenceEditedCharge(@Param("file") String file);
}
