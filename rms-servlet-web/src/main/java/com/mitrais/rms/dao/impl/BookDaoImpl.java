package com.mitrais.rms.dao.impl;

import com.mitrais.rms.dao.BookDao;
import com.mitrais.rms.dao.DataSourceFactory;
import com.mitrais.rms.model.Books;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Febri_MW251
 */
public class BookDaoImpl implements BookDao {

    public Optional<Books> find(Integer id) {
        try (Connection connection = DataSourceFactory.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM book WHERE book_id=?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Books books = new Books(rs.getInt("book_id"), rs.getString("title"), rs.getString("author"), rs.getFloat("price"));
                return Optional.of(books);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<Books> findAll() {
        List<Books> result = new ArrayList<>();
        try (Connection connection = DataSourceFactory.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM book");
            while (rs.next()) {
                Books book = new Books(rs.getInt("book_id"), rs.getString("title"), rs.getString("author"), rs.getFloat("price"));
                result.add(book);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean save(Books book) {
        try (Connection connection = DataSourceFactory.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO book VALUES (NULL, ?, ?, ?)");
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setFloat(3, book.getPrice());
            int i = stmt.executeUpdate();
            if (i == 1) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Books book) {
        try (Connection connection = DataSourceFactory.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("UPDATE book SET title=?, author=?, price=? WHERE book_id=?");
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setFloat(3, book.getPrice());
            stmt.setInt(4, book.getBookId());
            int i = stmt.executeUpdate();
            if (i == 1) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Books book) {
        try (Connection connection = DataSourceFactory.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM book WHERE book_id=?");
            stmt.setInt(1, book.getBookId());
            int i = stmt.executeUpdate();
            if (i == 1) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<Books> findByTitle(String title) {
        try (Connection connection = DataSourceFactory.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM book WHERE title=?");
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Books book = new Books(rs.getInt("book_id"), rs.getString("title"), rs.getString("author"), rs.getFloat("price"));
                return Optional.of(book);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<Books> findByAuthor(String author) {
        try (Connection connection = DataSourceFactory.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM books WHERE author=?");
            stmt.setString(1, author);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Books book = new Books(rs.getInt("book_id"), rs.getString("title"), rs.getString("author"), rs.getFloat("price"));
                return Optional.of(book);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return Optional.empty();
    }

    private static class SingletonHelper {

        private static final BookDaoImpl INSTANCE = new BookDaoImpl();
    }

    public static BookDao getInstance() {
        return SingletonHelper.INSTANCE;
    }
}
