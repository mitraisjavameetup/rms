package com.mitrais.rms.controller;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServlet;

public abstract class AbstractController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static final String VIEW_PREFIX = "/WEB-INF/jsp";
	public static final String VIEW_SUFFIX = ".jsp";

	protected String getTemplatePath(String... paths) {
		Path url = Paths.get(paths[0]);
		for (int i = 1; i < paths.length; i++) {
			url = url.resolve(paths[i]);
		}

		if (url.toString().equalsIgnoreCase("/")) {
			return VIEW_PREFIX + url.toString() + "index" + VIEW_SUFFIX;
		} else {
			return VIEW_PREFIX + url.toString() + VIEW_SUFFIX;
		}
	}
}
