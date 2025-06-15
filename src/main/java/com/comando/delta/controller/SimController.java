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
import com.comando.delta.service.SimService;

@Controller
@RequestMapping("/sim")
public class SimController {

    @Autowired
    private SimService simService;
      
    @GetMapping("/list")
    public String listSims(ModelMap modelMap){        
        modelMap.addAttribute("simList", simService.getSimList());
        
        return "list_sim.html";
    }

    
    @GetMapping("/search")
    public String getSim(String number, ModelMap modelMap) throws MyException{
        try {
            modelMap.addAttribute("simList", simService.getOneSimByNumberOrSerialNumber(number));            
            return "list_sim.html";
            
        } catch (MyException e) {
            modelMap.addAttribute("error", e.getMessage());            
            modelMap.addAttribute("simList", simService.getSimList());            
            return "list_sim.html";            
        }
    }

    
    @GetMapping("/register")
    public String registerSim(){

        return "create_sim.html";
    }


    @PostMapping("/registered")
    public String registeredSim(
    @RequestParam String number,
    @RequestParam String serialNumber,
    @RequestParam String tradeMark,
    @RequestParam String plan,
    ModelMap modelMap){
        try {
            simService.createSim(number, serialNumber, tradeMark, plan);

            return "redirect:/sim/list";
        } catch (MyException e) {
            modelMap.put("error", e.getMessage());
            modelMap.put("number", number);
            modelMap.put("serialNumber", serialNumber);
            modelMap.put("tradeMark", tradeMark);
            modelMap.put("plan", plan);

            return "create_sim.html";
        }
    }    

    @GetMapping("/edit/{id}")
    public String editSim(
    @PathVariable Long id, 
    ModelMap modelMap){        
        modelMap.addAttribute("editSim", simService.getOneSimById(id));

        return "edit_sim.html";
    }

    @PostMapping("/edit/{id}")
    public String editSim(
    @PathVariable Long id,
    @RequestParam String number,
    @RequestParam String serialNumber,
    @RequestParam String tradeMark,
    @RequestParam String plan,
    ModelMap modelMap) throws MyException{
        try {
            simService.editSim(id, number, serialNumber, tradeMark, plan);

            return "redirect:../list";
        } catch (MyException e) {
            modelMap.put("error", e.getMessage());
            modelMap.addAttribute("editSim", simService.getOneSimById(id));          

            return "edit_sim.html";
        }        
    }
    

    @PostMapping("/delete/{id}")
    public String deleteSim(@PathVariable Long id){
        
        simService.deleteSim(id);
        return "redirect:/sim/list";
    }
   
    
}
