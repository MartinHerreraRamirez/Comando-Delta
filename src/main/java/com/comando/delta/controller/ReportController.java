package com.comando.delta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.comando.delta.exception.MyException;
import com.comando.delta.service.ReportService;

@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/list")
    public String listReport(ModelMap modelMap){
        modelMap.addAttribute("reportList", reportService.getListReports());

        return "list_report.html";
    }  
    
    @GetMapping("/search")
    public String search(String reportNumber, ModelMap modelMap) throws Exception{
        try {
            modelMap.addAttribute("reportList", reportService.getOneReportBySearch(reportNumber));         
    
            return "list_report.html";            
        } catch (Exception e) {
            modelMap.addAttribute("error", e.getMessage());
            modelMap.addAttribute("reportList", reportService.getListReports());         
    
            return "list_report.html";            
        }
    }
      

    @GetMapping("/register")
    public String createReport(){

        return "create_report.html";
    }

    @PostMapping("/registered")
    public String created(
    @RequestParam String reportNumber,
    @RequestParam String gdeNumber,
    @RequestParam String location,
    @RequestParam String address,
    @RequestParam String dependency, //? luego sera un select
    @RequestParam String bodyReport,    
    ModelMap modelMap){
        try {            
            reportService.createReport(reportNumber, gdeNumber, location, address, dependency, bodyReport);
            modelMap.put("success", "denuncia cargada exitosamente");

            return "redirect:/report/list";
        } catch (MyException e) {
            modelMap.put("error", e.getMessage() );
            modelMap.put("reportNumber", reportNumber);
            modelMap.put("gdeNumber", gdeNumber);
            modelMap.put("location", location);
            modelMap.put("address", address);
            modelMap.put("dependency", dependency);
            modelMap.put("bodyReport", bodyReport);
            
            return "create_report.html";
        }                
    }

    @GetMapping("/edit/{id}")
    public String editReport(@PathVariable Long id, ModelMap modelMap){
        modelMap.addAttribute("editReport", reportService.getOneReportById(id));

        return "edit_report.html";
    }

    @PostMapping("/edit/{id}")
    public String editReport(
    @PathVariable Long id,
    @RequestParam String reportNumber,
    @RequestParam String gdeNumber,
    @RequestParam String location,
    @RequestParam String address,
    @RequestParam String dependency,
    @RequestParam String bodyReport,
    ModelMap modelMap) throws MyException{
        try {
            reportService.editReport(id, reportNumber, gdeNumber, location, address, dependency, bodyReport);

            return "redirect:../list";            
        } catch (MyException e) {            
            modelMap.put("error", e.getMessage());
            modelMap.addAttribute("editReport", reportService.getOneReportById(id));            

            return "edit_report.html";
        }
    }

    @GetMapping("/view/{id}")
    public String viewReport(@PathVariable Long id, ModelMap modelMap){
        modelMap.addAttribute("report", reportService.getOneReportById(id));

        return "view_report.html";
    }

    @GetMapping("/delete/{id}")
    public String deleteReport(@PathVariable Long id){
        reportService.deleteReport(id);

        return "redirect:../list";
    }
    
    
    
    
}
