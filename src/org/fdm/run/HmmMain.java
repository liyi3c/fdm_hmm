package org.fdm.run;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.Resource;

import org.fdm.dao.HmmComputer;
import org.fdm.dao.HmmTrainer;
import org.fdm.dao.HmmTrainerData;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class HmmMain {
	private final static boolean DEBUG = true;
	private HmmTrainer hmmTrainer;
	private HmmComputer hmmComputer;
	private HmmTrainerData hmmData;
	
	public HmmTrainer getHmmTrainer(){
		return hmmTrainer;
	}

	@Resource
	public void setHmmTrainer(HmmTrainer hmmTrainer) {
		this.hmmTrainer = hmmTrainer;
	}
	
	public HmmComputer getHmmComputer(){
		return hmmComputer;
	}

	@Resource
	public void setHmmComputer(HmmComputer hmmComputer) {
		this.hmmComputer = hmmComputer;
	}

	public HmmTrainerData getHmmData() {
		return hmmData;
	}

	@Resource
	public void setHmmData(HmmTrainerData hmmData) {
		this.hmmData = hmmData;
	}

	public static void main(String[] args) throws IOException {
		long startTime=System.currentTimeMillis();   //获取开始时间  
		HmmComputer hmmCpt;
		HmmTrainer hmmTrn;
		HmmTrainerData hmmData;
		Properties config;
		FileInputStream conFis = null;
		String mode;
		
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
		HmmMain hmm = (HmmMain)ctx.getBean("hmmMain");
		if (DEBUG) {
//			hmmData = hmm.getHmmData();
//			//hmmData.getPriorToken(999, "Position");
//			hmmData.addPriorTokenTag(888);
//			hmmTrn = hmm.getHmmTrainer();
//			hmmTrn.setModelName("1.s");
//			hmmTrn.genHmmModel();
			
			hmmCpt = hmm.getHmmComputer();
			hmmCpt.setHmmModelFile("1.s");
			hmmCpt.compute();
		}
		else {
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
		}
		long endTime=System.currentTimeMillis(); //获取结束时间  
		System.out.println("程序运行时间： "+(endTime-startTime)+"ms"); 
	}
}
