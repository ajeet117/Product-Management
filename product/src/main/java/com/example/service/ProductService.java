package com.example.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.model.Product;
import com.example.repository.ProductRepository;

@Service
@Transactional
public class ProductService {

	@Autowired
	private ProductRepository productRepo;
	
	public List<Product> findAll()
	{
		return productRepo.findAll();
	}
	public Page<Product> listAll(int pageNum,String keyword)
	{
		int maxpage=5;
		Pageable pageable=PageRequest.of(pageNum-1, maxpage);
		if(keyword != null)
		{
			return productRepo.findAll(keyword,pageable);
		}
		return productRepo.findAll(pageable);
	}
	public Product save(Product product)
	{
	 return productRepo.save(product);
	}
	
	public void deleteById(int id)
	{
		productRepo.deleteById(id);
	}
	
	public Product findById(int id)
	{
		return productRepo.findById(id).get();
	}
}
