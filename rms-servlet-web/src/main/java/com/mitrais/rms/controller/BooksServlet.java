package com.mitrais.rms.controller;

import com.mitrais.rms.dao.BookDao;
import com.mitrais.rms.dao.impl.BookDaoImpl;
import com.mitrais.rms.model.Books;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet("/books/*")
public class BooksServlet extends AbstractController {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        try {
            switch (action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/insert":
                    insertBook(request, response);
                    break;
                case "/delete":
                    deleteBook(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/update":
                    updateBook(request, response);
                    break;
                default:
                    listBooks(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listBooks(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        BookDao bookDao = BookDaoImpl.getInstance();
        List<Books> books = bookDao.findAll();
        request.setAttribute("books", books);

        RequestDispatcher dispatcher = request.getRequestDispatcher(VIEW_PREFIX + "/books/list" + VIEW_SUFFIX);
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(VIEW_PREFIX + "/books/form" + VIEW_SUFFIX);
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        Optional<Books> book = BookDaoImpl.getInstance().find(id);

        RequestDispatcher dispatcher = request.getRequestDispatcher(VIEW_PREFIX + "/books/form" + VIEW_SUFFIX);
        request.setAttribute("book", book);
        dispatcher.forward(request, response);

    }

    private void insertBook(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        Float price = Float.parseFloat(request.getParameter("price"));

        Books book = new Books();
        book.setAuthor(author);
        book.setTitle(title);
        book.setPrice(price);

        BookDaoImpl.getInstance().save(book);
        response.sendRedirect("list");
    }

    private void updateBook(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        Float price = Float.parseFloat(request.getParameter("price"));

        Books book = new Books(id, title, author, price);
        BookDaoImpl.getInstance().update(book);

        response.sendRedirect("list");
    }

    private void deleteBook(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        Books book = new Books(id);
        BookDaoImpl.getInstance().delete(book);
        response.sendRedirect("list");
    }
}
