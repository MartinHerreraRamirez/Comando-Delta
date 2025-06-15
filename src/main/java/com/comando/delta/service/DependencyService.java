package com.comando.delta.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comando.delta.exception.MyException;
import com.comando.delta.model.Dependency;
import com.comando.delta.repository.IDependencyRepository;
import com.comando.delta.repository.IIndividualRepository;

@Service
public class DependencyService {
    
    @Autowired
    private IDependencyRepository dependencyRepository;

    @Autowired IIndividualRepository individualRepository;

    @Transactional
    public void createDependency(
    String code,
    String name) throws MyException{
        validateFields(code, name);

        Dependency dependency = new Dependency();
        dependency.setCode(code);
        dependency.setName(name);

        dependencyRepository.save(dependency);
    }

    @Transactional
    public void editDependency(
    Long id,
    String code,
    String name) throws MyException{
        validateFields(code,  name);

        dependencyRepository.findById(id).ifPresent( dependency -> {
            dependency.setCode(code);
            dependency.setName(name);

            dependencyRepository.save(dependency);
        });
    }

    @Transactional
    public void deleteDependency(Long id) throws MyException{
        validateUseDependency(id);
        dependencyRepository.deleteById(id);
    }

    public List<Dependency> getDependencyList(){
        return dependencyRepository.findDependencyList();
    }

    public Dependency getOneDependency(Long id){
        return dependencyRepository.findById(id).get();
    }

    public void validateFields(
    String code,
    String name) throws MyException{
        if(name.isEmpty() || name == null){
            throw new MyException("El campo nombre no puede estar vacio");         
        }

        if(name.length() > 30){
            throw new MyException("El maximo es 30 caracteres para el campo nombre");
        }

        if(!code.matches("-?\\d+")){
            throw new MyException("El valor ingresado en el campo codigo no es valido");
        } 
        
        if(code.isEmpty() || code == null){
            throw new MyException("El campo codigo no puede estar vacio");         
        }

        if(code.length() > 5){
            throw new MyException("El maximo es 5 caracteres para el campo codigo");
        }            
    }

    public void validateUseDependency(Long id) throws MyException{
        if(individualRepository.isDependencyInUse(id)){
            throw new MyException("no se puede eliminar la dependencia debido a que esta en uso en otras tablas");
        }
    }   


}
