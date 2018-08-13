package com.java;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class ImageServlet
 */
@WebServlet("/image")
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private EntityManager em;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String id=request.getParameter("id");
			Query query = em.createQuery("SELECT i.foto FROM Item i WHERE i.id = :id");
				query.setParameter("id", Long.parseLong(id));
			byte[] foto = (byte[])query.getSingleResult();
			
			response.reset();
			response.getOutputStream().write(foto);
			
		} catch (Exception e) {
			throw new ServletException(e.getMessage());
		}
		
	}

}
