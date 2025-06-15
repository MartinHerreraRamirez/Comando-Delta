package com.comando.delta.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.comando.delta.enums.TypeCharge;
import com.comando.delta.exception.MyException;
import com.comando.delta.model.Charge;
import com.comando.delta.repository.IChargeRepository;

@Service
public class ChargeService {

    @Autowired 
    private IChargeRepository chargeRepository;

    @Autowired
    private ImageService imageService;

    @Transactional
    public void createCharge(
    String number,
    String file,
    String departure,
    String opening,
    String description,
    String dateAdmission,
    String dateCharge,
    String expedient,
    String serialNumber,
    String price,
    String quantity,
    String place,
    String typeCharge,
    MultipartFile image) throws Exception {
        validateFields(number, file, departure, opening, description, dateAdmission, dateCharge, expedient, serialNumber, price, quantity, place, typeCharge, image);
        existenceCharge(file);

        Charge charge = new Charge();

        charge.setNumber(number);
        charge.setFile(file);
        charge.setDeparture(departure);
        charge.setOpening(opening);
        charge.setDescription(description);
        charge.setDateAdmission(dateAdmission);
        charge.setDateCharge(dateCharge);
        charge.setExpedient(expedient);
        charge.setSerialNumber(serialNumber);
        charge.setPrice(price);
        charge.setQuantity(quantity);
        charge.setPlace(place);
        charge.setTypeCharge(TypeCharge.valueOf(typeCharge));
        charge.setImage(imageService.saveImage(image));

        chargeRepository.save(charge);
    }


    public Charge getOneChargeById(Long id){
        return chargeRepository.findById(id).get();
    }


    public Charge getOneChargeByNumberFile(String file) throws MyException{
        validateSearch(file);
        
        return chargeRepository.findOneChargeByNumberFile(file);
    }


    public List<Charge> getChargeList(){        
        return chargeRepository.findChargeList();
    }


    public List<Charge> getChargeListByBrigadeType(){
        return chargeRepository.findChargeListByBrigadeType();
    }


    public List<Charge> getChargeListByTechnicalType(){
        return chargeRepository.findChargeListByTechnicalType();
    }


    public List<Charge> getChargeListByAdministrativeType(){
        return chargeRepository.findChargeListByTechnicalType();
    }
    

    public void deleteCharge(Long id){
        chargeRepository.deleteById(id);
    }


    @Transactional
    public void editCharge(
    Long id,
    String number,
    String file,
    String departure,
    String opening,
    String description,
    String dateAdmission,
    String dateCharge,
    String expedient,
    String serialNumber,
    String price,
    String quantity,
    String place,
    String typeCharge,
    MultipartFile image) throws Exception {
        validateFields(number, file, departure, opening, description, dateAdmission, dateCharge, expedient, serialNumber, price, quantity, place, typeCharge, image);
        existenceEditedCharge(file);

        try {
            chargeRepository.findById(id).ifPresent( charge -> {
                try {
                    charge.setNumber(number);
                    charge.setFile(file);
                    charge.setDeparture(departure);
                    charge.setOpening(opening);
                    charge.setDescription(description);
                    charge.setDateAdmission(dateAdmission);
                    charge.setDateCharge(dateCharge);
                    charge.setExpedient(expedient);
                    charge.setSerialNumber(serialNumber);
                    charge.setPrice(price);
                    charge.setQuantity(quantity);
                    charge.setPlace(place);   
                    charge.setTypeCharge(TypeCharge.valueOf(typeCharge));             
                    charge.setImage(imageService.editImage(image, charge.getImage() != null ? charge.getImage().getId() : null));                    
                } catch (Exception e) {
                }
    
                chargeRepository.save(charge);
            });            
        } catch (Exception e) {
        }

    }

    public void existenceCharge(String file) throws MyException{
        if(chargeRepository.existenceCharge(file)){
            throw new MyException("El número de ficha ya existe");
        }
    }

    public void existenceEditedCharge(String file) throws MyException{       
        if(chargeRepository.existenceEditedCharge(file) > 1){
            throw new MyException("El número de ficha ya existe");
        }
    }

    public void validateSearch(String file) throws MyException{
        if(file == null || file.isEmpty()){
            throw new MyException("El campo de búsqueda no puede estar vacío");
        }
    }

    public void validateFields(
    String number,
    String file,
    String departure,
    String opening,
    String description,
    String dateAdmission,
    String dateCharge,
    String expedient,
    String serialNumber,
    String price,
    String quantity,
    String place,
    String typeCharge,
    MultipartFile image) throws MyException {
        if(number == null || number.isEmpty()){
            throw new MyException("El campo número no puede estar vacío");
        }   
        
        if(number.length() > 10){
            throw new MyException("El máximo es 10 caracteres para el campo numero");
        }     
        
        if(!number.matches("-?\\d+")){
            throw new MyException("El valor ingresado en el campo numero no es valido");
        } 

        if(file == null || file.isEmpty()){
            throw new MyException("El campo ficha no puede estar vacío");
        }  
        
        if(file.length() > 10){
            throw new MyException("El máximo es 10 caracteres para el campo ficha");
        }

        if(!file.matches("-?\\d+")){
            throw new MyException("El valor ingresado en el campo ficha no es valido");
        } 

        if(departure == null || departure.isEmpty()){
            throw new MyException("El campo partida no puede estar vacío");
        }   
        
        if(departure.length() > 5){
            throw new MyException("El máximo es 5 caracteres para el campo partida");
        }

        if(!departure.matches("-?\\d+")){
            throw new MyException("El valor ingresado en el campo partida no es valido");
        } 

        if(opening == null || opening.isEmpty()){
            throw new MyException("El campo apertura no puede estar vacío");
        }   
        
        if(opening.length() > 5){
            throw new MyException("El máximo es 10 caracteres para el campo apertura");
        }

        if(!opening.matches("-?\\d+")){
            throw new MyException("El valor ingresado en el campo apertura no es valido");
        } 

        if(description.isEmpty() || description == null){
            throw new MyException("El campo descripción no puede estar vacío");
        }

        if(description.length() > 255){
            throw new MyException("El máximo es 255 caracteres para el campo descripción");
        }

        if(dateAdmission.isEmpty() || dateAdmission == null){
            throw new MyException("El campo fecha de ingreso no puede estar vacío");
        }

        if(dateAdmission.length() > 12){
            throw new MyException("El máximo es 12 caracteres para el campo fecha de ingreso");
        }

        if(dateCharge.isEmpty() || dateCharge == null){
            throw new MyException("El campo fecha de cargo no puede estar vacío");
        }

        if(dateCharge.length() > 12){
            throw new MyException("El máximo es 12 caracteres para el campo fecha de cargo");
        }

        if(expedient == null){
            throw new MyException("El campo expediente no puede estar vacío");
        }          

        if(serialNumber == null){
            throw new MyException("El campo número de serie no puede estar vacío");
        }     

        if(!serialNumber.matches("-?\\d+")){
            throw new MyException("El valor ingresado en el campo N° de serie no es valido");
        } 

        if(price == null || price.isEmpty()){
            throw new MyException("El campo precio no puede estar vacío");
        } 

        if(price.length() > 10){
            throw new MyException("El máximo es 10 caracteres para el campo precio");
        } 
        
        if(!price.matches("-?\\d+")){
            throw new MyException("El valor ingresado en el campo precio no es valido");
        } 

        if(quantity == null || quantity.isEmpty()){
            throw new MyException("El campo cantidad no puede estar vacío");
        }

        if(quantity.length() > 10){
            throw new MyException("El máximo es 10 caracteres para el campo cantidad");
        } 

        if(!quantity.matches("-?\\d+")){
            throw new MyException("El valor ingresado en el campo cantidad no es valido");
        }       

        if(place.isEmpty() || place == null){
            throw new MyException("El campo lugar no puede estar vacío");
        }

        if(place.length() > 25){
            throw new MyException("El máximo es 25 caracteres para el campo lugar");
        }

        if(typeCharge.isEmpty()){
            throw new MyException("El campo oficina no puede estar vacio");
        }       
    }  

    
}
