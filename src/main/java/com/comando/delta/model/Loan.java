package com.comando.delta.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Equipment equipment;

    @ManyToOne
    private Individual withdrawIndividual;

    @ManyToOne
    private Individual returnIndividual;

    @ManyToOne
    private Users deliveryUser;

    @ManyToOne
    private Users receivesUser;

    private String startDate;
    private String endDate;

    private boolean isActive;

    private String comment;
    private String novelty;
    private String contactNumber;   

    public Loan() {
    }

    public Long getId() {
        return id;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Individual getWithdrawIndividual() {
        return withdrawIndividual;
    }

    public void setWithdrawIndividual(Individual withdrawIndividual) {
        this.withdrawIndividual = withdrawIndividual;
    }

    public Individual getReturnIndividual() {
        return returnIndividual;
    }

    public void setReturnIndividual(Individual returnIndividual) {
        this.returnIndividual = returnIndividual;
    }

    public Users getDeliveryUser() {
        return deliveryUser;
    }

    public void setDeliveryUser(Users deliveryUser) {
        this.deliveryUser = deliveryUser;
    }

    public Users getReceivesUser() {
        return receivesUser;
    }

    public void setReceivesUser(Users receivesUser) {
        this.receivesUser = receivesUser;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getNovelty() {
        return novelty;
    }

    public void setNovelty(String novelty) {
        this.novelty = novelty;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

}
