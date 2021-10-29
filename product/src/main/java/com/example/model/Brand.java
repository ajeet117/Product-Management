package com.example.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="tb_brands")
public class Brand {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="brandname",length=64)
	private String name;
	@Column(name="brandlogo",length=64)
	private String logo;
	@Column(name="branddescription",length=400)
	private String description;
	public Brand() {
		
	}
	public Brand(String name, String logo, String description) {
		
		this.name = name;
		this.logo = logo;
		this.description = description;
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
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return  name ;
	}
	@Transient
	public String getBrandImagePath()
	{
		if(logo == null || id == null)
			return null;
		return "/brand-photos/" + id + "/" + logo;
	}
	
}
