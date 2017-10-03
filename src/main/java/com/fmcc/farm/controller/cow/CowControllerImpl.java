package com.fmcc.farm.controller.cow;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fmcc.farm.dto.CowDTO;
import com.fmcc.farm.mappers.cow.CowMapper;
import com.fmcc.farm.model.Cow;
import com.fmcc.farm.service.cow.CowService;
import com.fmcc.farm.service.user.UserService;

@RestController
@RequestMapping(value="user/{user_id}/animal/cow")
public class CowControllerImpl implements CowController{
	
	@Autowired
	private CowService cowService;
	
	@Autowired
	private CowMapper cowMapper;
	
	@Autowired
	private UserService userService;

	@Override
	@RequestMapping(method = RequestMethod.POST)
	public CowDTO create(@RequestBody CowDTO t, 
							@PathVariable(name="user_id") Integer userId) {
		final Cow c = cowService.create(cowMapper.map(t));
		userService.addNewAnimal(c, userId);
		return cowMapper.map(c);
	}

	@Override
	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED,
					reason = "Delete request not allowed.")
	public void delete(@RequestBody CowDTO t) {
		//Nothing to do.		
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED,
					reason = "Delete request not allowed.")
	public void deleteWithId(@RequestBody CowDTO t) {
		//Nothing to do.		
	}

	@Override
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public void update(@RequestBody CowDTO t, 
						@PathVariable(name="user_id") Integer userId,
						@PathVariable(name="id") Integer id) {
		if(t.getId() != null) {
			if(t.getId().equals(id)) {
				final Cow c = cowMapper.map(t);
				cowService.update(c);
				userService.addNewAnimal(c, userId);
			} else {
				throw new NullPointerException();
			}
		} else {
			throw new NullPointerException();
		}
		final Cow c = cowMapper.map(t);
		cowService.update(c);
		userService.addNewAnimal(c, userId);	
	}

	@Override
	@RequestMapping(method = RequestMethod.GET)
	public List<CowDTO> getAll(@RequestParam(name="page",defaultValue="1") Integer page,
								@RequestParam(name="size",defaultValue="5") Integer size,
								@PathVariable(name="user_id") Integer userId) {
		final List<CowDTO> dtos = new ArrayList<>();
		if(page > 0 && size < 11 && size>0) {
			cowService.getAll(userId,page-1,size).forEach(c -> {
				dtos.add(cowMapper.map(c));
			});
		}
		return dtos;
	}

	@Override
	@RequestMapping(method = RequestMethod.GET,value = "/{id}")
	public CowDTO findById(@PathVariable Integer id) {
		return cowMapper.map(cowService.findById(id));
	}

}