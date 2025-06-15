package com.comando.delta.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


import com.comando.delta.enums.TypeEquipment;

@Entity
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;

    private String tradeMark;
    private String model;
    private String number;

    @Enumerated(EnumType.STRING)
    private TypeEquipment typeEquipment;

    private String date;

    private boolean isAvailable;

    @ManyToOne
    private Users user;

    private String observation;

    private boolean haveSim;

   
    public Equipment() {
    }

    public Long getId() {
        return id;
    }

    public String getTradeMark() {
        return tradeMark;
    }

    public void setTradeMark(String tradeMark) {
        this.tradeMark = tradeMark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public TypeEquipment getTypeEquipment() {
        return typeEquipment;
    }

    public void setTypeEquipment(TypeEquipment typeEquipment) {
        this.typeEquipment = typeEquipment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String data) {
        this.date = data;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public boolean getHaveSim() {
        return haveSim;
    }

    public void setHaveSim(boolean haveSim) {
        this.haveSim = haveSim;
    }
}