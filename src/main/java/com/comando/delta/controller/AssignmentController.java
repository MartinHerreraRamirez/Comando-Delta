package com.comando.delta.controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.comando.delta.exception.MyException;
import com.comando.delta.model.AssignEquipment;
import com.comando.delta.model.AssignPerson;
import com.comando.delta.service.AssignEquipmentService;
import com.comando.delta.service.AssignmentService;
import com.comando.delta.service.AssingPersonService;
import com.comando.delta.service.DependencyService;
import com.comando.delta.service.EquipmentService;
import com.comando.delta.service.IndividualService;
import com.comando.delta.service.SimService;

@Controller
@RequestMapping("/assignment")
public class AssignmentController{

    @Autowired
    private AssignmentService assignmentService;

    @Autowired 
    private AssingPersonService assignPersonService;

    @Autowired
    private AssignEquipmentService assignEquipmentService;

    @Autowired
    private SimService simService;   

    @Autowired
    private DependencyService dependencyService;

    @Autowired
    private IndividualService individualService;

    @Autowired
    private EquipmentService equipmentService;
    
    @GetMapping("/list")
    public String activeAssignmentList(ModelMap modelMap){
        modelMap.addAttribute("status", "ACTIVE");
        modelMap.addAttribute("assignmentList", assignPersonService.getAssignPersonListByTrueStatus());
        
        return "list_assignment.html";
    }     

    @GetMapping("/load")
    public String activeAssignmentListByType(
    @RequestParam String type,
    @RequestParam(required = false) String status,
    ModelMap modelMap) throws MyException{
        try {
            if("INDIVIDUAL".equals(type)){
                modelMap.addAttribute("status", status);
                modelMap.addAttribute("assignmentList", assignPersonService.getAssignmentListByTypeAndStatus(type, status));
            } else {
                modelMap.addAttribute("status", status);
                modelMap.addAttribute("assignmentList", assignEquipmentService.getAssignEquipmentListByTypeAndStatus(type, status));
            }    
        } catch (MyException e) {
            modelMap.addAttribute("loadError", e.getMessage());
        }
        return "list_assignment.html";
    }   

    @GetMapping("/search/number")
    public String searchAssignmenByEquipmentNumberOrPhoneNumber(
    @RequestParam String number,
    ModelMap modelMap) throws MyException{
        try {
            Optional<AssignPerson> assignPerson = assignPersonService.getAssignPersonBySimNumberOrEquipNumber(number);

            if(assignPerson.isPresent()){
                modelMap.addAttribute("assigmentList", assignPerson.get());
            }

            Optional<AssignEquipment> assignEquipment = assignEquipmentService.getAssignEquipmentBySimNumberOrEquipNumber(number);

            if(assignEquipment.isPresent()){
                modelMap.addAttribute("assignmentList", assignEquipment.get());
            }

        } catch(MyException e){
            modelMap.addAttribute("error", e.getMessage());
        }
        return "list_assignment.html";
    }

    @GetMapping("/register")
    public String registerAssignment(ModelMap modelMap){
        modelMap.addAttribute("dependencyList", dependencyService.getDependencyList());
        return "create_assignment.html";
    }

    @GetMapping("/register/individual")
    public String registerAssignmentToIndividual(
    @RequestParam String dependencyName,
    ModelMap modelMap) throws MyException{
        try {
            modelMap.addAttribute("individualList", individualService.getIndividualListByDependency(dependencyName));
            modelMap.addAttribute("simList", simService.getActiveSimList());
            
        } catch (MyException e) {
            modelMap.addAttribute("error", e.getMessage());
            modelMap.addAttribute("individualList", individualService.getIndividualListByDependency(dependencyName));
            modelMap.addAttribute("simList", simService.getActiveSimList());        
        }
        return "create_assignment.html";
    }

    @GetMapping("/register/equipment")
    public String registerAssignmentToEquipment(
    @RequestParam String typeEquipment,
    ModelMap modelMap) throws MyException{
        try {
            modelMap.addAttribute("equipmentList", equipmentService.getEquipmentListByType(typeEquipment));
            modelMap.addAttribute("simList", simService.getActiveSimList());            
        } catch (MyException e) {
            modelMap.addAttribute("error", e.getMessage());
            modelMap.addAttribute("equipmentList", equipmentService.getEquipmentListByType(typeEquipment));
            modelMap.addAttribute("simList", simService.getActiveSimList());
        }
        return "create_assignment.html";
    }

    @PostMapping("/registered")
    public String registerAssignment(
    @RequestParam(required = false) String personalFile,
    @RequestParam(required = false) String numberEquipment,
    @RequestParam String simNumber,
    @RequestParam String contactNumber,
    @RequestParam(required = false) String comment,
    ModelMap modelMap) throws MyException {
        if(personalFile != null && !personalFile.isEmpty()){
            assignmentService.createAssignmentToIndividual(personalFile, simNumber, contactNumber, comment);
            return "redirect:/assignment/list";
        }

        if(numberEquipment != null && !numberEquipment.isEmpty()){
            assignmentService.createAssignmentToEquipment(numberEquipment, simNumber, contactNumber, comment);
            return "redirect:/assignment/list";
        }
        return null;        
    }

    @GetMapping("/finish/{id}")
    public String finishAssignment(
    @PathVariable Long id,
    ModelMap modelMap) throws MyException{        
        modelMap.addAttribute("finishAssignment", assignmentService.getOneAssignment(id));
        modelMap.addAttribute("individualList", individualService.getIndividualListByDependency(assignPersonService.getOneAssignPersonById(id).getAssignPerson().getDependency().getName()));

        return "finish_assignment";
    }

    @PostMapping("/finish/{id}")
    public String finishAssignment(
    @PathVariable Long id,
    @RequestParam String typeDTO,
    @RequestParam(required = false) String personalFile,
    @RequestParam String noventy) throws MyException{
        assignmentService.finishAssignment(id, typeDTO, personalFile, noventy);

        return "redirect:/assignment/list";
    }
    
}
