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
import com.comando.delta.service.HierarchyService;

@Controller
@RequestMapping("/hierarchy")
public class HierarchyController {

    @Autowired
    private HierarchyService hierarchyService;


    @GetMapping("/list")
    public String hierarchyList(ModelMap modelMap){
        modelMap.addAttribute("hierarchyList", hierarchyService.getHierarchyList());

        return "list_hierarchy";
    }


    @GetMapping("/register")
    public String hierarchyRegister(){

        return "create_hierarchy";
    }


    @PostMapping("/registered")
    public String hierarchyRegistered(
    @RequestParam String name,
    ModelMap modelMap) throws MyException{
        try {
            hierarchyService.createHierarchy(name);

            return "redirect:/hierarchy/list";            
        } catch (Exception e) {
            modelMap.addAttribute("error", e.getMessage());
            modelMap.addAttribute("name", name);

            return "create_hierarchy";
        }        
    }


    @GetMapping("/edit/{id}")
    public String editHierarchy(
    @PathVariable Long id,    
    ModelMap modelMap) throws MyException{
        modelMap.addAttribute("hierarchyEdit", hierarchyService.getOneHierarchy(id));

        return "edit_hierarchy.html";
    }


    @PostMapping("/edit/{id}")
    public String editedHierarchy(
    @PathVariable Long id,
    @RequestParam String name,
    ModelMap modelMap) throws MyException{
        try {
            hierarchyService.editHierarchy(id, name);

            return "redirect:/hierarchy/list";
        } catch (MyException e) {
            modelMap.addAttribute("error", e.getMessage());
            modelMap.addAttribute("name", name);

            return "edit_hierarchy.html";
        }

    }


    @PostMapping("/delete/{id}")
    public String deleteHierarchy(@PathVariable Long id){
        hierarchyService.deleteHierarchy(id);

        return "list_hierarchy";
    }
    
    
}
