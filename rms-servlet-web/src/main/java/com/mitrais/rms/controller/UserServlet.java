package com.mitrais.rms.controller;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mitrais.rms.dao.UserDao;
import com.mitrais.rms.dao.impl.UserDaoImpl;
import com.mitrais.rms.injectorbinding.ControllerModule;
import com.mitrais.rms.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/users/*")
public class UserServlet extends AbstractController
{
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String path = getTemplatePath(req.getServletPath()+req.getPathInfo());
        
        Injector injector = Guice.createInjector(new ControllerModule());
        try (UserDao userDao = injector.getInstance(UserDaoImpl.class)) {

            if ("/list".equalsIgnoreCase(req.getPathInfo())){
                List<User> users = userDao.findAll();
                req.setAttribute("users", users);
            }
            
        	userDao.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        RequestDispatcher requestDispatcher = req.getRequestDispatcher(path);
        requestDispatcher.forward(req, resp);
    }
}
