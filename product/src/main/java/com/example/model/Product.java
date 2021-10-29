package com.example.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 64)
	private String name;
	@Column(length = 64)
	private String madeIn;
	
	private float price;
	@Column(length = 256)
	private String details;
	@Column(length = 64)
	private String image;
	@Column(length = 10)
	private int stock;
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "brand_id")
	private Brand brand;

	public Product() {

	}

	public Product(String name, String madeIn, float price, String details, String image, int stock, Brand brand) {

		this.name = name;
		this.madeIn = madeIn;
		this.price = price;
		this.details = details;
		this.image = image;
		this.stock = stock;
		this.brand = brand;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMadeIn() {
		return madeIn;
	}

	public void setMadeIn(String madeIn) {
		this.madeIn = madeIn;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	@Transient
	public String getProductImagePath()
	{
		if(image == null || id == null)
		return null;
		
		return "/product-photos/" + id +"/" + image;
	}
}
