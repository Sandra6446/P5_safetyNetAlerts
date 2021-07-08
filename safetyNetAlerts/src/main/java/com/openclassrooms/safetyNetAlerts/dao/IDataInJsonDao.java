package com.openclassrooms.safetyNetAlerts.dao;

import java.util.List;

/**
 * Reads and updates the json data file
 *
 * @param <T> The returned or updated object
 */
public interface IDataInJsonDao<T> {

    /**
     * Reads in the json data file
     *
     * @return The list of all the T objects find in data file
     */
    List<T> getAll();

    /**
     * Adds an object in json data file
     *
     * @param t The object to be added in the data file
     * @return The added object
     */
    T save(T t);

    /**
     * Updates an object in json data file
     *
     * @param t The object to be updated in data file
     * @return The updated object
     */
    T update(T t);

    /**
     * Deletes an object in json data file
     *
     * @param t The object to be deleted in data file
     * @return The deleted object
     */
    boolean remove(T t);

}
