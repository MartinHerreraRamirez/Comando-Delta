package com.comando.delta.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.comando.delta.enums.Rol;
import com.comando.delta.model.Hierarchy;
import com.comando.delta.model.Users;
import com.comando.delta.repository.IHierarchyRepository;
import com.comando.delta.repository.IUsersRepository;

@Component
public class UserComponent implements CommandLineRunner{

    @Autowired
    private IUsersRepository usersRepository;  
    
    @Autowired
    private IHierarchyRepository hierarchyRepository;

    @Override
    public void run(String... args) throws Exception{
        if(usersRepository.count() == 0){
            Hierarchy defaultHierarchy;
            List<Hierarchy> hierarchies = hierarchyRepository.findAll();
            if (!hierarchies.isEmpty()) {
                defaultHierarchy = hierarchies.get(0);
            } else {
                defaultHierarchy = new Hierarchy();
                defaultHierarchy.setName("SUPERADMIN");
                hierarchyRepository.save(defaultHierarchy);
            }
            
            Users superAdmin = new Users();
            superAdmin.setHierarchy(defaultHierarchy);
            superAdmin.setName("super");
            superAdmin.setLastName("admin");
            superAdmin.setOperatorNumber("0344");
            superAdmin.setPersonalFile("0344");
            superAdmin.setRol(Rol.SUPERADMIN);
            superAdmin.setEmail("email@email.com");
            superAdmin.setPhoneNumber("1234567890");
            superAdmin.setHierarchy(hierarchyRepository.findAll().get(0));
            superAdmin.setPassword(new BCryptPasswordEncoder().encode("byHerreraRamirezMartin"));
            superAdmin.setIsActive(true);
            usersRepository.save(superAdmin);
        }
    }
    
}
