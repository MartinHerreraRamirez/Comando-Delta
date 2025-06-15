package com.comando.delta.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comando.delta.model.Assignment;
import com.comando.delta.model.Equipment;
import com.comando.delta.model.Individual;
import com.comando.delta.exception.MyException;
import com.comando.delta.model.AssignEquipment;
import com.comando.delta.model.AssignPerson;
import com.comando.delta.model.Sim;
import com.comando.delta.model.Users;
import com.comando.delta.repository.IAssignmentRepository;
import com.comando.delta.repository.IAssignEquipmentRepository;
import com.comando.delta.repository.IAssignPersonRepository;
import com.comando.delta.repository.IEquipmentRepository;
import com.comando.delta.repository.IIndividualRepository;
import com.comando.delta.repository.ISimRepository;

@Service
public class AssignmentService {

    @Autowired
    private IAssignmentRepository assignmentRepository;

    @Autowired
    private IAssignEquipmentRepository assignEquipmentRepository;

    @Autowired
    private IAssignPersonRepository assignPersonRepository;

    @Autowired
    private ISimRepository simRepository;

    @Autowired
    private IEquipmentRepository equipmentRepository;    

    @Autowired
    private IIndividualRepository individualRepository;

    @Autowired
    private HttpSession session;

    @Transactional
    public void createAssignmentToEquipment(    
    String numberEquipment, 
    String simNumber, 
    String contactNumber,
    String comment) throws MyException{
        validateFields(simNumber, numberEquipment, contactNumber, comment);

        LocalDateTime dateNow = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Sim sim = simRepository.findOneSimByNumber(simNumber);
        sim.setIsAvailable(false);
        simRepository.save(sim);

        Equipment equipment = equipmentRepository.findEquipmentByNumber(numberEquipment);
        equipment.setHaveSim(true);
        equipmentRepository.save(equipment);

        AssignEquipment assignEquipment = new AssignEquipment();

        assignEquipment.setAssignmentType("EQUIPMENT");
        assignEquipment.setAssignEquipment(equipment);
        assignEquipment.setAssignerUser((Users) session.getAttribute("currentUser"));
        assignEquipment.setSim(sim);
        assignEquipment.setStartDate(dateNow.format(formatter));
        assignEquipment.setComment(validateComment(comment));
        assignEquipment.setIsActive(true);
        assignEquipment.setContactNumber(contactNumber);

        assignEquipmentRepository.save(assignEquipment);        
    }

    @Transactional
    public void createAssignmentToIndividual(
    String personalFile,
    String simNumber,
    String contactNumber,
    String comment) throws MyException{
        validateFields(personalFile, simNumber, contactNumber, comment);

        LocalDateTime dateNow = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Sim sim = simRepository.findOneSimByNumber(simNumber);
        sim.setIsAvailable(false);
        simRepository.save(sim);
       
        AssignPerson assignPerson = new AssignPerson();

        Individual individual = individualRepository.findIndividualByPersonalFile(personalFile);

        assignPerson.setAssignmentType("INDIVIDUAL");
        assignPerson.setStartDate(dateNow.format(formatter));
        assignPerson.setAssignerUser((Users) session.getAttribute("currentUser"));
        assignPerson.setSim(sim);
        assignPerson.setComment(validateComment(comment));
        assignPerson.setAssignPerson(individual);
        assignPerson.setIsActive(true);
        assignPerson.setContactNumber(contactNumber);

        assignPersonRepository.save(assignPerson);        
    }

    @Transactional
    public void finishAssignment(Long id, String typeDTO, String personalFile, String noventy) {
        LocalDateTime dateNow = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        if (typeDTO.equals("INDIVIDUAL")) {
            assignPersonRepository.findById(id).ifPresent(assignPerson -> {
                assignPerson.setIsActive(false);
                assignPerson.setNoventy(validateComment(noventy));
                assignPerson.setEndDate(dateNow.format(formatter));
                assignPerson.setReturnPerson(individualRepository.findIndividualByPersonalFile(personalFile));
                assignPerson.setReceivesUser((Users) session.getAttribute("currentUser"));

                assignPerson.getSim().setIsAvailable(true);
            });
        } else {           
            assignEquipmentRepository.findById(id).ifPresent(assignEquipment -> {
                assignEquipment.setIsActive(false);
                assignEquipment.setNoventy(validateComment(noventy));
                assignEquipment.setEndDate(dateNow.format(formatter));
                assignEquipment.setReceivesUser((Users) session.getAttribute("currentUser"));

                assignEquipment.getSim().setIsAvailable(true);
            });
        }
    }   

    public List<Assignment> getActiveAssignmentList() {
        return assignmentRepository.findActiveAssignmentList();
    }  

    public Assignment getOneAssignment(Long id) {
        return assignmentRepository.findById(id).get();
    }

    public List<Assignment> getAssignmentListByEquipmentOrPhoneNumber(String number) throws MyException{
        validateNumber(number);
        if(number.length() < 5){
            return assignmentRepository.findAssignmentListByEquipmentNumber(number);
        }

        return assignmentRepository.findAssigmentListByPhoneNumber(number);
    }

    private String validateComment(String comment) {
        if (comment.isEmpty() || comment == null) {
            return "-----";
        }
        return comment;
    }

    private void validateNumber(String number) throws MyException{
        if(number.isEmpty() || number == null){
            throw new MyException("El campo de busqueda no puede estar vacío");
        }

        if(!number.matches("-?\\d+")){
            throw new MyException("Los datos ingresados no son validos");
        }
    }   

    

    public void validateFields(
    String personalFileOrnumberEquipment,
    String simNumber,
    String contactNumber,
    String comment) throws MyException{
        if(personalFileOrnumberEquipment.isEmpty() || personalFileOrnumberEquipment == null){
            throw new MyException("El campo de busqueda no puede estar vacío");
        }

        if(simNumber.isEmpty() || simNumber == null){
            throw new MyException("El campo sim no puede estar vacío");
        }       
        
        if(contactNumber.isEmpty() || contactNumber == null){
            throw new MyException("El campo numero de contacto no puede estar vacío");
        }

        if(!contactNumber.matches("-?\\d+")){
            throw new MyException("El valor ingresado en campo numero de contacto no es valido");
        }

        if(comment.isEmpty() || comment == null){
            throw new MyException("El campo comentario no puede estar vacio");
        }
    }   


}
