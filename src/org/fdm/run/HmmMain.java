package org.fdm.run;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.Resource;

import org.fdm.dao.HmmComputer;
import org.fdm.dao.HmmTrainer;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class HmmMain {
	private HmmTrainer hmmTrainer;
	
	public HmmTrainer getHmmTrainer(){
		return hmmTrainer;
	}

	@Resource
	public void setHmmTrainer(HmmTrainer hmmTrainer) {
		this.hmmTrainer = hmmTrainer;
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
		HmmComputer hmmCpt;
		HmmTrainer hmmTrn;
		Properties config;
		FileInputStream conFis = null;
		String mode;
		
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
		HmmMain hmm = (HmmMain)ctx.getBean("hmmMain");
		if (args.length < 1 || args.length > 2)
			System.out.println("Usage: java -jar FdmHmm.jar xxx.properties [testFile]");
		else if (args.length == 1) {
			conFis = new FileInputStream(new File(args[0]));
			config = new Properties();
			config.load(conFis);
			mode = config.getProperty("MODE");
			
			if(mode.equalsIgnoreCase("compute")) {
				hmmCpt = hmm.getHmmComputer();
				hmmCpt.setHmmModelFile(config.getProperty("MODEL"));
				hmmCpt.compute();
			} else if (mode.equalsIgnoreCase("train")) {
				hmmTrn = hmm.getHmmTrainer();
				hmmTrn.setModelName(config.getProperty("TRAIN.OUT"));
				hmmTrn.genHmmModel();
			} else {
				System.out.println("Invalid Mode!!!");
			}
			
		} else if (args.length == 2) {
			conFis = new FileInputStream(new File(args[0]));
			config = new Properties();
			config.load(conFis);
			mode = config.getProperty("MODE");
			
			if(mode.equalsIgnoreCase("compute")) {
				hmmCpt = hmm.getHmmComputer();
				hmmCpt.setHmmModelFile(config.getProperty("MODEL"));
				hmmCpt.setTestFile(args[1]);
				hmmCpt.compute();
			} else if (mode.equalsIgnoreCase("train")) {
				hmmTrn = hmm.getHmmTrainer();
				hmmTrn.setModelName(config.getProperty("TRAIN.OUT"));
				hmmTrn.genHmmModel();
			} else {
				System.out.println("Invalid Mode!!!");
			}
		}
		
		long endTime=System.currentTimeMillis(); //获取结束时间  
		System.out.println("程序运行时间： "+(endTime-startTime)+"ms"); 
	}
}
