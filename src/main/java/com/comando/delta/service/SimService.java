package com.comando.delta.service;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comando.delta.exception.MyException;
import com.comando.delta.model.Sim;
import com.comando.delta.model.Users;
import com.comando.delta.repository.ISimRepository;

@Service
public class SimService {

    @Autowired
    private ISimRepository simRepository; 

    @Autowired
    HttpSession session;

    @Transactional
    public void createSim(
    String number,
    String serialNumber,
    String tradeMark,
    String plan) throws MyException {
        validateFields(number, serialNumber, tradeMark, plan);
        validateNumber(serialNumber);
        validateSerialNumber(serialNumber);

        Sim sim = new Sim();
        Users user = (Users) session.getAttribute("currentUser");

        sim.setNumber(number);
        sim.setSerialNumber(serialNumber);
        sim.setTradeMark(tradeMark);
        sim.setPlan(plan);
        sim.setUser(user);
        sim.setIsAvailable(true);

        simRepository.save(sim);
    }

    @Transactional
    public void deleteSim(Long id) {

        simRepository.deleteById(id);
    }

    @Transactional
    public void editSim(
    Long id,
    String number,
    String serialNumber,
    String tradeMark,
    String plan) throws MyException {
        validateFields(number, serialNumber, tradeMark, plan);
        validateUpdateNumber(number, id);
        validateUpdateSerialNumber(serialNumber, id);

        Users user = (Users) session.getAttribute("currentUser");

        simRepository.findById(id).ifPresent(sim -> {
            sim.setNumber(number);
            sim.setSerialNumber(serialNumber);
            sim.setTradeMark(tradeMark);
            sim.setPlan(plan);
            sim.setUser(user);

            simRepository.save(sim);
        });
    }

    public Sim getOneSimByNumberOrSerialNumber(String number) throws MyException {
        validateSearch(number);

        if (simRepository.findOneSimByNumber(number) != null) {
            return simRepository.findOneSimByNumber(number);
        }

        return simRepository.findOneSimBySerialNumber(number);
    }

    public Sim getOneSimById(Long id) {
        return simRepository.findOneSimById(id);
    }

    public List<Sim> getSimList() {
        return simRepository.findSimList();
    }

    public List<Sim> getActiveSimList() {
        return simRepository.findActiveSimList();
    }    

    public void validateUpdateNumber(String number, Long id) throws MyException {
        if (simRepository.existenceUpdatedNumber(number, id)) {
            throw new MyException("El número ya existe");
        }
    }

    public void validateUpdateSerialNumber(String serialNumber, Long id) throws MyException {
        if (simRepository.existenceUpdatedSerialNumber(serialNumber, id)) {
            throw new MyException("El número de serie ya existe");
        }
    }

    public void validateNumber(String number) throws MyException {
        if (simRepository.existenceNumber(number)) {
            throw new MyException("El número de abonado ya existe");
        }
    }

    public void validateSerialNumber(String serialNumber) throws MyException {
        if (simRepository.existenceSerialNumber(serialNumber)) {
            throw new MyException("El número de serie ya existe");
        }
    }

    public void validateSearch(String number) throws MyException {
        if (number.isEmpty() || number == null) {
            throw new MyException("El campo de búsqueda no puede estar vacío");
        }

        if (!number.matches("-?\\d+")) {
            throw new MyException("Los datos ingresados no son validos");
        }
    }

    private void validateFields(
            String number,
            String serialNumber,
            String tradeMark,
            String plan) throws MyException {
        if (number.isEmpty() || number == null) {
            throw new MyException("El campo número no puede estar vacío");
        }

        if (number.length() > 20) {
            throw new MyException("El máximo es 20 caracteres para el campo número");
        }

        if (!number.matches("-?\\d+")) {
            throw new MyException("El valor ingresado en el campo numero no es valido");
        }

        if (serialNumber.isEmpty() || serialNumber == null) {
            throw new MyException("El campo número de serie no puede estar vacío");
        }

        if (serialNumber.length() > 20) {
            throw new MyException("El máximo es 20 caracteres para el campo número de serie");
        }

        if (!serialNumber.matches("-?\\d+")) {
            throw new MyException("El valor ingresado en el campo numero de serie no es valido");
        }

        if (tradeMark.isEmpty() || tradeMark == null) {
            throw new MyException("El campo marca no puede estar vacío");
        }

        if (tradeMark.length() > 15) {
            throw new MyException("El máximo es 15 caracteres para el campo marca");
        }

        if (plan.isEmpty() || plan == null) {
            throw new MyException("El campo plan no puede estar vacío");
        }

        if (plan.length() > 10) {
            throw new MyException("El máximo es 10 caracteres para el campo plan");
        }

    }

}
