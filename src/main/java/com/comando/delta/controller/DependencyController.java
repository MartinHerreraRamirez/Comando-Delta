package com.comando.delta.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.comando.delta.exception.MyException;
import com.comando.delta.service.DependencyService;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/dependency")
public class DependencyController {

    @Autowired
    private DependencyService dependencyService;

    @GetMapping("/list")
    public String dependencyList(ModelMap modelMap){
        modelMap.addAttribute("dependencyList", dependencyService.getDependencyList());

        return "list_dependency";
    }


    @GetMapping("/register")
    public String registerDependency(){
        return "create_dependency.html";
    }

    @PostMapping("/registered")
    public String registerDependency(
    @RequestParam String code,
    @RequestParam String name,
    ModelMap modelMap) throws MyException {
       try {
            dependencyService.createDependency(code, name);

            return "redirect:/dependency/list";
       } catch (MyException e) {
            modelMap.addAttribute("error", e.getMessage());
            modelMap.addAttribute("code", code);
            modelMap.addAttribute("name", name);

            return "create_dependency.html";
       }
    }


    @GetMapping("/edit/{id}")
    public String editDependency(@PathVariable Long id, ModelMap modelMap){
        modelMap.addAttribute("dependencyEdit", dependencyService.getOneDependency(id));

        return "edit_dependency";
    }


    @PostMapping("/edit/{id}")
    public String editDependency(
    @PathVariable Long id,
    @RequestParam String code,
    @RequestParam String name,
    ModelMap modelMap) throws MyException{
        try {
            dependencyService.editDependency(id, code,  name);

            return "redirect:/dependency/list";
        } catch (MyException e) {
            modelMap.addAttribute("error", e.getMessage());
            modelMap.addAttribute("code", code);
            modelMap.addAttribute("name", name);

            return "edit_dependency";
        }
    }


    @PostMapping("/delete/{id}")
    public String deleteDependency(
        @PathVariable Long id,
        ModelMap modelMap) throws MyException{
        try {
            dependencyService.deleteDependency(id);

            return "redirect:/dependency/list";
        } catch (MyException e) {
            modelMap.addAttribute("error", e.getMessage());
            modelMap.addAttribute("dependencyList", dependencyService.getDependencyList());

            return "list_dependency";
        }
    }




}
