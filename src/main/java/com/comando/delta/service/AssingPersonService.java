package com.comando.delta.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comando.delta.exception.MyException;
import com.comando.delta.model.AssignPerson;
import com.comando.delta.repository.IAssignPersonRepository;

@Service
public class AssingPersonService {

    @Autowired
    private IAssignPersonRepository assignPersonRepository;
  

    public List<AssignPerson> getAssignPersonListByTrueStatus(){
        return assignPersonRepository.findAssignPersonListByTrueStatus();
    }


    public AssignPerson getOneAssignPersonById(Long id){
        return assignPersonRepository.findById(id).get();
    }


    public List<AssignPerson> getAssignmentListByTypeAndStatus(String type, String status) throws MyException{
        validateLoadFields(type, status);

        if("ACTIVE".equals(status)){
            return assignPersonRepository.findAssignPersonListByTrueStatus();
        } else {
            return assignPersonRepository.findAssignPersonListByFalseStatus();
        }               
    }


    public Optional<AssignPerson> getAssignPersonBySimNumberOrEquipNumber(String number) throws MyException{
        validateSearchField(number);
        // return assignPersonRepository.findAssignPersonBySimNumberOrEquipmentNumber(number);
        return null;
    }

    public void validateLoadFields(String type, String status) throws MyException{
        if(type == null || type.isEmpty()){
            throw new MyException("debe seleccionar un tipo de asignacion a cargar");
        }

        if(status == null || status.isEmpty()){
            throw new MyException("debe seleccionar una opcion");
        }
    }

    public void validateSearchField(String number) throws MyException{
        if(number == null || number.isEmpty()){
            throw new MyException("El campo de busqueda no puede estar vacio");
        }

    }
    
}
