package com.comando.delta.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comando.delta.enums.TypeEquipment;
import com.comando.delta.exception.MyException;
import com.comando.delta.model.AssignEquipment;
import com.comando.delta.repository.IAssignEquipmentRepository;

@Service
public class AssignEquipmentService{

    @Autowired
    private IAssignEquipmentRepository assignEquipmentRepository;

    public List<AssignEquipment> getAssignEquipmentListByTypeAndStatus(String type, String status) throws MyException{
        validateLoadFields(type, status);

        TypeEquipment typeEquipment = TypeEquipment.valueOf(type);

        if("ACTIVE".equals(status)){
            return assignEquipmentRepository.findAssignEquipmentListByTypeEquipmentAndTrueStatus(typeEquipment);
        }else {
            return assignEquipmentRepository.findAssignEquipmentListByTypeEquipmentAndFalseStatus(typeEquipment);
        }
    }

    public Optional<AssignEquipment> getAssignEquipmentBySimNumberOrEquipNumber(String number) throws MyException{
        validateSearchField(number);

        return assignEquipmentRepository.findAssignEquipmentBySimNumberOrEquipNumber(number);
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
