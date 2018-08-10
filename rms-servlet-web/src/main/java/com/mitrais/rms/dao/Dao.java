package com.mitrais.rms.dao;

import java.util.List;
import java.util.Optional;

/**
 * This interface provides generic CRUD operation. See <a href="https://en.wikipedia.org/wiki/Create,_read,_update_and_delete>CRUD</a>
 * Its uses generic to provide type-safe operation.
 */
public interface Dao<T, ID> extends AutoCloseable {
    /**
     * Find single record by id.
     * @param id id
     * @return record wrapped by {@link Optional}
     */
    Optional<T> find(ID id);

    /**
     * Find all records
     * @return list of records
     */
    List<T> findAll();

    /**
     * Persist /  update record to database
     * @param o record
     * @return true if operation success
     */
    boolean save(T o);

    /**
     * set deleted = 1 data with specified id from database
     * @param o record
     * @return true if operation success
     */
    boolean delete(T o);
}
