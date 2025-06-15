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
import com.comando.delta.service.DependencyService;
import com.comando.delta.service.HierarchyService;
import com.comando.delta.service.IndividualService;

@Controller
@RequestMapping("/individual")
public class IndividualController {

    @Autowired
    private IndividualService individualService;

    @Autowired
    private HierarchyService hierarchyService;

    @Autowired
    private DependencyService dependencyService;    
    

    @GetMapping("/list")
    public String listIndividual(ModelMap modelMap){
        modelMap.addAttribute("individualList", individualService.getIndividualList());

        return "list_individual";
    }


    @GetMapping("/search")
    public String searchIndividual(
    String personalFile, 
    ModelMap modelMap) throws MyException{
        try {
            modelMap.addAttribute("individualList", individualService.getOneIndividualByPersonalFile(personalFile));

            return "list_individual";
        } catch (MyException e) {
            modelMap.addAttribute("error", e.getMessage());
            modelMap.addAttribute("individualList", individualService.getIndividualList());

            return "list_individual";
        }
    }


    @GetMapping("/register")
    public String registerIndividual(ModelMap modelMap){
        modelMap.addAttribute("hierarchys", hierarchyService.getHierarchyList());
        modelMap.addAttribute("dependencys", dependencyService.getDependencyList());

        return "create_individual";
    }


    @PostMapping("/registered")
    public String registeredIndividual(
    @RequestParam String fullName,
    @RequestParam String personalFile,
    @RequestParam Long hierarchyId,
    @RequestParam Long dependencyId,
    ModelMap modelMap) throws MyException{
        try {
            individualService.createIndividual(fullName, personalFile, hierarchyId, dependencyId);

            return "redirect:/individual/list";
        } catch (MyException e) {
            modelMap.put("error", e.getMessage());
            modelMap.addAttribute("fullName", fullName);
            modelMap.addAttribute("personalFile", personalFile);            
            modelMap.addAttribute("hierarchys", hierarchyService.getHierarchyList());
            modelMap.addAttribute("dependencys", dependencyService.getDependencyList());

            return "create_individual";
        }

    }


    @GetMapping("/edit/{id}")
    public String editIndividual(
    @PathVariable Long id,
    ModelMap modelMap){       
        modelMap.addAttribute("individualEdit",  individualService.getOneIndividual(id));
        modelMap.addAttribute("hierarchys", hierarchyService.getHierarchyList());
        modelMap.addAttribute("dependencys", dependencyService.getDependencyList());

        return "edit_individual.html";
    }


    @PostMapping("/edit/{id}")
    public String editIndividual(
    @PathVariable Long id,
    @RequestParam String fullName,
    @RequestParam String personalFile,
    @RequestParam Long hierarchyId,
    @RequestParam Long dependencyId,
    ModelMap modelMap) throws MyException{
        try {
            individualService.editIndividual(id, fullName, personalFile, hierarchyId, dependencyId);

            return "redirect:/individual/list";
        } catch (MyException e) {
            modelMap.addAttribute("error", e.getMessage());
            modelMap.addAttribute("personalFile", personalFile);
            modelMap.addAttribute("hierarchys", hierarchyService.getHierarchyList());
            modelMap.addAttribute("dependencys", dependencyService.getDependencyList());

            return "edit_individual.html";
        }
    }


    @PostMapping("/delete/{id}")
    public String deleteIndividual(@PathVariable Long id){
        individualService.deleteIndividual(id);

        return "redirect:/individual/list";
    }
    
}
