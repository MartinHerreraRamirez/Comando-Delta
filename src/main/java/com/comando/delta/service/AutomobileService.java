package com.comando.delta.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.comando.delta.exception.MyException;
import com.comando.delta.model.Automobile;
import com.comando.delta.repository.IAutomobileRepository;

@Service
public class AutomobileService {
    
    @Autowired 
    private IAutomobileRepository automobileRepository;

    @Autowired
    private ImageService imageService;

    @Transactional
    public void createMobile(
    String number,
    String tradeMark,
    String model,
    String licensePlate,
    String color,
    String typeMobile,
    String status,
    MultipartFile image) throws Exception{
        Automobile automobile = new Automobile();

        automobile.setNumber(number);
        automobile.setTradeMark(tradeMark);
        automobile.setModel(model);
        automobile.setLicensePlate(licensePlate);
        automobile.setColor(color);
        automobile.setTypeMobile(typeMobile);
        automobile.setStatus(status);
        automobile.setImage(imageService.saveImage(image));

        automobileRepository.save(automobile);
    }

    public List<Automobile> getAutomobileList(){
        return automobileRepository.findAutomobileList();
    }

    public Automobile getOneAutomobile(Long id){
        return automobileRepository.findAutomobileById(id);
    }

    public Automobile getOneAutomobileByNumber(String number) throws MyException{
        validateSearch(number);
        return automobileRepository.findAutomobileByNumber(number);
    }

    @Transactional
    public void editMobile(
    Long id,
    String number,
    String tradeMark,
    String model,
    String licensePlate,
    String color,
    String typeMobile,
    String status,
    MultipartFile image) throws Exception{
        try {
            automobileRepository.findById(id).ifPresent( automobile -> {
                try {
                    automobile.setNumber(number);
                    automobile.setTradeMark(tradeMark);
                    automobile.setModel(model);
                    automobile.setLicensePlate(licensePlate);
                    automobile.setColor(color);
                    automobile.setTypeMobile(typeMobile);
                    automobile.setStatus(status);
                    if (image != null && !image.isEmpty()) {
                        automobile.setImage(imageService.editImage(image, 
                            automobile.getImage() != null ? automobile.getImage().getId() : null));
                    }                
                } catch (Exception e) {
                }
    
                automobileRepository.save(automobile);
            });            
        } catch (Exception e) {
        }       
    }

    public void validate(
    String number,
    String tradeMark,
    String model,
    String licensePlate,
    String color,
    String typeMobile,
    Boolean status,
    MultipartFile image) throws MyException{
        if(number.isEmpty() || number == null){
            throw new MyException("El campo numero no puede estar vacio");
        }

        if(!number.matches("-?\\d+")){
            throw new MyException("El valor ingresado en el campo numero no es valido");
        } 

         
        if(number.length() > 10){
            throw new MyException("El máximo es 10 caracteres para el campo numero");
        }  

        if(tradeMark.isEmpty() || tradeMark == null){
            throw new MyException("El campo marca no puede estar vacio");
        }

         
        if(tradeMark.length() > 15){
            throw new MyException("El máximo es 15 caracteres para el campo numero");
        }  

        if(model.isEmpty() || model == null){
            throw new MyException("El campo modelo no puede estar vacio");
        }

        if(model.length() > 10){
            throw new MyException("El máximo es 10 caracteres para el campo modelo");
        }  

        if(licensePlate.isEmpty() || licensePlate == null){
            throw new MyException("El campo dominio no puede estar vacio");
        }

        if(tradeMark.length() > 15){
            throw new MyException("El máximo es 15 caracteres para el campo dominio");
        }  

        if(color.isEmpty() || color == null){
            throw new MyException("El campo color no puede estar vacio");
        }

        if(color.length() > 12){
            throw new MyException("El máximo es 12 caracteres para el campo color");
        }  

        if(typeMobile.isEmpty() || typeMobile == null){
            throw new MyException("El campo tipo de movil no puede estar vacio");
        }
    }

    public void validateSearch(String number) throws MyException{
        if(number == null || number.isEmpty()){
            throw new MyException("El campo de búsqueda no puede estar vacío");
        }
    }
}
