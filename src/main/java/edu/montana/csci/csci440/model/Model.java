package edu.montana.csci.csci440.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// base class for entities
public class Model {

    List<String> _errors = new LinkedList<>();

    public boolean create() {
        throw new UnsupportedOperationException("Needs to be implemented");
    }

    public boolean update() {
        throw new UnsupportedOperationException("Needs to be implemented");
    }

    public void delete() {
        throw new UnsupportedOperationException("Needs to be implemented");
    }

    public boolean verify() {
        throw new UnsupportedOperationException("Needs to be implemented");
    }

    public void addError(String err) {
        _errors.add(err);
    }

    public List<String> getErrors() {
        return _errors;
    }

    public boolean hasErrors(){
        return _errors.size() > 0;
    }

}
