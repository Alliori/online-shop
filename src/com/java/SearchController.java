package com.java;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

import com.java.model.Item;

@Named
@RequestScoped
public class SearchController implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private EntityManager em;

	private List<Item> items;

	public List<Item> getItems() {
		items = findAll();
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public List<Item> findAll() {
		try {
			TypedQuery<Item> query = em.createNamedQuery("Item.findAll", Item.class);
			return query.getResultList();
		} catch (Exception e) {
			Logger.getLogger(SearchController.class.getCanonicalName()).log(Level.WARN, "Error : " + e.getMessage());
		}
		return new ArrayList<>();
	}

}
