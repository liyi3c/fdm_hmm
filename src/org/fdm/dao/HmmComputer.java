package org.fdm.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.fdm.domain.Intro_test;
import org.fdm.util.MathUtiler;
import org.fdm.util.StrHandler;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
public class HmmComputer {
private SessionFactory sessionFactory;

	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Resource
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void hmm_compute() throws IOException{
		try {
			BufferedReader clsMapReader = new BufferedReader(new FileReader(new File("cls_map.txt")));
			BufferedReader clsClsMapReader = new BufferedReader(new FileReader(new File("cls_cls_map.txt")));
			BufferedReader textClsMapReader = new BufferedReader(new FileReader(new File("text_cls_map.txt")));
			BufferedReader distcTextMapReader = new BufferedReader(new FileReader(new File("distc_text_map.txt")));
			BufferedReader clsClsProbReader= new BufferedReader(new FileReader(new File("cls_cls_prob.txt")));
			BufferedReader textClsProbReader = new BufferedReader(new FileReader(new File("text_cls_prob.txt")));			
			FileWriter textOutWriter = new FileWriter(new File("text_out.txt"));
			
			String input;
			ArrayList<Integer> clsMap_cls = new ArrayList<Integer>();
			ArrayList<Integer> clsMap_count = new ArrayList<Integer>();
			ArrayList<Integer> clsClsMap_cls1 = new ArrayList<Integer>();
			ArrayList<Integer> clsClsMap_cls2 = new ArrayList<Integer>();
			ArrayList<Integer> clsClsMap_count = new ArrayList<Integer>();
			ArrayList<Integer> textClsMap_cls = new ArrayList<Integer>();
			ArrayList<String> textClsMap_text = new ArrayList<String>();
			ArrayList<Integer> textClsMap_count = new ArrayList<Integer>();
			ArrayList<String> distcText = new ArrayList<String>();
			Double cls_cls_prob[][];
			Double cls_text_prob[][];
			
			
			while((input=clsMapReader.readLine())!=null){
				for(int i=0;i<input.length();i++){
					if(input.charAt(i)==':'){
						clsMap_cls.add(Integer.parseInt(input.substring(0, i)));
						clsMap_count.add(Integer.parseInt(input.substring(i+1, input.length())));
					}
						
				}
			}
			while((input=clsClsMapReader.readLine())!=null){
				int seg=0;
				for(int i=0;i<input.length();i++){
					if(input.charAt(i)==','){
						clsClsMap_cls1.add(Integer.parseInt(input.substring(0, i)));
						seg=i+1;
					}else if(input.charAt(i)==':'){
						clsClsMap_cls2.add(Integer.parseInt(input.substring(seg, i)));
						clsClsMap_count.add(Integer.parseInt(input.substring(i+1, input.length())));
					}
						
				}
			}
			while((input=textClsMapReader.readLine())!=null){
				int seg=0;
				for(int i=0;i<input.length();i++){
					if(input.charAt(i)==',' && input.charAt(i+1)!=','){
						textClsMap_text.add(input.substring(0, i));
						seg=i+1;
					}else if(input.charAt(i)==':' && input.charAt(i+1)!=','){
						//System.out.println(f1);
						try{
						textClsMap_cls.add(Integer.parseInt(input.substring(seg, i)));
						textClsMap_count.add(Integer.parseInt(input.substring(i+1, input.length())));
						}catch(Exception e){
							System.out.println(input);
						}
					}
						
				}
			}
			while((input=distcTextMapReader.readLine())!=null){
				distcText.add(input);
			}
			
			//////////////////////////
			
			/////////////////////////
			int num = clsMap_cls.size();
			int num1 = distcText.size();
			cls_cls_prob = new Double[num][num];
			cls_text_prob = new Double[num][num1];
			
			for(int i=0;i<clsMap_cls.size();i++){
				for(int j=0;j<clsMap_cls.size();j++){
					
					cls_cls_prob[i][j]=0.0;
				}
			}
			for(int i=0;i<num;i++){
				for(int j=0;j<num1;j++){
					cls_text_prob[i][j]=0.0;
				}
			}

			String textProb;
			Integer limit=0;
			Double prob = 0.0;
			int lineCount=0;
			while((input=clsClsProbReader.readLine())!=null){
				while(input.contains(",")){
					textProb = input.substring(0, input.indexOf(","));
					input = input.substring(input.indexOf(",")+1, input.length());
					try{
						prob = Double.valueOf(textProb.substring(1, textProb.indexOf(":")));
						limit = Integer.valueOf(textProb.substring(textProb.indexOf(":")+1,textProb.length()-1));
					}catch(NumberFormatException e){
						System.out.println(input);
					}
					cls_cls_prob[lineCount][limit] = prob;							
				}
				lineCount++;
			}
			lineCount=0;
			while((input=textClsProbReader.readLine())!=null){
				while(input.contains(",")){
					textProb = input.substring(0, input.indexOf(","));
					input = input.substring(input.indexOf(",")+1, input.length());
					prob = Double.valueOf(textProb.substring(1, textProb.indexOf(":")));
					limit = Integer.valueOf(textProb.substring(textProb.indexOf(":")+1,textProb.length()-1));
					cls_text_prob[lineCount][limit] = prob;							
				}
				lineCount++;
			}

			String inputIntro;
			Session session = sessionFactory.openSession();
			session.beginTransaction();
			Query q1 = session.createQuery("from Intro_test");
			List<Intro_test> list_intro = q1.list();	
			for(int kk=0;kk<list_intro.size();kk++){
				textOutWriter.write("******************test input "+kk+"******************\r\n");
				textOutWriter.write("***my_result***sae_result***text***\r\n");
				inputIntro = list_intro.get(kk).getIntroduction();
				
				ArrayList<String> text_in = new ArrayList<String>();
				ArrayList<Integer> cls_in = new ArrayList<Integer>();
				StrHandler.intro_string2array(inputIntro, text_in, cls_in);
				int N = cls_in.size();
				int[][] cls_input = new int[N][num];
				int[] cls_output = new int[N];
				Double[] temp_prob = new Double[num];
				Double[] temp_prob2 = new Double[num];
				Double[] temp_prob3 = new Double[num];
				//Double[] temp_prob3 = new Double[num];
				for(int i=0;i<num;i++){
					temp_prob[i]=0.0;
					temp_prob2[i]=0.0;
					temp_prob3[i]=0.0;
					//temp_prob3[i]=0.0;
					cls_input[0][i]=clsMap_cls.indexOf(cls_in.get(0));
				}
				try{
				temp_prob[clsMap_cls.indexOf(cls_in.get(0))]=1.0;
				}catch(ArrayIndexOutOfBoundsException e){
					System.out.println(inputIntro + "\n ------ cls_in.get(0) =  " + cls_in.get(0) + "\n -------clsMap_cls.indexOf(cls_in.get(0)) = " + clsMap_cls.indexOf(cls_in.get(0)));
					System.out.println(num + "  " + num1);
				}
				int index;
				int max_arg;
				int i=0;
				for(i=1;i<N-1;i++){
					for(int j=0;j<num;j++){
						
						for(int k=0;k<num;k++){
							temp_prob2[k] = temp_prob[k]*cls_cls_prob[k][j]*100;
						}
						
						max_arg = MathUtiler.double_max(temp_prob2,num);
						if(distcText.contains(text_in.get(i))){
							index = distcText.indexOf(text_in.get(i));
							temp_prob3[j] = temp_prob2[max_arg]*(cls_text_prob[j][index]);
						}
						else
							temp_prob3[j] = temp_prob2[max_arg]*0.000001;
						cls_input[i][j]=max_arg;
					}
					for(int k=0;k<num;k++){
						temp_prob[k]=temp_prob3[k];
					}

				}
				max_arg = MathUtiler.double_max(temp_prob,num);
				cls_output[N-1] = clsMap_cls.get(max_arg);
				int ii = max_arg;
				for(i=N-2;i>=0;i--){
					ii = cls_input[i+1][ii];
					cls_output[i] = clsMap_cls.get(ii);
				}
//					for(int k=0;k<N-1;k++){
//						for(int j=0;j<num;j++)
//							fwtest.write(cls_input[k][j]+"\t");
//						fwtest.write("\r\n");
//					}
				for(i=0;i<N;i++){
					textOutWriter.write("***"+cls_output[i]+"***"+cls_in.get(i)+"***"+text_in.get(i)+"***\r\n");
				}
			}
				
			clsMapReader.close();
			clsClsMapReader.close();
			textClsMapReader.close();
			distcTextMapReader.close();
			clsClsProbReader.close();
			textClsProbReader.close();
			textOutWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
