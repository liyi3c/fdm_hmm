package org.fdm.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.fdm.domain.Auditor;
import org.fdm.domain.Intro_test;
import org.fdm.model.HmmModel;
import org.fdm.util.MathUtiler;
import org.fdm.util.StrHandler;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import exp.check.io.IoExpHandler;

@Component
public class HmmComputer {
	private SessionFactory sessionFactory;
	private String hmmModelFile;
	private String testFile = null;
	
	private HmmModel hmm;
	private ArrayList<Integer> clsMap_cls;
	private ArrayList<String> distc_text;
	private Double cls_cls_prob[][];
	private Double cls_text_prob[][];
	
	private int[][] cls_input;
	private int[] cls_output;
	private ArrayList<Integer> cls_in;
	private ArrayList<String> text_in;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Resource
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setHmmModelFile(String hmmModelFile) {
		this.hmmModelFile = hmmModelFile;
	}

	public void setTestFile(String testFile) {
		this.testFile = testFile;
	}
	
	private void init() {
		clsMap_cls = hmm.getClsMap_cls();
		distc_text = hmm.getDistc_text();
		cls_cls_prob = hmm.getCls_cls_prob();
		cls_text_prob = hmm.getCls_text_prob();
	}

	public void compute(){
		FileWriter textOutWriter = null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			textOutWriter = new FileWriter(new File(hmmModelFile + ".out"));
			fis = new FileInputStream(hmmModelFile);
            ois = new ObjectInputStream(fis);
            hmm = (HmmModel) ois.readObject();
		} catch (IOException ioe) {
			throw new RuntimeException("Err to load Model from file. Details : \r\n" + ioe.getMessage());
		} catch (ClassNotFoundException anfe) {
			throw new RuntimeException("Err to read Model, please check it's valid model. Details : \r\n" + anfe.getMessage());
		} finally {
			IoExpHandler.closeInputStream(ois);
			IoExpHandler.closeInputStream(fis);
		}
		
		init();

		String inputIntro;
		
		if (testFile == null) {
			Session session = sessionFactory.openSession();
			session.beginTransaction();
			Query q1 = session.createQuery("from Intro_test");
			@SuppressWarnings("unchecked")
			List<Intro_test> list_intro = q1.list();	
			try {
			for(int kk=0;kk<list_intro.size();kk++){
				textOutWriter.write("******************test input "+kk+"******************\r\n");
				textOutWriter.write("***my_result***sae_result***text***\r\n");
				inputIntro = list_intro.get(kk).getIntroduction();
				text_in = new ArrayList<String>();
				cls_in = new ArrayList<Integer>();
				StrHandler.introString2array(inputIntro, text_in, cls_in);
				
				hmmCompute(text_in);
				for(int i=0;i<cls_in.size();i++){
					textOutWriter.write("***"+cls_output[i]+"***"+cls_in.get(i)+"***"+text_in.get(i)+"***\r\n");
				}
			}
			} catch (IOException ioe) {
				
			} finally {
				IoExpHandler.closeWriter(textOutWriter);
			}
			
			session.flush();
			session.clear();
			session.getTransaction().commit();
			session.close();
		}
	}
	
	public void computeFromRawToDB(){
		Properties props = new Properties();
	    props.setProperty("sighanCorporaDict", "data");
	    props.setProperty("serDictionary","data/dict-chris6.ser.gz");
	    props.setProperty("inputEncoding", "UTF-8");
	    props.setProperty("sighanPostProcessing", "true");
	    CRFClassifier<CoreLabel> segmenter = new CRFClassifier<CoreLabel>(props);
	    segmenter.loadClassifierNoExceptions("data/ctb.gz", props);
	    
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream(hmmModelFile);
            ois = new ObjectInputStream(fis);
            hmm = (HmmModel) ois.readObject();
		} catch (IOException ioe) {
			throw new RuntimeException("Err to load Model from file. Details : \r\n" + ioe.getMessage());
		} catch (ClassNotFoundException anfe) {
			throw new RuntimeException("Err to read Model, please check it's valid model. Details : \r\n" + anfe.getMessage());
		} finally {
			IoExpHandler.closeInputStream(ois);
			IoExpHandler.closeInputStream(fis);
		}
		
		init();

		String inputIntro;
		String intro_stf_seg;
		
		if (testFile == null) {
			Session session = sessionFactory.openSession();
			session.beginTransaction();
			Query q1 = session.createQuery("from Auditor");
			@SuppressWarnings("unchecked")
			List<Auditor> list_intro = q1.list();	
			for(int kk=0;kk<list_intro.size();kk++){
				StringBuilder strBld = new StringBuilder("");
				inputIntro = list_intro.get(kk).getBackground();
				if(inputIntro.length() <= 3)
					continue;
				intro_stf_seg = segmenter.classifyToString(inputIntro);
				if(intro_stf_seg.length() <= 3)
					continue;
				text_in = new ArrayList<String>(Arrays.asList(intro_stf_seg.split(" ")));
				
				hmmCompute(text_in);
				for(int i=0;i<cls_output.length;i++){
	    			strBld.append(text_in.get(i)).append("\t").append(cls_output[i]).append("\t");
				}
				
				Query q2 = session.createQuery("update Auditor set backgroundSeg = ? where id = ?");
				q2.setParameter(0, strBld.toString());
				q2.setParameter(1, list_intro.get(kk).getId());
				q2.executeUpdate();
				
				if (kk%1000 == 0) {
					System.out.println("Percent: " + (double)kk/list_intro.size());
					session.flush();
					session.clear();
				}
			}
			
			session.flush();
			session.clear();
			session.getTransaction().commit();
			session.close();
		}
	}
	
	private void hmmCompute(ArrayList<String> text_in) {
		int dimension_cls = clsMap_cls.size();
		int N = text_in.size();
		cls_input = new int[N][dimension_cls];
		cls_output = new int[N];
		Double[] temp_prob = new Double[dimension_cls];
		Double[] temp_prob2 = new Double[dimension_cls];
		Double[] temp_prob3 = new Double[dimension_cls];
		for(int i=0;i<dimension_cls;i++){
			temp_prob[i]=1/(double)dimension_cls;
			temp_prob2[i]=0.0;
			temp_prob3[i]=0.0;
			cls_input[0][i]=i;
		}
//		if (clsMap_cls.indexOf(cls_in.get(0)) != -1)
//		temp_prob[clsMap_cls.indexOf(cls_in.get(0))]=1.0;
//		else
//			System.out.println("------ cls_in.get(0) =  " + cls_in.get(0) + 
//					"\n -------clsMap_cls.indexOf(cls_in.get(0)) = " + 
//					clsMap_cls.indexOf(cls_in.get(0)));
		int index;
		int max_arg;
		int i=0;
		for(i=1;i<N-1;i++){
			for(int j=0;j<dimension_cls;j++){
				
				for(int k=0;k<dimension_cls;k++){
					temp_prob2[k] = temp_prob[k]*cls_cls_prob[k][j]*100;
				}
				
				max_arg = MathUtiler.double_max(temp_prob2,dimension_cls);
				if(distc_text.contains(text_in.get(i))){
					index = distc_text.indexOf(text_in.get(i));
					temp_prob3[j] = temp_prob2[max_arg]*(cls_text_prob[j][index]);
				}
				else
					temp_prob3[j] = temp_prob2[max_arg]*0.000001;
				cls_input[i][j]=max_arg;
			}
			for(int k=0;k<dimension_cls;k++){
				temp_prob[k]=temp_prob3[k];
			}

		}
		max_arg = MathUtiler.double_max(temp_prob,dimension_cls);
		cls_output[N-1] = clsMap_cls.get(max_arg);
		int ii = max_arg;
		for(i=N-2;i>=0;i--){
			ii = cls_input[i+1][ii];
			cls_output[i] = clsMap_cls.get(ii);
		}
	}
}
