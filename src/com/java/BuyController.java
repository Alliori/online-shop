package com.java;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.Resource;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import com.java.model.Customer;
import com.java.model.Item;

@Named
@RequestScoped
public class BuyController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private EntityManager em;
	
	@Resource
	private UserTransaction userTransaction;
	
	public void update(Long id) {
		try {
			userTransaction.begin();
			Item item=em.find(Item.class, id);
			item.setBuyer(getCustomer());
			item.setSold(new Date());
			em.merge(item);
			userTransaction.commit();
		} catch (Exception e) {
			System.out.println("Error : "+e.getMessage());
		}
	}
	
	private Customer getCustomer() {
		FacesContext context=FacesContext.getCurrentInstance();
		ELContext elContext=context.getELContext();
		ELResolver elResolver=context.getApplication().getELResolver();
		SignInController signinController =(SignInController) elResolver.getValue(elContext, null, "signInController");
		return signinController.getCustomer();
	}
	

}
