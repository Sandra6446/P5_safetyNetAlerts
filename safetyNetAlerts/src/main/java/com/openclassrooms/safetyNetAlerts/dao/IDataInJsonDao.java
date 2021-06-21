package com.openclassrooms.safetyNetAlerts.dao;

import java.util.List;

public interface IDataInJsonDao<T> {

    List<T> getAll();

    T save(T t);

    T update(T t);

    boolean remove (T t);

}
