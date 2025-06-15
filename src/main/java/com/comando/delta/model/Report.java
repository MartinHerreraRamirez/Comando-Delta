package com.comando.delta.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reportNumber;
    private String gdeNumber;
    private String location;
    private String address;
    private String dependency;
    private String bodyReport;
    private boolean isVisible;
    private boolean isExtension;

    @ManyToOne
    private Users users;

    private String date;

    public Report() {
    }

    public Long getId() {
        return id;
    }

    public String getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(String reportNumber) {
        this.reportNumber = reportNumber;
    }

    public String getBodyReport() {
        return bodyReport;
    }

    public void setBodyReport(String bodyReport) {
        this.bodyReport = bodyReport;
    }

    public String getGdeNumber() {
        return gdeNumber;
    }

    public void setGdeNumber(String gdeNumber) {
        this.gdeNumber = gdeNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean getIsExtension() {
        return isExtension;
    }

    public void setIsExtension(boolean isExtension) {
        this.isExtension = isExtension;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDependency() {
        return dependency;
    }

    public void setDependency(String dependency) {
        this.dependency = dependency;
    }

}
