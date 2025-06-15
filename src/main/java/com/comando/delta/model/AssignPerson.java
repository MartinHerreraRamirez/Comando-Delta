package com.comando.delta.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class AssignPerson extends Assignment{
    @ManyToOne
    @JoinColumn(name = "assign_person_id", nullable = false)
    private Individual assignPerson;

    @ManyToOne
    private Individual returnPerson;   

    public AssignPerson() {
    }

    public Individual getAssignPerson() {
        return assignPerson;
    }

    public void setAssignPerson(Individual assignPerson) {
        this.assignPerson = assignPerson;
    }   

    public Individual getReturnPerson() {
        return returnPerson;
    }

    public void setReturnPerson(Individual returnPerson) {
        this.returnPerson = returnPerson;
    }
}
