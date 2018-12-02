package com.cnpc.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cnpc.test.entity.Person;
import com.cnpc.test.service.TestService;

@RestController
@RequestMapping("test.do")
public class TestController {

	@Autowired
	TestService testService;
	@RequestMapping(params="method=test")
	public List<Person> test(){
		return testService.getNames("1");
	}
}
