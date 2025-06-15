package com.comando.delta.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.comando.delta.model.Hierarchy;
import com.comando.delta.enums.Rol;
import com.comando.delta.exception.MyException;
import com.comando.delta.model.Users;
import com.comando.delta.repository.IHierarchyRepository;
import com.comando.delta.repository.IUsersRepository;

@Service
public class UsersService implements UserDetailsService {

    @Autowired
    private IUsersRepository usersRepository;   

    @Autowired
    private IHierarchyRepository hierarchyRepository;  


    @Transactional
    public void createUser(
    String name, 
    String lastName, 
    String personalFile, 
    String operatorNumber, 
    Long hierarchyId,
    String rol,
    String email,
    String phoneNumber,
    String password,
    String password2) throws MyException{
        validate(name, lastName, personalFile, operatorNumber, hierarchyId, rol, email, phoneNumber, password, password2);
        validateOperatorNumber(operatorNumber);
        validateEmail(email);
        validatePhoneNumber(phoneNumber);
        validateHerarchyAndPersonalFile(hierarchyRepository.findById(hierarchyId).get(), personalFile);

        Users users = new Users();

        users.setName(name);
        users.setLastName(lastName);
        users.setPersonalFile(personalFile);
        users.setOperatorNumber(operatorNumber);
        users.setHierarchy(hierarchyRepository.findById(hierarchyId).get());
        users.setEmail(email);
        users.setPhoneNumber(phoneNumber);        
        users.setIsActive(false);
        users.setRol(Rol.valueOf(rol));
        users.setPassword(new BCryptPasswordEncoder().encode(password));        

        usersRepository.save(users);
    }


    @Transactional
    public void deleteUser(Long id){
        usersRepository.findById(id).ifPresent( users -> {
            users.setIsActive(!users.getIsActive());
            usersRepository.save(users);
        });     
    }

    public Users getOneUser(Long id){
        return usersRepository.findById(id).get();      
    }

    public Users findUserByPersonalFile(String personalFile) throws MyException{
        validateSearch(personalFile);
        
        return usersRepository.findUserByPersonalFile(personalFile);
    }


    public List<Users> getListUsers(){        
        return usersRepository.findUsersList();
    }

    @Transactional
    public void editUser(
    Long id,
    String name, 
    String lastName, 
    String personalFile, 
    String operatorNumber, 
    String email,
    String phoneNumber,
    Long hierarchyId) throws MyException{
        validate(name, lastName, personalFile, operatorNumber, hierarchyId, email, phoneNumber);
        validateOperatorNumber(operatorNumber, id);
        validateEmail(email, id);
        validatePhoneNumber(phoneNumber, id);

        usersRepository.findById(id).ifPresent( users -> {
            users.setName(name);
            users.setLastName(lastName);
            users.setPersonalFile(personalFile);
            users.setOperatorNumber(operatorNumber);
            users.setHierarchy(hierarchyRepository.findById(hierarchyId).get());
            users.setEmail(email);
            users.setPhoneNumber(phoneNumber);
            
            usersRepository.save(users);
        });       
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user = username.contains("@") 
        ? usersRepository.findUserByEmail(username)
        : usersRepository.findUserByOperatorNumber(username);        
       
        if(user != null && user.getIsActive() == true){

            List<GrantedAuthority> permit = new ArrayList<>();

            permit.add(new SimpleGrantedAuthority("ROLE_" + user.getRol().toString()));

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attributes.getRequest().getSession(true);

            session.setAttribute("currentUser", user);

            return new User(user.getEmail(), user.getPassword(), permit);

        }else{
            return null;
        }
    } 
    
    
    private void validateSearch(String personaFile) throws MyException{
        if(personaFile.isEmpty() || personaFile == null){
            throw new MyException("El campo de búsqueda no puede estar vacío");
        }
    }


    public void validateEmail(String email) throws MyException{
        if(usersRepository.existenceUserByEmail(email)){
            throw new MyException("El email ya existe");
        }
    }

    public void validateEmail(String email, Long id) throws MyException{
        if(usersRepository.existenceUpdateUserByEmail(email, id)){
            throw new MyException("El email ya existe");
        }
    }

    public void validateOperatorNumber(String operatorNumber) throws MyException{
        if(usersRepository.existenceUserByOperatorNumber(operatorNumber)){
            throw new MyException("El número de operador ya existe");
        }
    }

    public void validateOperatorNumber(String operatorNumber, Long id) throws MyException{
        if(usersRepository.existenceUpdateUserByOperatorNumber(operatorNumber, id)){
            throw new MyException("El número de operador ya existe");
        }
    }

    public void validatePhoneNumber(String phoneNumber) throws MyException{
        if(usersRepository.existenceUserByPhoneNumber(phoneNumber)){
            throw new MyException("El número de telefono ya existe");
        }
    }

    public void validatePhoneNumber(String phoneNumber, Long id) throws MyException{
        if(usersRepository.existenceUpdateUserByPhoneNumber(phoneNumber, id)){
            throw new MyException("El número de telefono ya existe");
        }
    }

    public void validateHerarchyAndPersonalFile(Hierarchy hierarchy, String personalFile) throws MyException{
        if(usersRepository.existenceUserByHierarchyAndPersonalFile( hierarchy, personalFile)){
            throw new MyException("Ya existe un usuario con esa jerarquia y legajo personal");
        }
    }

    public void validateHerarchyAndPersonalFile(Hierarchy hierarchy, String personalFile, Long id) throws MyException{
        if(usersRepository.existenceUpdateUserByHierarchyAndPersonalFile(hierarchy, personalFile, id)){
            throw new MyException("Ya existe un usuario con esa jerarquia y legajo personal");
        }
    }


    private void validate(
    String name, 
    String lastName, 
    String personalFile, 
    String operatorNumber, 
    Long hierarchyId,
    String rol,
    String email,
    String phoneNumber,       
    String password,
    String password2) throws MyException{
        if(name.isEmpty() || name == null){
            throw new MyException("El campo nombre no puede estar vacío");
        }

        if(name.length() > 50){
            throw new MyException("El máximo es 50 caracteres para el campo nombre");
        }

        if(lastName.isEmpty() || lastName == null){
            throw new MyException("El campo apellido no puede estar vacío");
        }

        if(lastName.length() > 50){
            throw new MyException("El máximo es 50 caracteres para el campo apellido");
        }
        
        if(personalFile.isEmpty() || personalFile == null){
            throw new MyException("El campo número de legajo personal no puede estar vacío");
        }

        if(personalFile.length() > 5){
            throw new MyException("El máximo es 5 caracteres para el campo legajo personal");
        }

        if(!personalFile.matches("-?\\d+")){
            throw new MyException("El valor ingresado en el campo legajo personal no es valido");
        } 

        if(operatorNumber.isEmpty() || operatorNumber == null){
            throw new MyException("El campo número de operador no puede estar vacío");
        }

        if(operatorNumber.length() > 5){
            throw new MyException("El máximo es 5 caracteres para el campo número de operador");
        }

        if(!operatorNumber.matches("-?\\d+")){
            throw new MyException("El valor ingresado en el campo numero de operador no es valido");
        } 

        if(hierarchyId == null){
            throw new MyException("El campo jerarquia no puede estar vacío");
        } 

        if(rol == null){
            throw new MyException("El campo rol no puede estar vacio");
        }

        if(email.isEmpty() || email == null){
            throw new MyException("El campo email no puede estar vacío");
        }

        if(!email.contains("@")){
            throw new MyException("El email ingresado no posee un formato valido");
        }

        if(email.length() < 5){
            throw new MyException("El mínimo es 5 caracteres para el campo email");
        }

        if(email.length() > 254){
            throw new MyException("El máximo es 254 caracteres para el campo email");
        }

        if(phoneNumber.isEmpty() || phoneNumber == null){
            throw new MyException("El campo número de teléfono no puede estar vacío");
        }

        if(phoneNumber.length() > 15){
            throw new MyException("El máximo es 15 de caracteres para el campo teléfono");
        }

        if(!phoneNumber.matches("-?\\d+")){
            throw new MyException("El valor ingresado en el campo numero de telefono no es valido");
        } 

        if(password.isEmpty() || password == null){
            throw new MyException("El campo contraseña no puede estar vacío");
        }

        if(password2.isEmpty() || password2 == null){
            throw new MyException("El campo confimación de contraseña no puede estar vacío");
        }

        if(password.length() < 8 || password2.length() < 8){
            throw new MyException("El mínimo es 8 caracteres  para el campo contraseña");
        }

        if(password.length() > 64 || password2.length() > 64){
            throw new MyException("El máximo es 64 caracteres para el campo contraseña");
        }

        if(!password.equals(password2)){
            throw new MyException("Los campos de contraseña no coinciden");
        }        
    }

    private void validate(
    String name, 
    String lastName, 
    String personalFile, 
    String operatorNumber, 
    Long hierarchyId,
    String email,
    String phoneNumber) throws MyException{
        if(name.isEmpty() || name == null){
            throw new MyException("El campo nombre no puede estar vacío");
        }

        if(name.length() > 50){
            throw new MyException("El máximo es 50 caracteres  para el campo nombre");
        }

        if(lastName.isEmpty() || lastName == null){
            throw new MyException("El campo apellido no puede estar vacío");
        }

        if(lastName.length() > 50){
            throw new MyException("El máximo es 50 caracteres para el campo apellido");
        }
        
        if(hierarchyId == null){
            throw new MyException("El campo jerarquia no puede ser vacío");
        }

        if(operatorNumber.isEmpty() || operatorNumber == null){
            throw new MyException("El campo número de operador no puede estar vacío");
        }

        if(operatorNumber.length() > 20){
            throw new MyException("El máximo es 20 caracteres para el campo número de operador");
        }

        if(!operatorNumber.matches("-?\\d+")){
            throw new MyException("El valor ingresado en el campo numero de telefono no es valido");
        } 

        if(personalFile.isEmpty() || personalFile == null){
            throw new MyException("El campo número de legajo personal no puede estar vacío");
        }

        if(personalFile.length() > 15){
            throw new MyException("El máximo es 15 caracteres para el campo legajo personal");
        }

        if(!personalFile.matches("-?\\d+")){
            throw new MyException("El valor ingresado en el campo numero de telefono no es valido");
        } 

        if(email.isEmpty() || email == null){
            throw new MyException("El campo email no puede estar vacío");
        }

        if(!email.contains("@")){
            throw new MyException("El email ingresado no posee un formato valido");
        }        

        if(email.length() > 254){
            throw new MyException("El máximo es 254 caracteres para el campo email");
        }

        if(phoneNumber.isEmpty() || phoneNumber == null){
            throw new MyException("El número de teléfono no puede estar vacío");
        }        

        if(phoneNumber.length() > 15){
            throw new MyException("El máximo es 15 caracteres para el campo teléfono");
        }       
        
        if(!phoneNumber.matches("-?\\d+")){
            throw new MyException("El valor ingresado en el campo numero de telefono no es valido");
        } 
    }
    
    
}
