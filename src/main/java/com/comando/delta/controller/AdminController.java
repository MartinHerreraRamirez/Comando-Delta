package com.comando.delta.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.comando.delta.enums.Rol;
import com.comando.delta.exception.MyException;
import com.comando.delta.model.Users;
import com.comando.delta.service.HierarchyService;
import com.comando.delta.service.UsersService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private HierarchyService hierarchyService;

    @GetMapping("/dashboard")
    public String dashboard(ModelMap modelMap, HttpSession session) {
        Users user = (Users) session.getAttribute("currentUser");

        modelMap.addAttribute("userList", usersService.getListUsers());
        modelMap.addAttribute("currentUser", user);

        return "dashboard.html";
    }

    @GetMapping("/user/create")
    public String createUser(ModelMap modelMap) {
        modelMap.addAttribute("hierarchys", hierarchyService.getHierarchyList());
        modelMap.addAttribute("rols", Rol.values());                                                     

        return "admin_form_create_user.html";    
    }

    @PostMapping("/user/registered")
    public String registered(
    @RequestParam String name,
    @RequestParam String lastName,
    @RequestParam String personalFile,
    @RequestParam String operatorNumber,
    @RequestParam Long hierarchyId,
    @RequestParam String rol,
    @RequestParam String email,
    @RequestParam String phone,
    @RequestParam String password,
    @RequestParam String password2,
    ModelMap modelMap) throws MyException {
        try {
            usersService.createUser(name, lastName, personalFile, operatorNumber, hierarchyId, rol, email, phone, password, password2);

            return "redirect:/admin/dashboard";
        } catch (MyException e) {
            modelMap.put("error", e.getMessage());
            modelMap.put("name", name);
            modelMap.put("lastName", lastName);
            modelMap.put("operatorNumber", operatorNumber);
            modelMap.put("personalFile", personalFile);
            modelMap.put("email", email);
            modelMap.put("phone", phone);
            modelMap.addAttribute("hierarchys", hierarchyService.getHierarchyList());
            modelMap.addAttribute("rols", Rol.values());

            return "admin_form_create_user.html";
        }
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        usersService.deleteUser(id);

        return "redirect:../../dashboard";
    }

    @GetMapping("/user/edit/{id}")
    public String editUser(@PathVariable Long id, ModelMap modelMap) {
        modelMap.addAttribute("user", usersService.getOneUser(id));
        modelMap.addAttribute("hierarchys", hierarchyService.getHierarchyList());
        modelMap.addAttribute("rols", Rol.values());                    

        return "admin_form_edit_user.html";
    }

    @PostMapping("/user/edit/{id}")
    public String editUser(
    @RequestParam Long id,
    @RequestParam String name,
    @RequestParam String lastName,
    @RequestParam String operatorNumber,
    @RequestParam String personalFile,
    @RequestParam String email,
    @RequestParam String phoneNumber,
    @RequestParam Long hierarchyId,
    @RequestParam String rol,
    ModelMap modelMap) throws MyException {
        try {
            usersService.editUser(id, name, lastName, personalFile, operatorNumber, email, phoneNumber, hierarchyId);

            return "redirect:/admin/dashboard";
        } catch (MyException e) {
            modelMap.put("error", e.getMessage());
            modelMap.addAttribute("user", usersService.getOneUser(id));
            modelMap.addAttribute("hierarchys", hierarchyService.getHierarchyList());
            modelMap.addAttribute("rols", Rol.values());

            return "admin_form_edit_user.html";
        }
    }

    @GetMapping("/search")
    public String search(String personalFile, ModelMap modelMap, HttpSession session) throws MyException {
        try {
            Users user = (Users) session.getAttribute("currentUser");
            modelMap.addAttribute("currentUser", user);
            
            modelMap.addAttribute("userList", usersService.findUserByPersonalFile(personalFile));

            return "dashboard.html";
        } catch (MyException e) {
            Users user = (Users) session.getAttribute("currentUser");
            modelMap.addAttribute("currentUser", user);

            modelMap.put("error", e.getMessage());
            modelMap.addAttribute("userList", usersService.getListUsers());

            return "dashboard.html";
        }
    }

}
