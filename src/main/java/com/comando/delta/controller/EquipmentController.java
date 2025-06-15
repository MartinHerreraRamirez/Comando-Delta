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
import com.comando.delta.service.EquipmentService;

@Controller
@RequestMapping("/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @GetMapping("/list")
    public String trunkingList(ModelMap modelMap){
        
        modelMap.addAttribute("equipmentList", equipmentService.getEquipmentList());
        modelMap.addAttribute("typeEquipmentList", TypeEquipment.values());

        return "list_equipment.html";
    }


    @GetMapping("/load")
    public String loadEquipment(@RequestParam String type, ModelMap modelMap) throws MyException{
        modelMap.addAttribute("equipmentList", equipmentService.getEquipmentListByType(type));
        modelMap.addAttribute("typeEquipmentList", TypeEquipment.values());

        return "list_equipment.html";
    }


    @GetMapping("/search")
    public String searchEquipment(String number, ModelMap modelMap) throws MyException {
        try {
            modelMap.addAttribute("equipmentList", equipmentService.getEquipmentByNumber(number));
            modelMap.addAttribute("typeEquipmentList", TypeEquipment.values());
    
            return "list_equipment.html";            
        } catch (MyException e) {
            modelMap.addAttribute("error", e.getMessage());
            modelMap.addAttribute("equipmentList", equipmentService.getEquipmentList());
            modelMap.addAttribute("typeEquipmentList", TypeEquipment.values());

            return "list_equipment.html";
        }
    }


    @GetMapping("/register")
    public String registerEquipment(ModelMap modelMap){
        modelMap.addAttribute("typeEquipmentList", TypeEquipment.values());

        return "create_equipment.html";
    }


    @PostMapping("/registered")
    public String registeredEquipment(
    @RequestParam String tradeMark,
    @RequestParam String model,
    @RequestParam String number,
    @RequestParam TypeEquipment typeEquipment,
    @RequestParam String observation,
    ModelMap modelMap
    ) throws MyException{
        try {            
            equipmentService.createEquipment(tradeMark, model, number, typeEquipment, observation);
            modelMap.put("success", "Equipo editado con exito");

            return "redirect:/equipment/list";
        } catch (MyException e) {
            modelMap.addAttribute("typeEquipmentList", TypeEquipment.values());
            modelMap.put("error", e.getMessage());
            modelMap.put("tradeMark", tradeMark);
            modelMap.put("model", model);
            modelMap.put("number", number);
            modelMap.put("typeEquipment", typeEquipment);
            modelMap.put("observation", observation);

            return "create_equipment.html";
        }
    }


    @GetMapping("/edit/{id}")
    public String editEquipment(@PathVariable Long id, ModelMap modelMap){
        modelMap.addAttribute("editEquipment", equipmentService.getOneEquipmentById(id));
        modelMap.addAttribute("typeEquipmentList", TypeEquipment.values());

        return "edit_equipment.html";
    }


    @PostMapping("/edit/{id}")
    public String editEquipment(
    @PathVariable Long id,
    @RequestParam String tradeMark,
    @RequestParam String model,
    @RequestParam String number,
    @RequestParam TypeEquipment typeEquipment,
    @RequestParam String observation,
    ModelMap modelMap) throws MyException{
        try {            
            equipmentService.editEquipment(id, tradeMark, model, number, typeEquipment, observation);

            return "redirect:/equipment/list";
        } catch (MyException e) {            
            modelMap.addAttribute("error", e.getMessage());         
            modelMap.addAttribute("typeEquipmentList", TypeEquipment.values());
            modelMap.addAttribute("editEquipment", equipmentService.getOneEquipmentById(id));    
    
            return "edit_equipment.html";            
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteEquipment(@PathVariable Long id){
        equipmentService.deleteEquipment(id);

        return "redirect:/equipment/list";
    }

   
    
}
