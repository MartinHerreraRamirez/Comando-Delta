package com.comando.delta.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comando.delta.enums.TypeEquipment;
import com.comando.delta.exception.MyException;
import com.comando.delta.model.Equipment;
import com.comando.delta.model.Loan;
import com.comando.delta.model.Users;
import com.comando.delta.repository.IEquipmentRepository;
import com.comando.delta.repository.IIndividualRepository;
import com.comando.delta.repository.ILoanRepository;

@Service
public class LoanService {
    @Autowired
    private ILoanRepository loanRepository;

    @Autowired
    private IEquipmentRepository equipmentRepository;

    @Autowired
    private IIndividualRepository individualRepository;

    @Autowired
    private HttpSession session;

    @Transactional
    public void createLoan(
    String personalFile,
    String numberEquipment,
    String contactNumber,
    String comment) throws MyException {
        validateFields(personalFile, numberEquipment, contactNumber);

        Loan loan = new Loan();
        LocalDateTime nowDate = LocalDateTime.now();
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        Equipment equipment = equipmentRepository.findEquipmentByNumber(numberEquipment);

        equipment.setIsAvailable(false);
        equipmentRepository.save(equipment);

        loan.setEquipment(equipment);
        loan.setWithdrawIndividual(individualRepository.findIndividualByPersonalFile(personalFile));
        loan.setStartDate(nowDate.format(formater));
        loan.setDeliveryUser((Users) session.getAttribute("currentUser"));
        loan.setIsActive(true);
        loan.setComment(validateComment(comment));
        loan.setContactNumber(contactNumber);

        loanRepository.save(loan);
    }

    @Transactional
    public void finishLoan(Long id, String personalFile, String novelty) {
        validateComment(novelty);

        loanRepository.findById(id).ifPresent(loan -> {
            LocalDateTime nowDate = LocalDateTime.now();
            DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            Equipment equipment = loan.getEquipment();
            equipment.setIsAvailable(true);
            equipmentRepository.save(equipment);

            loan.setEndDate(nowDate.format(formater));
            loan.setReturnIndividual(individualRepository.findIndividualByPersonalFile(personalFile));
            loan.setReceivesUser((Users) session.getAttribute("currentUser"));
            loan.setIsActive(false);
            loan.setNovelty(validateComment(novelty));
        });
    }

    public Loan getOneLoan(Long id) {
        return loanRepository.findById(id).get();
    }

    public List<Loan> getActiveLoanList(){
        return loanRepository.findActiveLoanList();
    }

    public List<Loan> getInactiveLoanList(){
        return loanRepository.findInactiveLoanList();
    }

    public List<Loan> getLoanList(String type, String status) {
        if (type != null && !type.isEmpty()) {
            if (status == null || status.isEmpty()) {
                return loanRepository.findLoanListByTypeEquipmentAndStatusTrue(TypeEquipment.valueOf(type));
            }
            if (status.equals("active")) {
                return loanRepository.findLoanListByTypeEquipmentAndStatusTrue(TypeEquipment.valueOf(type));
            }
            if (status.equals("inactive")) {
                return loanRepository.findLoanListByTypeEquipmentAndStatusFalse(TypeEquipment.valueOf(type));
            }
        } else {
            if ("active".equals(status)) {
                return loanRepository.findActiveLoanList();
            }
            if ("inactive".equals(status)) {
                return loanRepository.findInactiveLoanList();
            }
        }    
        return null;
    }

    public List<Loan> getLoanListByEquipmentNumber(String number) throws MyException{
        validateSearch(number);

        return loanRepository.findLoanListByEquipmentNumber(number);
    }

    public void validateFields(
    String equipmentId,
    String withdrawIndividualId,
    String contactNumber) throws MyException {
        if (equipmentId.isEmpty() || equipmentId == null) {
            throw new MyException("debe seleccionar un equipo");
        }

        if (withdrawIndividualId.isEmpty() || withdrawIndividualId == null) {
            throw new MyException("debe seleccionar un personal");
        }

        if(contactNumber.isEmpty() || contactNumber == null){
            throw new MyException("el campo numero de contacto no puede estar vacio");
        }

        if(!contactNumber.matches("-?\\d+")){
            throw new MyException("El valor ingresado en el campo numero de contacto no es valido");
        } 
    }

    public String validateComment(String comment) {
        if (comment.isEmpty() || comment == null) {
            return comment = "------";
        }

        return comment;
    }

    public void validateSearch(String nameDependency) throws MyException {
        if (nameDependency.isEmpty() || nameDependency == null) {
            throw new MyException("El campo de busqueda no puede estar vacio");
        }
    }

}
