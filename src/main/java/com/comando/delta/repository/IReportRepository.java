package com.comando.delta.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comando.delta.model.Report;

@Repository
public interface IReportRepository extends JpaRepository<Report, Long>{

    @Query(value="SELECT * FROM REPORT ORDER BY report_number ASC LIMIT 10", nativeQuery = true)
    public List<Report> findLastReports();

    @Query("SELECT r FROM Report r WHERE r.reportNumber = :reportNumber")
    public Report findReportByNumber(@Param("reportNumber") String reportNumber); 

    @Query("SELECT r FROM Report r WHERE LOWER(r.address) = LOWER(:address)")
    public Report findReportByAddress(@Param("address") String address);

    @Query("SELECT r FROM Report r WHERE r.id = :id")
    public Report findReportById(@Param("id") Long id);

    @Query("SELECT COUNT(r) > 0 FROM Report r WHERE r.reportNumber = :reportNumber")
    public Boolean existenceReportNumber(@Param("reportNumber") String reportNumber);

    @Query("SELECT COUNT(r) > 0 FROM Report r WHERE r.gdeNumber = :gdeNumber")
    public Boolean existenceGDENumber(@Param("gdeNumber") String gdeNumber);
    
    @Query("SELECT COUNT(r) > 0 FROM Report r WHERE r.reportNumber = :reportNumber AND (:id IS NULL OR r.id != :id)")
    public Boolean existenceUpdateReportNumber(@Param("reportNumber") String reportNumber, @Param("id") Long id);

    @Query("SELECT COUNT(r) > 0 FROM Report r WHERE r.gdeNumber = :gdeNumber AND (:id IS NULL OR r.id != :id)")
    public Boolean existenceUpdateGDENumber(@Param("gdeNumber") String gdeNumber, @Param("id") Long id);
}
