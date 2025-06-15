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
import com.comando.delta.model.Users;
import com.comando.delta.repository.IEquipmentRepository;

@Service
public class EquipmentService {

    @Autowired
    private IEquipmentRepository equipmentRepository;

    @Autowired
    private HttpSession session;

    @Transactional
    public void createEquipment(
    String tradeMark,
    String model,
    String number,
    TypeEquipment typeEquipment,
    String observation) throws MyException {
        validateFields(tradeMark, model, number, typeEquipment, observation);
        validateEquipmentNumber(number, typeEquipment);

        Users user = (Users) session.getAttribute("currentUser");
        Equipment equipment = new Equipment();

        LocalDateTime nowDate = LocalDateTime.now();
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        equipment.setTradeMark(tradeMark);
        equipment.setModel(model);
        equipment.setNumber(number);
        equipment.setTypeEquipment(typeEquipment);
        equipment.setDate(nowDate.format(formater));
        equipment.setIsAvailable(true);
        equipment.setObservation(observation);
        equipment.setUser(user);
        equipment.setHaveSim(false);

        equipmentRepository.save(equipment);
    }

    @Transactional
    public void deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
    }

    public List<Equipment> getEquipmentList() {
        return equipmentRepository.findEquipmentList();
    }

    public List<Equipment> getEquipmentListByType(String type) throws MyException {
        return equipmentRepository.findEquipmentListByType(TypeEquipment.valueOf(type));
    }

    public List<Equipment> getActiveEquipmentListByType(String type) {
        return equipmentRepository.findActiveEquipmentListByType(TypeEquipment.valueOf(type));
    }

    public List<Equipment> getEquipmentListByFalseStatusisAvailable(String type) {
        return equipmentRepository.findEquipmentListByFalseStatusisAvailable(TypeEquipment.valueOf(type));
    }

    public Equipment getOneEquipmentById(Long id) {
        return equipmentRepository.findById(id).get();
    }

    public List<Equipment> getEquipmentByNumber(String number) throws MyException {
        validateSearch(number);

        return equipmentRepository.findEquipmentListByNumber(number);
    }

    @Transactional
    public void editEquipment(
    Long id,
    String tradeMark,
    String model,
    String number,
    TypeEquipment typeEquipment,
    String observation) throws MyException {
        validateFields(tradeMark, model, number, typeEquipment, observation);
        validateEquipmentNumber(number, typeEquipment, id);

        equipmentRepository.findById(id).ifPresent(equipment -> {
            LocalDateTime nowDate = LocalDateTime.now();
            DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

            equipment.setTradeMark(tradeMark);
            equipment.setModel(model);
            equipment.setNumber(number);
            equipment.setDate(nowDate.format(formater));
            equipment.setObservation(observation);

            equipmentRepository.save(equipment);
        });
    }

    public void validateEquipmentNumber(String equipmentNumber, TypeEquipment typeEquipment) throws MyException {
        if (equipmentRepository.existenceEquipment(equipmentNumber, typeEquipment)) {
            throw new MyException("El número de equipo ya existe en el listado de " + typeEquipment.toString());
        }
    }

    public void validateEquipmentNumber(String equipmentNumber, TypeEquipment typeEquipment, Long id)
            throws MyException {
        if (equipmentRepository.existenceUpdateEquipment(equipmentNumber, typeEquipment, id)) {
            throw new MyException("El número de equipo ya existe en el listado de " + typeEquipment.toString());
        }
    }

    public void validateSearch(String number) throws MyException {
        if (number.isEmpty() || number == null) {
            throw new MyException("El campo de búsqueda no puede estar vacío");
        }
    }

    public void validateFields(
    String tradeMark,
    String model,
    String number,
    TypeEquipment typeEquipment,
    String observation) throws MyException {
        if (tradeMark.isEmpty() || tradeMark == null) {
            throw new MyException("El campo marca no puede estar vacío");
        }

        if (tradeMark.length() > 12) {
            throw new MyException("El máximo de caracteres es 12 para el campo marca");
        }

        if (model.isEmpty() || model == null) {
            throw new MyException("El campo modelo no puede estar vacío");
        }

        if (model.length() > 12) {
            throw new MyException("El máximo de caracteres es 12 para el campo modelo");
        }

        if (number.isEmpty() || number == null) {
            throw new MyException("El campo número de equipo no puede estar vacío");
        }

        if (number.length() > 3) {
            throw new MyException("El máximo de caracteres es 3 para el campo número de equipo");
        }        

        if (typeEquipment == null) {
            throw new MyException("El campo tipo de equipo no puede estar vacío");
        }

        if (observation.length() > 150) {
            throw new MyException("El máximo es 150 caracteres para el campo observacíones");
        }
    }

}
