package edu.montana.csci.csci440.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// base class for entities
public class Model {

    List<String> _errors = new LinkedList<>();

    public boolean create()
    {
        throw new UnsupportedOperationException("Needs to be implemented");
    }

    public boolean update()
    {
        throw new UnsupportedOperationException("Needs to be implemented");
    }

    public void delete()
    {
        throw new UnsupportedOperationException("Needs to be implemented");
    }

    public boolean verify()
    {
        throw new UnsupportedOperationException("Needs to be implemented");
    }

    public void addError(String err)
    {
        _errors.add(err);
    }

    public List<String> getErrors()
    {
        return _errors;
    }

    public boolean hasErrors()
    {
        return _errors.size() > 0;
    }

<<<<<<< HEAD
=======
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return  false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Field[] declaredFields = this.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            try {
                if (!declaredField.get(this).equals(declaredField.get(obj))) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        Field[] declaredFields = this.getClass().getDeclaredFields();
        List<Object> values = new LinkedList<>();
        for (Field declaredField : declaredFields) {
            try {
                values.add(declaredField.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return Objects.hash(values.toArray());
    }
>>>>>>> 089cc343544bddb1266a1a5edacd1680a1855e93
}
