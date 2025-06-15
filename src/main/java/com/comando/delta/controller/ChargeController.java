package com.comando.delta.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.comando.delta.enums.Rol;
import com.comando.delta.enums.TypeCharge;
import com.comando.delta.exception.MyException;
import com.comando.delta.model.Users;
import com.comando.delta.service.ChargeService;

@Controller
@RequestMapping("/charge")
public class ChargeController {

    @Autowired
    private ChargeService chargeService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPERADMIN','ROLE_ADMINISTRATIVO','ROLE_BRIGADA','ROLE_TECNICA')")
    @GetMapping("/list")
    public String getChargeList(ModelMap modelMap, HttpSession session){
        Users user = (Users) session.getAttribute("currentUser");
        Rol rol = user.getRol();
        
        if(rol == Rol.BRIGADA){
            modelMap.addAttribute("chargeList", chargeService.getChargeListByBrigadeType());
            return "list_charge.html";
        }

        if(rol == Rol.TECNICA){
            modelMap.addAttribute("chargeList", chargeService.getChargeListByTechnicalType());
            return "list_charge.html";
        }

        if(rol == Rol.ADMINISTRATIVO){
            modelMap.addAttribute("chargeList", chargeService.getChargeListByTechnicalType());
            return "list_charge.html";
        }

        modelMap.addAttribute("chargeList", chargeService.getChargeList());        
        return "list_charge.html";        
    } 



    @GetMapping("/search")
    public String searchCharge(String file, ModelMap modelMap) throws MyException{        
        try {
            modelMap.addAttribute("chargeList", chargeService.getOneChargeByNumberFile(file));
            
            return "list_charge.html";
        } catch (MyException e) {
            modelMap.put("error", e.getMessage());
            modelMap.addAttribute("chargeList", chargeService.getChargeList());

            return "list_charge.html";
        }
    }


    @GetMapping("/register")
    public String registerCharge(ModelMap modelMap){
        modelMap.addAttribute("typeCharge", TypeCharge.values());

        return "create_charge.html";
    }


    @PostMapping("/registered")
    public String registeredCharge( 
    @RequestParam String number,
    @RequestParam String file,
    @RequestParam String departure,
    @RequestParam String opening,
    @RequestParam String description,
    @RequestParam String dateAdmission,
    @RequestParam String dateCharge,
    @RequestParam String expedient,
    @RequestParam String serialNumber,
    @RequestParam String price,
    @RequestParam String quantity,
    @RequestParam String place,
    @RequestParam String typeCharge,
    @RequestParam MultipartFile image,
    ModelMap modelMap) throws MyException{
        try {
            chargeService.createCharge(number, file, departure, opening, description, dateAdmission, dateCharge, expedient, serialNumber, price, quantity, place, typeCharge, image);

            return "redirect:/charge/list";
        } catch (Exception e) {
            modelMap.put("error", e.getMessage());
            modelMap.put("number", number);
            modelMap.put("file", file);
            modelMap.put("departure", departure);
            modelMap.put("opening", opening);
            modelMap.put("description", description);
            modelMap.put("description", description);
            modelMap.put("dateAdmission", dateAdmission);
            modelMap.put("dateCharge", dateCharge);
            modelMap.put("expedient", expedient);
            modelMap.put("serialNumber", serialNumber);
            modelMap.put("price", price);
            modelMap.put("quantity", quantity);
            modelMap.put("place", place);            
            
            return "create_charge.html";
        }

    }


    @GetMapping("/see/{id}")
    public String seeCharge(@PathVariable Long id, ModelMap modelMap){
        modelMap.addAttribute("charge", chargeService.getOneChargeById(id));

        return "view_charge";
    }


    @GetMapping("/edit/{id}")
    public String editCharge(@PathVariable Long id, ModelMap modelMap){
        modelMap.addAttribute("editCharge", chargeService.getOneChargeById(id));
        modelMap.addAttribute("typeCharge", TypeCharge.values());

        return "edit_charge";
    }


    @PostMapping("/edit/{id}")
    public String editCharge(
    @PathVariable Long id,
    @RequestParam String number,
    @RequestParam String file,
    @RequestParam String departure,
    @RequestParam String opening,
    @RequestParam String description,
    @RequestParam String dateAdmission,
    @RequestParam String dateCharge,
    @RequestParam String expedient,
    @RequestParam String serialNumber,
    @RequestParam String price,
    @RequestParam String quantity,
    @RequestParam String place,
    @RequestParam String typeCharge,
    @RequestParam(required = false) MultipartFile image,
    ModelMap modelMap) throws Exception {
        try {
            chargeService.editCharge(id, number, file, departure, opening, description, dateAdmission, dateCharge, expedient, serialNumber, price, quantity, place, typeCharge, image);

            return "redirect:/charge/list";
        } catch (MyException e) {
            modelMap.addAttribute("error", e.getMessage());
            modelMap.addAttribute("editCharge", chargeService.getOneChargeById(id));

            return "edit_charge.html";
        }
    }


    @PostMapping("/delete/{id}")
    public String deleteCharge(@PathVariable Long id){
        chargeService.deleteCharge(id);

        return "redirect:/charge/list";
    }
    
}
