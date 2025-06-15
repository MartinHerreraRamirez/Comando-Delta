package com.comando.delta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.comando.delta.enums.TypeEquipment;
import com.comando.delta.exception.MyException;
import com.comando.delta.service.DependencyService;
import com.comando.delta.service.EquipmentService;
import com.comando.delta.service.IndividualService;
import com.comando.delta.service.LoanService;

@Controller
@RequestMapping("/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private IndividualService individualService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private DependencyService dependencyService;


    @GetMapping("/list")
    public String listActivesLoan(ModelMap modelMap){
        modelMap.addAttribute("loanList", loanService.getActiveLoanList());
        modelMap.addAttribute("typeEquipmentList", TypeEquipment.values());

        return "list_loan.html";
    }
   

    @GetMapping("/load")
    public String loadLoanList(
    @RequestParam(required = false) String type, 
    @RequestParam(required = false) String status,
    ModelMap modelMap){
        modelMap.addAttribute("loanList", loanService.getLoanList(type, status));
        modelMap.addAttribute("status", status);
        
        modelMap.addAttribute("typeEquipmentList", TypeEquipment.values());

        return "list_loan.html";
    }   


    @GetMapping("/register")
    public String registerLoan(ModelMap modelMap){
        modelMap.addAttribute("typeEquipmentList", TypeEquipment.values());
        modelMap.addAttribute("dependencyList", dependencyService.getDependencyList());        

        return "create_loan.html";
    }


    @PostMapping("/registered")
    public String registerLoan(
    @RequestParam String personalFile,
    @RequestParam String numberEquipment,
    @RequestParam String contactNumber,
    @RequestParam(required = false) String comment) throws MyException{
        try {
            loanService.createLoan(personalFile, numberEquipment, contactNumber, comment);

            return "redirect:/loan/list";
        } catch (MyException e) {

            return "create_loan.html";
        }
    }


    @GetMapping("/search")
    public String searchIndividualAndEquipmentToLoan(
    @RequestParam String dependencyName, 
    @RequestParam String typeEquipment,
    ModelMap modelMap) throws MyException{
        try {
            modelMap.addAttribute("dependencyList", dependencyService.getDependencyList());
            modelMap.addAttribute("typeEquipmentList", TypeEquipment.values());
                        
            modelMap.addAttribute("individualList", individualService.getIndividualListByDependency(dependencyName));
            modelMap.addAttribute("equipmentList", equipmentService.getEquipmentListByFalseStatusisAvailable(typeEquipment));

            return "create_loan.html";
        } catch (MyException e) {
            modelMap.addAttribute("error", e.getMessage()); 
            modelMap.addAttribute("dependencyList", dependencyService.getDependencyList());
            modelMap.addAttribute("typeEquipmentList", TypeEquipment.values());

            modelMap.addAttribute("individualList", individualService.getIndividualListByDependency(dependencyName));
            modelMap.addAttribute("equipmentList", equipmentService.getEquipmentListByType(typeEquipment));
           
            return "create_loan.html";
        }        
    }


    @GetMapping("/search/number")
    public String searchActiveLoansByEquipmentNumber(
    @RequestParam String number,
    ModelMap modelMap) throws MyException{
        try {
            modelMap.addAttribute("loanList", loanService.getLoanListByEquipmentNumber(number));
            modelMap.addAttribute("typeEquipmentList", TypeEquipment.values());

            return "list_loan.html";
        } catch (MyException e) {
            modelMap.addAttribute("error", e.getMessage());
            modelMap.addAttribute("loanList", loanService.getActiveLoanList());
            modelMap.addAttribute("typeEquipmentList", TypeEquipment.values());

            return "list_loan.html";
        }
    }  


    @GetMapping("/finish/{id}")
    public String finishLoan(
    @PathVariable Long id,
    ModelMap modelMap) throws MyException {
        modelMap.addAttribute("finishLoan", loanService.getOneLoan(id));
        modelMap.addAttribute("individualList", individualService.getIndividualListByLoan(id));
        
        return "finish_loan.html";
    }


    @PostMapping("/finish/{id}")
    public String finishLoan(
    @PathVariable Long id,
    @RequestParam String personalFile,
    @RequestParam(required = false) String novelty,
    ModelMap modelMap){       
        loanService.finishLoan(id, personalFile, novelty);        

        return "redirect:/loan/list";       
    }
    
    
}
