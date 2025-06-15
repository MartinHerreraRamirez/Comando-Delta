package com.comando.delta.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comando.delta.exception.MyException;
import com.comando.delta.model.Individual;
import com.comando.delta.repository.IDependencyRepository;
import com.comando.delta.repository.IHierarchyRepository;
import com.comando.delta.repository.IIndividualRepository;
import com.comando.delta.repository.ILoanRepository;

@Service
public class IndividualService {

    @Autowired
    private IIndividualRepository individualRepository;

    @Autowired 
    private IHierarchyRepository hierarchyRepository;

    @Autowired
    private IDependencyRepository dependencyRepository;

    @Autowired
    private ILoanRepository loanRepository;

    @Transactional
    public void createIndividual(
    String fullName,
    String personalFile,
    long hierarchyId,
    long dependencyId) throws MyException{
        validateFields(fullName, personalFile, hierarchyId, dependencyId);
        Individual individual = new Individual();

        individual.setFullName(fullName);
        individual.setPersonalFile(personalFile);
        individual.setHierarchy(hierarchyRepository.findById(hierarchyId).get());
        individual.setDependency(dependencyRepository.findById(dependencyId).get());

        individualRepository.save(individual);
    }

    @Transactional
    public void editIndividual(
    Long id,
    String fullName,
    String personalFile,
    Long hierarchyId,
    Long dependencyId) throws MyException{
        validateFields(fullName, personalFile, hierarchyId, dependencyId);
        individualRepository.findById(id).ifPresent(individual -> {
            individual.setFullName(fullName);
            individual.setPersonalFile(personalFile);
            individual.setHierarchy(hierarchyRepository.findById(hierarchyId).get());
            individual.setDependency(dependencyRepository.findById(dependencyId).get());

            individualRepository.save(individual);
        });
    }

    @Transactional
    public void deleteIndividual(Long id){
        individualRepository.deleteById(id);
    }

    public Individual getOneIndividual(Long id){
        return individualRepository.findById(id).get();
    }

    public Individual getOneIndividualByPersonalFile(String personalFile) throws MyException{
        validateSearch(personalFile);
        return individualRepository.findIndividualByPersonalFile(personalFile);
    }

    public List<Individual> getIndividualList(){
        return individualRepository.findIndividualList();
    }


    public List<Individual> getIndividualListByDependency(String dependencyName) throws MyException{
        validateFieldSearchNameDependency(dependencyName);
        return individualRepository.findIndividualListByDependency(dependencyName);
    }


    public List<Individual> getIndividualListByLoan(Long loanId) throws MyException{        
        return individualRepository.findIndividualListByLoan(loanRepository.findById(loanId).get().getWithdrawIndividual().getDependency().getName());
    }
    

    public void validateFields(
    String fullName,
    String personalFile,
    Long hierarchyId,
    Long dependencyId) throws MyException {

        if(fullName == null || fullName.isEmpty()){
            throw new MyException("El campo nombre no puede estar vacio");
        }       
        
        if(fullName.length() > 30){
            throw new MyException("El máximo de caracteres es 30 para el campo nombre y apellido");
        } 

        if(personalFile == null || personalFile.isEmpty()){
            throw new MyException("El campo legajo personal no puede estar vacio");
        }

        if(personalFile.length() > 8){
            throw new MyException("El máximo de caracteres es 8 para el campo legajo personal");
        }

        if(!personalFile.matches("-?\\d+")){
            throw new MyException("El valor ingresado en el campo legajo personal no es valido");
        } 

        if(hierarchyId == null){
            throw new MyException("El campo jerarquia no puede estar vacio");
        }

        if(dependencyId == null){
            throw new MyException("El campo dependencia no puede estar vacio");
        }
    }

    public void validateSearch(String personalFile) throws MyException {
        if(personalFile == null || personalFile.isEmpty()){
            throw new MyException("El campo busqueda no puede estar vacio");
        }      

        if(!personalFile.matches("-?\\d+")){
            throw new MyException("El valor ingresado en el campo busqueda no es valido");
        } 
    }

    public void validateFieldSearchNameDependency(String nameDependency) throws MyException{
        if(nameDependency.isEmpty() || nameDependency == null){
            throw new MyException("El campo de busqueda no puede estar vacio");
        }
    }
    
}
