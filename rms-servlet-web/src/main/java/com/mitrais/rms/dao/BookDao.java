package com.mitrais.rms.dao;

import com.mitrais.rms.model.Books;
import com.mitrais.rms.model.User;

import java.util.Optional;

/**
 * Provides interface specific to {@link User} data
 */
public interface BookDao extends Dao<Books, Integer> {

    /**
     * Find {@link User} by its username
     *
     * @param title
     * @return user
     */
    Optional<Books> findByTitle(String title);

    /**
     * Find {@link User} by its author
     *
     * @param author
     * @return user
     */
    Optional<Books> findByAuthor(String author);
}
