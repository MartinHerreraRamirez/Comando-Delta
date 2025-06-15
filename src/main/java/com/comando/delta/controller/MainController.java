package com.comando.delta.controller;

import javax.servlet.http.HttpSession;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.comando.delta.model.Users;

@Controller
@RequestMapping("/")
public class MainController {    

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_ADMIN', 'ROLE_ADMINISTRATIVO', 'ROLE_BRIGADA', 'ROLE_TECNICA', 'ROLE_REBO', 'ROLE_COMANDO')")
    @GetMapping("/")
    public String index(HttpSession session){
        Users user = (Users) session.getAttribute("currentUser");

        if(user.getRol().toString().equals("ADMIN") || user.getRol().toString().equals("SUPERADMIN")){
            return "redirect:/admin/dashboard";
        }

        return "index.html";
    } 

    @GetMapping("/login")
    public String login(
    @RequestParam(required = false) String error,
    ModelMap modelMap){
        if(error != null){
            modelMap.put("error", "credenciales incorrectas");            
        }

        return "login.html";
    }   
    
}
