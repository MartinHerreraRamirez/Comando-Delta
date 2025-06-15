package com.comando.delta.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class AssignEquipment extends Assignment {    
    @OneToOne
    @JoinColumn(name = "assign_equipment_id", nullable = false)
    private Equipment assignEquipment;

    public Equipment getAssignEquipment() {
        return assignEquipment;
    }

    public void setAssignEquipment(Equipment assignEquipment) {
        this.assignEquipment = assignEquipment;
    }
}
