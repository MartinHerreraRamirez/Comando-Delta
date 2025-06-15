package com.comando.delta.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;


@Entity
public class Individual {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long id;

	private String fullName;
	private String personalFile;

	@OneToOne
	private Hierarchy hierarchy;

	@OneToOne
	private Dependency dependency;

	@ManyToMany(mappedBy = "automobiles")
	private Set<Order> orders;

	public Individual() {
	}

	public Long getId() {
		return id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPersonalFile() {
		return personalFile;
	}

	public void setPersonalFile(String personalFile) {
		this.personalFile = personalFile;
	}

	public Hierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}

	public Dependency getDependency() {
		return dependency;
	}

	public void setDependency(Dependency dependency) {
		this.dependency = dependency;
	}

	public Set<Order> getOrder() {
    	return orders;
	}

	public void setOrder(Set<Order> orders) {
		this.orders = orders;
	}

}
