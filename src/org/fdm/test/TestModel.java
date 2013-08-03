package org.fdm.test;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.fdm.dao.HmmComputer;
import org.fdm.dao.HmmExtractor;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class TestModel {
	private HmmExtractor hmmExtractor;
	
	public HmmExtractor getHmmExtractor(){
		return hmmExtractor;
	}

	@Resource
	public void setHmmExtractor(HmmExtractor hmmExtractor) {
		this.hmmExtractor = hmmExtractor;
	}
	
	
	private HmmComputer hmmComputer;
	
	public HmmComputer getHmmComputer(){
		return hmmComputer;
	}

	@Resource
	public void setHmmComputer(HmmComputer hmmComputer) {
		this.hmmComputer = hmmComputer;
	}

	public static void main(String[] args) throws IOException {
long startTime=System.currentTimeMillis();   //获取开始时间  

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");		
		TestModel u = (TestModel)ctx.getBean("testModel");
		u.getHmmComputer().hmm_compute();
		
long endTime=System.currentTimeMillis(); //获取结束时间  
System.out.println("程序运行时间： "+(endTime-startTime)+"ms"); 

	}
}
