package com.comando.delta.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comando.delta.exception.MyException;
import com.comando.delta.model.Report;
import com.comando.delta.model.Users;
import com.comando.delta.repository.IReportRepository;

@Service
public class ReportService {    
    @Autowired
    private IReportRepository reportRepository;

    @Autowired
    private HttpSession session;

    @Transactional
    public void createReport(
    String reportNumber,
    String gdeNumber, 
    String location,
    String address,
    String dependency,
    String bodyReport) throws MyException{        
        validateFields(reportNumber, gdeNumber, location, address, dependency, bodyReport);
        validateReportNumber(reportNumber);
        validateGDENumber(gdeNumber);
            
        Report report = new Report();
        Users user = (Users) session.getAttribute("currentUser");
        
        LocalDateTime dateNow = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");        
        
        report.setReportNumber(reportNumber);
        report.setGdeNumber(gdeNumber);
        report.setLocation(location);
        report.setAddress(address);
        report.setDependency(dependency);
        report.setBodyReport(bodyReport);
        report.setUsers(user);
        report.setDate(dateNow.format(formatter));
        report.setIsVisible(true);
        report.setIsExtension(false);

        reportRepository.save(report);
    }


    @Transactional
    public void deleteReport(Long id){
        reportRepository.findById(id).ifPresent( report -> {
            report.setIsVisible(false);
            reportRepository.save(report);
        });
    }


    public List<Report> getListReports(){        
        List<Report> listReports = reportRepository.findLastReports();
        
        return listReports;
    }


    public Report getOneReportById(Long id){
        return  reportRepository.findReportById(id);
    }


    public Report getOneReportBySearch(String numberReport) throws MyException{
        validateSearch(numberReport); 
        
        if(reportRepository.existenceReportNumber(numberReport)){
            return reportRepository.findReportByNumber(numberReport);
        }
        
        return reportRepository.findReportByAddress(numberReport);        
    }


    @Transactional
    public void editReport(
    Long id,
    String reportNumber,
    String gdeNumber, 
    String location,
    String address,
    String dependency,
    String bodyReport) throws MyException{
        validateFields(reportNumber, gdeNumber, location, address, dependency, bodyReport);
        validateUpdateReportNumber(reportNumber, id);
        validateUpdateGDENumber(gdeNumber, id);

        Users user = (Users) session.getAttribute("currentUser");  
        
        LocalDateTime dateNow = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        reportRepository.findById(id).ifPresent(report -> {            
            report.setReportNumber(reportNumber);
            report.setGdeNumber(gdeNumber);
            report.setLocation(location);
            report.setAddress(address);
            report.setDependency(dependency);
            report.setBodyReport(bodyReport);
            report.setUsers(user);
            report.setDate(dateNow.format(formatter));
            report.setIsExtension(true);
         
            reportRepository.save(report);
        });   
    }
    
    
    public void validateReportNumber(String numberReport) throws MyException{
        if(reportRepository.existenceReportNumber(numberReport)){
            throw new MyException("El número de denuncia ya existe");
        }
    }


    public void validateGDENumber(String gdeNumber) throws MyException{
        if(reportRepository.existenceGDENumber(gdeNumber)){
            throw new MyException("El número de G.D.E. ya existe");
        }
    }


    public void validateUpdateReportNumber(String numberReport, Long id) throws MyException{
        if(reportRepository.existenceUpdateReportNumber(numberReport, id)){
            throw new MyException("El número de denuncia ya existe");
        }
    }


    public void validateUpdateGDENumber(String gdeNumber, Long id) throws MyException{
        if(reportRepository.existenceUpdateGDENumber(gdeNumber, id)){
            throw new MyException("El número de G.D.E. ya existe");
        }
    }

    
    
    
    public void validateSearch(String numberReport) throws MyException{
        if(numberReport.isEmpty() || numberReport == null){
            throw new MyException("El campo de búsqueda no puede estar vacío");
        }

    }
    

    public void validateFields(
    String reportNumber,
    String gdeNumber,
    String location,
    String address,
    String dependency,
    String bodyReport) throws MyException{
        if(reportNumber.isEmpty() || reportNumber == null){
            throw new MyException("El campo número de denuncia no puede estar vacío");
        }       

        if(reportNumber.length() > 10){
            throw new MyException("El máximo es 10 caracteres para el campo número de denuncia");
        }

        if(!reportNumber.matches("-?\\d+")){
            throw new MyException("El valor ingresado en el campo numero de denuncia no es valido");
        } 

        if(gdeNumber.isEmpty() || gdeNumber == null){
            throw new MyException("El campo N° G.D.E. no puede estar vacío");
        }               

        if(gdeNumber.length() > 20){
            throw new MyException("El máximo es 20 caracteres para el campo N° G.D.E.");
        }

        if(!gdeNumber.matches("-?\\d+")){
            throw new MyException("El valor ingresado en el campo numero de gde no es valido");
        } 

        if(location.isEmpty() || location == null){
            throw new MyException("El campo localidad no puede estar vacío");
        }
      
        if(location.length() > 20){
            throw new MyException("El máximo es 20 caracteres para el campo localidad");
        }

        if(address.isEmpty() || address == null){
            throw new MyException("El campo dirección no puede estar vacío");
        }

        if(address.length() > 20){
            throw new MyException("El máximo es 20 caracteres para el campo dirección");
        }

        if(dependency.isEmpty() || dependency == null){
            throw new MyException("El campo dependencia no puede estar vacío");
        }      

        if(bodyReport.isEmpty() || bodyReport == null){
            throw new MyException("El campo reporte no puede estar vacío");
        }        

        if(bodyReport.length() > 2000){
            throw new MyException("El máximo es 2000 caracteres para el campo reporte");
        }

    }

    
}

