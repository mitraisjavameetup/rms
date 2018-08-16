package com.mitrais.rms.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mitrais.rms.dao.UserDao;
import com.mitrais.rms.dao.impl.UserDaoImpl;
import com.mitrais.rms.injectorbinding.ControllerModule;
import com.mitrais.rms.model.User;

@WebServlet("/users/*")
public class UserServlet extends AbstractController {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pathInfo = request.getPathInfo();
		RequestDispatcher requestDispatcher = null;

		Injector injector = Guice.createInjector(new ControllerModule());
		try (UserDao userDao = injector.getInstance(UserDaoImpl.class)) {
			if (request.getPathInfo().contains("/form")) {
				requestDispatcher = getFormPage(userDao, pathInfo, request);
			} else if (request.getPathInfo().contains("/delete")) {
				doUserDelete(userDao, pathInfo, request);
				response.sendRedirect("/rms-servlet-web/users/");
				return;
			} else {
				requestDispatcher = getListPage(userDao, request);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		requestDispatcher.forward(request, response);
	}

	private RequestDispatcher getListPage(UserDao userDao, HttpServletRequest request) {
		String pathInfo = "/users/list";
		List<User> users = userDao.findAll();
		request.setAttribute("users", users);
		String path = getTemplatePath(request.getServletPath(), pathInfo);

		return request.getRequestDispatcher(path);
	}
	
	private void doUserDelete(UserDao userDao, String pathInfo, HttpServletRequest request) {
		User userToDelete = getUserFromRequestedId(userDao, pathInfo);
		boolean isDeleted = userDao.delete(userToDelete);
		
		String status = isDeleted ? "success" : "fail";
		request.getSession().setAttribute("statusSession", status); 	
	}

	private RequestDispatcher getFormPage(UserDao userDao, String pathInfo, HttpServletRequest request) {
		String[] pathParts = pathInfo.split("/");
		String path = getTemplatePath(request.getServletPath(), pathParts[1]);
		
		User result = getUserFromRequestedId(userDao, pathInfo);
		request.setAttribute("user", result);

		return request.getRequestDispatcher(path);
	}
	
	private User getUserFromRequestedId(UserDao userDao, String pathInfo) {
		String[] pathParts = pathInfo.split("/");
		Optional<User> user = Optional.empty();
		User result = null;
		
		if (pathParts.length >= 3) {
			try {
				long userId = Long.parseLong(pathParts[2]);
				user = userDao.find(userId);
			} catch (NumberFormatException e) {
				// TODO: put this warning to logger
				System.out.println("Failed to convert Id to integer");
			}
		}
		
		result = user.isPresent() ? user.get() : new User();
		return result;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		long id = Long.parseLong(request.getParameter("id"));
		
		boolean updated = false;
		User user = new User(id, username, password);
		Injector injector = Guice.createInjector(new ControllerModule());
		
		try (UserDao userDao = injector.getInstance(UserDaoImpl.class)) {
			updated = userDao.save(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String status = updated ? "success" : "fail";
		request.setAttribute("status", status);
		
		// TODO: please repair the page flow after saving either success or fail
		if (updated) {
			response.sendRedirect("users/list");
		} else {
			request.setAttribute("user", user);
			String path = getTemplatePath(request.getServletPath(), "form");
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
			requestDispatcher.forward(request, response);
		}
	}	
}
