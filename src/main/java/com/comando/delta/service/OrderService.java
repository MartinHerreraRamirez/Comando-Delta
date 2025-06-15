package com.comando.delta.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comando.delta.exception.MyException;
import com.comando.delta.interfaces.IStatistic;
import com.comando.delta.model.Order;
import com.comando.delta.repository.IAutomobileRepository;
import com.comando.delta.repository.IIndividualRepository;
import com.comando.delta.repository.IOrderRepository;

@Service
public class OrderService {

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IIndividualRepository individualRepository;

    @Autowired
    private IAutomobileRepository automobileRepository;

    @Transactional
    public void createOrderService(
    String orderNumber,
    String place,
    String requestType,    
    List<String> serviceType,
    Set<Long> individuals,
    Set<Long> automobiles) throws MyException{
        Order serviceOrder = new Order();

        LocalDateTime nowDate = LocalDateTime.now();
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        serviceOrder.setOrderNumber(orderNumber);
        serviceOrder.setPlace(place);
        serviceOrder.setRequestType(requestType);
        serviceOrder.setServiceType(serviceType);
        serviceOrder.setIndividuals(new LinkedHashSet<>(individualRepository.findAllById(individuals)));
        serviceOrder.setAutomobiles(new LinkedHashSet<>(automobileRepository.findAllById(automobiles)));        
        serviceOrder.setIsActive(true);     
        serviceOrder.setDepartureTime(nowDate.format(formater));
        
        orderRepository.save(serviceOrder);
    }

    @Transactional
    public void finishOrderService(Long id){
        orderRepository.findById(id).ifPresent(serviceOrder -> {
            LocalDateTime nowDate = LocalDateTime.now();
            DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

            serviceOrder.setIsActive(false);
            serviceOrder.setEntryTime(nowDate.format(formater));
            
            orderRepository.save(serviceOrder);
        });
    }

    @Transactional
    public void editOrderService(
    Long id,
    String orderNumber,
    String place,
    String requestType,    
    List<String> servicesType,
    Set<Long> individuals,
    Set<Long> automobiles) throws MyException{
        orderRepository.findById(id).ifPresent( order -> {
            order.setOrderNumber(orderNumber);
            order.setPlace(place);
            order.setRequestType(requestType);
            order.setServiceType(servicesType);
            order.setIndividuals(new LinkedHashSet<>(individualRepository.findAllById(individuals)));
            order.setAutomobiles(new LinkedHashSet<>(automobileRepository.findAllById(automobiles)));

            orderRepository.save(order);
        });
    }

    public List<Order> getActiveOrderList(){
        return orderRepository.findActiveOrderList();
    }

     public List<Order> getInactiveOrderList(){
        return orderRepository.findInactiveOrderList();
    }

    public List<Order> getOrderListByStatus(String status){
        return "active".equalsIgnoreCase(status) 
                ? orderRepository.findActiveOrderList()
                : orderRepository.findInactiveOrderList();
    }

    public List<IStatistic> getStatistics(){
        return orderRepository.findStatisticsList();
    }

    public Order getOneOrder(Long id){
        return orderRepository.findById(id).get();
    }

    public Order getOneOrderByNumber(String number){
        return orderRepository.findOrderByNumber(number);
    }

    @Transactional
    public void deleteAll(){
        orderRepository.deleteAll();
    }

    public void existenceOrder(String orderNumber) throws MyException{
        if(orderRepository.existenceOrder(orderNumber)){
            throw new MyException("El número de ficha ya existe");
        }
    }

    public void existenceEditedOrder(String orderNumber) throws MyException{       
        if(orderRepository.existenceEditedOrder(orderNumber) > 1){
            throw new MyException("El número de ficha ya existe");
        }
    }
    

    public void validateFields(String orderNumber, String place) throws MyException{
        if(orderNumber == null || orderNumber.isEmpty()){
            throw new MyException("El campo número no puede estar vacío");
        }   
        
        if(orderNumber.length() > 5){
            throw new MyException("El máximo es 5 caracteres para el campo numero");
        }     
        
        if(!orderNumber.matches("-?\\d+")){
            throw new MyException("El valor ingresado en el campo numero no es valido");
        } 

        if(place == null || place.isEmpty()){
            throw new MyException("El campo lugar no puede estar vacío");
        }   
        
        if(place.length() > 20){
            throw new MyException("El máximo es 20 caracteres para el campo lugar");
        }  
        
    }
    
}
