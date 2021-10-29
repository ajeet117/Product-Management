package com.example.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.model.Brand;
import com.example.model.Product;
import com.example.service.BrandService;
import com.example.service.ProductService;
import com.example.utility.FileUploadUtil;



@Controller
public class ProductController {

	@Autowired
	private BrandService brandService;

	@Autowired
	private ProductService productService;
	@RequestMapping("/product/create")
	public String createProduct(Model model)
	{
		List<Brand> allBrands=brandService.findAll();
		model.addAttribute("allBrands",allBrands);
		model.addAttribute("title","Product-Add");
		model.addAttribute("product",new Product());
		return "product_form";
	}
	
	
	
	@RequestMapping(value="/product/save",method = RequestMethod.POST)
	public String saveProudct(@ModelAttribute("product") Product product, @RequestParam("productlogo") MultipartFile multiPartFile) throws IOException {
		
		String fileName=StringUtils.cleanPath(multiPartFile.getOriginalFilename());
		product.setImage(fileName);
		Product savedProduct=productService.save(product);
		String uploadDir="product-photos/" + savedProduct.getId();
		FileUploadUtil.saveFile(uploadDir, fileName, multiPartFile);
		return "redirect:/product/view";
	}
	@RequestMapping(value="/product/view")
	public String viewProduct(Model model)
	{
		String keyword=null;
		return pageView(1,model,keyword);
	}
	@RequestMapping("/product/page/{pagenum}")
	public String pageView(@PathVariable("pagenum") int pagenum, Model model,@Param("keyword") String keyword)
	{
		model.addAttribute("title","Product-View");
		Page<Product> page=productService.listAll(pagenum,keyword);
		List<Product> allProduct=page.getContent();
		if(allProduct.isEmpty())
		{
			model.addAttribute("error", "No object found");
		}
		model.addAttribute("allProduct",allProduct);
		model.addAttribute("totalpage",page.getTotalPages());
		model.addAttribute("currentpage",pagenum);
		model.addAttribute("keyword",keyword);
		return "view_product";
	}
	@RequestMapping("/product/delete/{id}")
	public String deleteProduct(@PathVariable("id") int id)
	{
		productService.deleteById(id);
		return "redirect:/product/view";
	}
	@RequestMapping("/product/edit")
	public String editProduct(@Param("id") int id, Model model)
	{
		Product product=productService.findById(id);
		List<Brand> allBrands=brandService.findAll();
		model.addAttribute("allBrands",allBrands);
		model.addAttribute("product",product);
		return "product_form";
	}
}
