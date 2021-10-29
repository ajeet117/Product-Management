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
import com.example.service.BrandService;
import com.example.utility.FileUploadUtil;



@Controller
public class BrandController {

	@Autowired
	private BrandService service;
	@RequestMapping("/brand/add")
	public String addBrand(Model model)
	{
		model.addAttribute("title","Brand-Add");
		model.addAttribute("brand",new Brand());
		return "brand_form";
	}
	
	@RequestMapping(value="/brand/save",method = RequestMethod.POST)
	public String saveBrand(@ModelAttribute("brand") Brand brand,@RequestParam("brandlogo") MultipartFile multipartFile) throws IOException
	{
		String fileName=StringUtils.cleanPath(multipartFile.getOriginalFilename());
		brand.setLogo(fileName);
		Brand saveBrand=service.save(brand);
		String uploadDir="brand-photos/" + saveBrand.getId();
		FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
		return "redirect:/brand/view";
	}
	
	@RequestMapping("/brand/view")
	public String viewBrand(Model model)
	{
		
		return viewPage(1,model);
	}
	@RequestMapping("/brand/page/{pagenum}")
	public String viewPage(@PathVariable("pagenum") int pagenum,Model model)
	{
		Page<Brand> page=service.listAll(pagenum);
		List<Brand> allBrand=page.getContent();
		model.addAttribute("title","Brand-View");
		model.addAttribute("allBrand",allBrand);
		model.addAttribute("totalpage",page.getTotalPages());
		model.addAttribute("currentpage",pagenum);
		return "view_brand";
	}
	
	@RequestMapping("/brand/delete/{id}")
	public String deleteBrand(@PathVariable("id") Integer id)
	{
		service.deleteById(id);
		return "redirect:/brand/view";
	}
	@RequestMapping("/brand/edit")
	public String editBrand(@Param("id") Integer id,Model model)
	{
		Brand brand=service.getById(id);
		model.addAttribute("brand",brand);
		
		return "brand_form";
	}
}
