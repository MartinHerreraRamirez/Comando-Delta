package com.comando.delta.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comando.delta.exception.MyException;
import com.comando.delta.model.Hierarchy;
import com.comando.delta.repository.IHierarchyRepository;

@Service
public class HierarchyService {

    @Autowired
    private IHierarchyRepository hierarchyRepository;

    @Transactional
    public void createHierarchy(String name) throws MyException {
        validateField(name);

        Hierarchy hierarchy = new Hierarchy();

        hierarchy.setName(name);

        hierarchyRepository.save(hierarchy);
    }

    @Transactional
    public void editHierarchy(Long id, String name) throws MyException {
        validateField(name);

        hierarchyRepository.findById(id).ifPresent(hierarchy -> {
            hierarchy.setName(name);

            hierarchyRepository.save(hierarchy);
        });
    }

    @Transactional
    public void deleteHierarchy(Long id) {
        hierarchyRepository.deleteById(id);
    }

    public Hierarchy getOneHierarchy(Long id) {
        return hierarchyRepository.findById(id).get();
    }

    public List<Hierarchy> getHierarchyList() {
        return hierarchyRepository.findHierarchyList();
    }

    public void validateField(String name) throws MyException {
        if (name.isEmpty() || name == null) {
            throw new MyException("El campo nombre no puede estar vacio");
        }

        if (name.length() > 30) {
            throw new MyException("El máximo de caracteres es 30 para el campo nombre");
        }
    }

}
