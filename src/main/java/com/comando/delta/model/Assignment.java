package com.comando.delta.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Assignment {
    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;

    private String assignmentType;
    private String startDate;
    private String endDate;
    private String contactNumber;

    @OneToOne
    private Sim sim;

    @ManyToOne
    private Users assignerUser;    

    @ManyToOne
    private Users receivesUser;

    private String comment;
    private String noventy;

    private Boolean isActive;

    public Assignment() {
    }

    public Long getId() {
        return id;
    }    

    public String getAssignmentType() {
        return assignmentType;
    }

    public void setAssignmentType(String assignmentType) {
        this.assignmentType = assignmentType;
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

    public Sim getSim() {
        return sim;
    }

    public void setSim(Sim sim) {
        this.sim = sim;
    }

    public Users getAssignerUser() {
        return assignerUser;
    }

    public void setAssignerUser(Users assignUser) {
        this.assignerUser = assignUser;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getNoventy() {
        return noventy;
    }

    public void setNoventy(String noventy) {
        this.noventy = noventy;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Users getReceivesUser() {
        return receivesUser;
    }

    public void setReceivesUser(Users receivesUser) {
        this.receivesUser = receivesUser;
    } 

    
   
}
