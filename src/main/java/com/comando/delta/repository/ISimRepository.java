package com.comando.delta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comando.delta.model.Sim;

@Repository
public interface ISimRepository extends JpaRepository<Sim, Long>{
    @Query("SELECT s FROM Sim s ORDER BY s.id DESC")
    List<Sim> findSimList();

    @Query("SELECT s FROM Sim s WHERE s.id = :id")
    Sim findOneSimById(@Param("id") Long id);

    @Query("SELECT s FROM Sim s WHERE s.number = :number")
    Sim findOneSimByNumber(@Param("number") String number);

    @Query("SELECT s FROM Sim s WHERE s.serialNumber = :serialNumber")
    Sim findOneSimBySerialNumber(@Param("serialNumber") String serialNumber);

    @Query("SELECT COUNT(s) > 0 FROM Sim s WHERE s.number = :number")
    Boolean existenceNumber(@Param("number") String number);

    @Query("SELECT COUNT(s) > 0 FROM Sim s WHERE s.serialNumber = :serialNumber")
    Boolean existenceSerialNumber(@Param("serialNumber") String serialNumber);

    @Query("SELECT COUNT(s) > 0 FROM Sim s WHERE s.number = :number AND (:id IS NULL OR s.id != :id)" )
    Boolean existenceUpdatedNumber(@Param("number") String number, @Param("id") Long id);

    @Query("SELECT COUNT(s) > 0 FROM Sim s WHERE s.serialNumber = :serialNumber AND (:id IS NULL OR s.id != :id)")
    Boolean existenceUpdatedSerialNumber(@Param("serialNumber") String serialNumber, @Param("id") Long id);

    @Query("SELECT s FROM Sim s WHERE s.isAvailable = true")
    List<Sim> findActiveSimList();   

}
