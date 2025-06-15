package com.comando.delta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.comando.delta.exception.MyException;
import com.comando.delta.service.AutomobileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;




@Controller
@RequestMapping("/automobile")
public class AutomobileController {

    @Autowired
    private AutomobileService automobileService;

    @GetMapping("/list")
    public String getAutomobileList(ModelMap modelMap) {
        modelMap.addAttribute("automobileList", automobileService.getAutomobileList());
        return "list_automobile.html";
    }

    @GetMapping("/{id}")
    public String getAumobileById(@PathVariable Long id, ModelMap modelMap){
        modelMap.addAttribute("automobile", automobileService.getOneAutomobile(id));
        return "view_automobile.html";
    }

    @GetMapping("/search")
    public String getAutomobileSearched(String number, ModelMap modelMap) throws MyException{
        try {
            modelMap.addAttribute("automobileList", automobileService.getOneAutomobileByNumber(number));            
            return "list_automobile.html";
        } catch (MyException e) {
            modelMap.put("error", e.getMessage());
            return "list_automobile.html";
        }

    }

    @GetMapping("/register")
    public String automobileCreate() {
        return "create_automobile.html";
    }

    @PostMapping("/registered")
    public String automobileCreated(
    @RequestParam String number,
    @RequestParam String tradeMark,
    @RequestParam String model,
    @RequestParam String licensePlate,
    @RequestParam String color,
    @RequestParam String typeMobile,
    @RequestParam String status,
    @RequestParam MultipartFile image,
    ModelMap modelMap) throws MyException {
        try {
            automobileService.createMobile(number, tradeMark, model, licensePlate, color, typeMobile, status, image);
            
            return "redirect:/automobile/list";
        } catch (Exception e) {
            modelMap.addAttribute("number", number);
            modelMap.addAttribute("tradeMark", tradeMark);
            modelMap.addAttribute("model", model);
            modelMap.addAttribute("licensePlate", licensePlate);
            modelMap.addAttribute("color", color);
            modelMap.addAttribute("typeMobile", typeMobile);
            modelMap.addAttribute("status", status);
            modelMap.addAttribute("image", image);
            modelMap.addAttribute("error", e.getMessage());

            return "create_automobile.html";
        }        
    }

    @GetMapping("/edit/{id}")
    public String aumobileEdit(@PathVariable Long id, ModelMap modelMap){
        modelMap.addAttribute("automobile", automobileService.getOneAutomobile(id));
        return "edit_automobile.html";
    }

    @PostMapping("/edit/{id}")
    public String aumobileEdit(
    @RequestParam Long id,
    @RequestParam String number,
    @RequestParam String tradeMark,
    @RequestParam String model,
    @RequestParam String licensePlate,
    @RequestParam String color,
    @RequestParam String typeMobile,
    @RequestParam String status,
    @RequestParam MultipartFile image,
    ModelMap modelMap) throws Exception {
        try {
            automobileService.editMobile(id, number, tradeMark, model, licensePlate, color, typeMobile, status, image);

            return "redirect:/automobile/list";            
        } catch (Exception e) {
            modelMap.addAttribute("id", id);
            modelMap.addAttribute("number", number);
            modelMap.addAttribute("tradeMark", tradeMark);
            modelMap.addAttribute("model", model);
            modelMap.addAttribute("licensePlate", licensePlate);
            modelMap.addAttribute("color", color);
            modelMap.addAttribute("typeMobile", typeMobile);
            modelMap.addAttribute("status", status);
            modelMap.addAttribute("image", image);
            modelMap.addAttribute("error", e.getMessage());

            modelMap.addAttribute("error", e.getMessage());

            return "";
        }
    }   
    
}
