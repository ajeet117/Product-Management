package com.example.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.model.Brand;
import com.example.repository.BrandRepository;

@Service
@Transactional
public class BrandService {

	@Autowired
	private BrandRepository repo;
	
	public List<Brand> findAll()
	{
		return repo.findAll();
	}
	public Page<Brand> listAll(int pagenum)
	{
		int maxpage=5;
		Pageable pageable=PageRequest.of(pagenum-1, maxpage);
		return repo.findAll(pageable);
		
	}
	public Brand save(Brand brand)
	{
		return repo.save(brand);
	}
	public void deleteById(Integer id)
	{
		repo.deleteById(id);
	}
	public Brand getById(Integer id)
	{
		return repo.findById(id).get();
	}
}
