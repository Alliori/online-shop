package com.java;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

import com.java.model.Customer;

@Named
@RequestScoped
public class RegisterController implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private EntityManager em;

	@Resource
	private UserTransaction userTrans;

	@Inject
	private Customer customer;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String persist() {
		try {
			userTrans.begin();
			em.persist(customer);
			userTrans.commit();
			Logger.getLogger(RegisterController.class.getCanonicalName()).log(Level.INFO, "Saving : " + customer);

			FacesMessage message = new FacesMessage(" Succesfully registered  " + customer.getEmail());
			FacesContext.getCurrentInstance().addMessage("registerForm", message);

		} catch (Exception e) {
			Logger.getLogger(RegisterController.class.getCanonicalName()).log(Level.WARN, "Error : " + e.getMessage());

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN + e.getMessage(),
					e.getCause().getMessage());
			FacesContext.getCurrentInstance().addMessage("registerForm", message);

		}
		return "register";
	}

}
