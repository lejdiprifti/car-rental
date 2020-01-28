package com.ikubinfo.rental.service;

import java.io.IOException;
import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ikubinfo.rental.converter.CategoryConverter;
import com.ikubinfo.rental.entity.CategoryEntity;
import com.ikubinfo.rental.model.CategoryModel;
import com.ikubinfo.rental.repository.CategoryRepository;
import com.ikubinfo.rental.security.JwtTokenUtil;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository catRepository;
	
	@Autowired
	private CategoryConverter catConverter;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	public CategoryService() {
		
	}
	
	public List<CategoryModel> getAll() {
		return catConverter.toModel(catRepository.getAll());
	}
	
	public CategoryModel getById(Long id) {
		try {
			return catConverter.toModel(catRepository.getById(id));
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
		}
	}
	
	public void save(CategoryModel model) throws IOException {
		if (jwtTokenUtil.getRole().get("id") == "1") {
		try {
			checkIfExists(model.getName());
			CategoryEntity entity = new CategoryEntity();
			entity.setName(model.getName());
			if (model.getPhoto() != null) {
			entity.setPhoto(model.getPhoto().getBytes());
			}
			entity.setActive(true);
			entity.setDescription(model.getDescription());
			catRepository.save(entity);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category already exists.");
		}
		}else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to perform this action.");
		}
	}
	
	public void edit(CategoryModel model) throws IOException {
		if (jwtTokenUtil.getRole().get("id") == "1") {
		try {
			CategoryEntity entity = catRepository.getById(model.getId());
			if (model.getName() != null) {
				try {
					catRepository.getByName(model.getName());
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category already exists.");
				} catch (NoResultException e) {
					entity.setName(model.getName());
				}
			}
			if (model.getDescription() != null) {
				entity.setDescription(model.getDescription());
			}
			if (model.getPhoto() != null) {
				entity.setPhoto(model.getPhoto().getBytes());
			}
		} catch (NoResultException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
		}
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to perform this action.");
		}
	}
	
	public void delete(Long id) {
		if (jwtTokenUtil.getRole().get("id") == "1") {
			try {
				CategoryEntity entity = catRepository.getById(id);
				entity.setActive(false);
				catRepository.edit(entity);
			} catch (NoResultException e) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
			}
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to perform this action.");
		}
	}
	
	public void checkIfExists(String name) throws Exception{
		try {
			catRepository.getByName(name);
			throw new Exception("Category already exists.");
		} catch (NoResultException e) {
			
		}
	}
	
	
}
