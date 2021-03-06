package com.fmcc.farm.service.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.fmcc.farm.dao.UserDAO;
import com.fmcc.farm.dto.StatsUserEarningDTO;
import com.fmcc.farm.model.Animal;
import com.fmcc.farm.model.User;
import com.fmcc.farm.validators.dtoidpathid.PathIdAndDTOIdMatchValidator;
import com.fmcc.farm.validators.notnull.NotNullValidator;
import com.fmcc.farm.validators.pagesize.PageAndSizeValidator;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDAO dao;
	
	@Autowired
	private PageAndSizeValidator pageAndSizeValidator;
	
	@Autowired
	private PathIdAndDTOIdMatchValidator idValidator;
	
	@Autowired
	private NotNullValidator notNullValidator;
	
	@Override
	public User create(User t) {
		return dao.save(t);	
	}   

	@Override
	public void delete(User t, Integer pathId) throws NullPointerException{
		if(idValidator.validateMatchingIds(t.getId(), pathId) &&
				notNullValidator.validateNotNull(findById(pathId))) {
			dao.delete(t);
		}
	}

	@Override
	public void update(User t, Integer pathId) throws NullPointerException {
		if(idValidator.validateMatchingIds(t.getId(), pathId) &&
				notNullValidator.validateNotNull(findById(pathId))) {
			dao.save(t);
		}
	}

	@Override
	public List<User> getAll(Integer page, Integer size) throws NullPointerException{
		final List<User> users = new ArrayList<>();
		if(pageAndSizeValidator.validatePageAndSize(page, size)) {
			dao.findAll(new PageRequest(page-1, size)).forEach(c -> {	
				users.add(c);
			});
		}
		return users;
	}

	@Override
	public User findById(Integer id) {
		User user = dao.findOne(id);
		return user;
	}

	@Override
	public void addNewAnimal(Animal a, Integer userId) throws NullPointerException{
		User u = findById(userId);
		if(notNullValidator.validateNotNull(u)) {
			final List<Animal> animals = u.getAnimals();
			animals.add(a);
			u.setAnimals(animals);
			update(u,userId);		
		}
	}

	@Override
	public List<StatsUserEarningDTO> usersEarningsBetweenDates(Date fromDate, Date toDate, Integer page, Integer size) {
		List<StatsUserEarningDTO> dtos = new ArrayList<>();
		if(pageAndSizeValidator.validatePageAndSize(page, size)) {
			dao.usersEarningsBetweenDates(fromDate, toDate, new PageRequest(page-1, size)).forEach(e -> {
				dtos.add(e);
			});
		}
		return dtos;
	}

}