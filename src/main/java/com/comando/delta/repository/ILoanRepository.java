package com.comando.delta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comando.delta.enums.TypeEquipment;
import com.comando.delta.model.Loan;

@Repository
public interface ILoanRepository extends JpaRepository<Loan, Long> {    
    @Query("SELECT l FROM Loan l ORDER BY l.id DESC")
    List<Loan> findLoanList();

    @Query("SELECT l FROM Loan l WHERE l.equipment.number = :number ORDER BY l.id DESC")
    List<Loan> findLoanListByEquipmentNumber(@Param("number") String number); 

    @Query("SELECT l FROM Loan l WHERE l.equipment.typeEquipment = :type ORDER BY l.id DESC")
    List<Loan> findLoanListByTypeEquipment(@Param("type") TypeEquipment type);

    @Query("SELECT l FROM Loan l WHERE l.isActive = true ORDER BY l.id DESC")
    List<Loan> findActiveLoanList();

    @Query("SELECT l FROM Loan l WHERE l.isActive = false ORDER BY l.id DESC")
    List<Loan> findInactiveLoanList();

    @Query("SELECT l FROM Loan l WHERE l.equipment.typeEquipment = :type AND l.isActive = true ORDER BY l.id DESC")
    List<Loan> findLoanListByTypeEquipmentAndStatusTrue(@Param("type") TypeEquipment type);

    @Query("SELECT l FROM Loan l WHERE l.equipment.typeEquipment = :type AND l.isActive = false ORDER BY l.id DESC")
    List<Loan> findLoanListByTypeEquipmentAndStatusFalse(@Param("type") TypeEquipment type);
}
