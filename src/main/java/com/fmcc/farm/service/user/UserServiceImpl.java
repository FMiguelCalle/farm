package com.fmcc.farm.service.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.fmcc.farm.dao.UserDAO;
import com.fmcc.farm.model.Animal;
import com.fmcc.farm.model.User;
import com.fmcc.farm.validators.pagesize.PageAndSizeValidator;


@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDAO dao;
	
	@Autowired
	private PageAndSizeValidator pageAndSizeValidator;
	
	@Override
	public User create(User t) {
		return dao.save(t);	
	}   

	@Override
	public void delete(User t) {
		dao.delete(t);
	}

	@Override
	public void update(User t) {
		dao.save(t);
	}

	@Override
	public List<User> getAll(Integer page, Integer size) {
		final List<User> users = new ArrayList<>();
		if(pageAndSizeValidator.validatePageAndSize(page, size)) {
			dao.findAll(new PageRequest(page-1, size)).forEach(c -> users.add(c));
		}
		return users;
	}

	@Override
	public User findById(Integer id) {
		return dao.findOne(id);
	}

	@Override
	public void addNewAnimal(Animal a, Integer userId) {
		User u = findById(userId);
		final List<Animal> animals = u.getAnimals();
		animals.add(a);
		u.setAnimals(animals);
		update(u);		
	}


}
