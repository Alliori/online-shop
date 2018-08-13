package com.java;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

import com.java.model.Customer;

@Named
@SessionScoped
public class SignInController implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private EntityManager em;

	@Resource
	private UserTransaction userTrans;

	@Inject
	private Customer customer;

	private String email;
	private String password;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String find() {
		try {

			TypedQuery<Customer> query = em.createQuery(
					"SELECT c FROM Customer c " + "WHERE c.email= :email " + "AND c.password= :password ",
					Customer.class);

			query.setParameter("email", email);
			query.setParameter("password", password);
			List<Customer> list = query.getResultList();

			if (list != null && list.size() > 0) {
				customer = list.get(0);

				FacesMessage message = new FacesMessage(" Succesfully signed in under id  " + customer.getEmail());
				FacesContext.getCurrentInstance().addMessage("signInForm", message);
			}

		} catch (Exception e) {
			Logger.getLogger(SignInController.class.getCanonicalName()).log(Level.WARN, "Error : " + e.getMessage());

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN + e.getMessage(),
					e.getCause().getMessage());
			FacesContext.getCurrentInstance().addMessage("signInForm", message);

		}
		return "signin";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
