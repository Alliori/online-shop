package com.java.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.Date;


/**
 * The persistent class for the item database table.
 * 
 */

@Entity

@Table(name="item")
@NamedQuery(name="Item.findAll", query="SELECT i FROM Item i")
public class Item implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ITEM_ID_GENERATOR", sequenceName="SEQ_ITEM",
			allocationSize=1,
			initialValue=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ITEM_ID_GENERATOR")
	@Column(unique=true, nullable=false)
	private Long id;

	@NotNull
	private String description;

	private byte[] foto;

	@NotNull
	private Double price;

	@Temporal(TemporalType.TIMESTAMP)
	private Date sold;

	@NotNull
	private String title;

	//bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumn(name="buyer_id")
	private Customer buyer;

	//bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumn(name="seller_id", nullable=false)
	private Customer seller;

	public Item() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte[] getFoto() {
		return this.foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getSold() {
		return this.sold;
	}

	public void setSold(Date sold) {
		this.sold = sold;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Customer getBuyer() {
		return this.buyer;
	}

	public void setBuyer(Customer buyer) {
		this.buyer = buyer;
	}

	public Customer getSeller() {
		return this.seller;
	}

	public void setSeller(Customer seller) {
		this.seller = seller;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Item  "+ title;
	}

}