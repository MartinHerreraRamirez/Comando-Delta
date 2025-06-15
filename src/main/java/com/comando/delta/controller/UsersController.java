// package com.comando.delta.controller;

// import javax.servlet.http.HttpSession;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.ModelMap;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;

// import com.comando.delta.exception.MyException;
// import com.comando.delta.service.HierarchyService;
// import com.comando.delta.service.UsersService;

// @Controller
// @RequestMapping("/user")
// public class UsersController {

//     @Autowired
//     private UsersService usersService;

//     @Autowired
//     private HierarchyService hierarchyService;

//     @GetMapping("/register")
//     public String register(ModelMap modelMap){
//         modelMap.addAttribute("hierarchys", hierarchyService.getHierarchyList());

//         return "create_user.html";
//     }

//     @PostMapping("/registered")
//     public String registered(    
//     @RequestParam String name,
//     @RequestParam String lastName,
//     @RequestParam String personalFile,
//     @RequestParam String operatorNumber,
//     @RequestParam Long hierarchyId,
//     @RequestParam String rol,
//     @RequestParam String email,
//     @RequestParam String phone,
//     @RequestParam String password,
//     @RequestParam String password2,
//     ModelMap modelMap,
//     HttpSession session){
//         try {
//             usersService.createUser(name, lastName,  personalFile, operatorNumber, hierarchyId, rol, email, phone, password, password2);
//             modelMap.put("success", "Usuario creado con exito");

//             return "login.html";     
//         } catch (MyException e) { 
//             modelMap.put("error", e.getMessage());
//             modelMap.put("name", name);
//             modelMap.put("lastName", lastName);
//             modelMap.put("operatorNumber", operatorNumber);
//             modelMap.put("personalFile", personalFile);
//             modelMap.put("email", email);
//             modelMap.put("phone", phone);
//             modelMap.addAttribute("hierarchys", hierarchyService.getHierarchyList());
            
//             return "create_user.html";
//         }        
//     }
    

// }
