package com.fmcc.farm.service.chicken;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.fmcc.farm.dao.ChickenDAO;
import com.fmcc.farm.model.Chicken;
import com.fmcc.farm.model.Production;
import com.fmcc.farm.validators.pagesize.PageAndSizeValidator;

@Service
public class ChickenServiceImpl implements ChickenService{
	
	@Autowired
	private ChickenDAO dao;
	
	@Autowired
	private PageAndSizeValidator pageAndSizeValidator;
	
	@Override
	public Chicken create(Chicken t) {	
		return dao.save(t);
	}

	@Override
	public void update(Chicken t) {
		dao.save(t);
	}

	@Override
	public List<Chicken> getAll(Integer userId, Integer page, Integer size) {
		final List<Chicken> chickens = new ArrayList<>();
		if(pageAndSizeValidator.validatePageAndSize(page, size)) {
			dao.findAllByUserId(userId, new PageRequest(page-1, size)).forEach(chicken -> {
				chickens.add(chicken);
			});
		}
		return chickens;
	}

	@Override
	public Chicken findById(Integer id) {
		return dao.findOne(id);
	}

	@Override
	public void addNewProduction(Production p, Integer animalId) {
		Chicken c = findById(animalId);
		final List<Production> productions = c.getProductions();
		productions.add(p);
		c.setProductions(productions);
		update(c);
	}
	
	@Override
	public List<Chicken> findAllByUserId(Integer userId, Integer page, Integer size) {
		final List<Chicken> chickens = new ArrayList<>();
		if(pageAndSizeValidator.validatePageAndSize(page, size)) {
			dao.findAllByUserId(userId, new PageRequest(page-1,size)).forEach(c -> {
				chickens.add(c);
			});
		}
		return chickens;
	}

}
