package org.fdm.dao;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.fdm.domain.Com_tokens;
import org.fdm.domain.Intro_learn;
import org.fdm.domain.Intro_seg;
import org.fdm.domain.Intro_test;
import org.fdm.util.MathUtiler;
import org.fdm.util.StrHandler;
import org.fdm.util.TagHandler;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
public class HmmExtractor {
	private SessionFactory sessionFactory;

	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Resource
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
//	private class cls_count{
//		public Integer cls;
//		public Integer count;
//		public cls_count(String s){
//			for(int i=0;i<s.length();i++){
//				if(s.charAt(i)==':'){
//					cls = Integer.parseInt(s.substring(0, i));
//					count = Integer.parseInt(s.substring(i+1, s.length()));
//				}
//			}
//		}
//	}
	private class text_cls{
		public String text;
		public Integer cls;
		public String getText(){
			return text;
		}
		public Integer getCls(){
			return cls;
		}
		public int hashCode() {   
			  int result = 17;  //任意素数   
			  result = 31*result +text.hashCode(); //c1,c2是什么看下文解释   
			  result = 31*result +cls;   
			  return result;   
		}  
		public boolean equals(Object other)  
	    {  
	        if(this == other)  
	        {  
	            //如果引用地址相同，即引用的是同一个对象，就返回true  
	            return true;  
	        }  
	  
	            //如果other不是Cat类的实例，返回false  
	        if(!(other instanceof text_cls))  
	        {  
	            return false;  
	        }  
	  
	        final text_cls tc = (text_cls)other;  
	            //name值不同，返回false  
	        if(!text.equals(tc.text))
	            return false;  
	            //birthday值不同，返回false  
	        if(!cls.equals(tc.cls))  
	            return false;  
	  
	        return true;  
	    }
	}
	
	private class cls_cls{
		public Integer cls1;
		public Integer cls2;

		public Integer getCls1(){
			return cls1;
		}
		public Integer getCls2(){
			return cls2;
		}
		public int hashCode() {   
			  int result = 17;  //任意素数   
			  result = 31*result +cls1; //c1,c2是什么看下文解释   
			  result = 31*result +cls2;   
			  return result;   
		}
		public boolean equals(Object other)  
	    {  
	        if(this == other)  
	        {  
	            //如果引用地址相同，即引用的是同一个对象，就返回true  
	            return true;  
	        }  
	  
	            //如果other不是Cat类的实例，返回false  
	        if(!(other instanceof cls_cls))  
	        {  
	            return false;  
	        }  
	  
	        final cls_cls tc = (cls_cls)other;  
	            //name值不同，返回false  
	        if(!cls1.equals(tc.cls1))
	            return false;  
	            //birthday值不同，返回false  
	        if(!cls2.equals(tc.cls2))  
	            return false;  
	  
	        return true;  
	    }
	}
	
// create prob mats from text-cls maps
	public void create_prob_mat() throws IOException{
		try {
			BufferedReader clsMapReader = new BufferedReader(new FileReader(new File("cls_map.txt")));
			BufferedReader clsClsMapReader = new BufferedReader(new FileReader(new File("cls_cls_map.txt")));
			BufferedReader textClsMapReader = new BufferedReader(new FileReader(new File("text_cls_map.txt")));
			BufferedReader distcTextMapReader = new BufferedReader(new FileReader(new File("distc_text_map.txt")));
			FileWriter clsClsProbWriter = new FileWriter(new File("cls_cls_prob.txt"));
			FileWriter textClsProbWriter = new FileWriter(new File("text_cls_prob.txt"));			
			
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
			int cls1;
			int cls2;
			int pos1;
			int pos2;
			String text;
			int cls;
			for(int i=0;i<clsClsMap_cls1.size();i++){
				cls1 = clsClsMap_cls1.get(i);
				cls2 = clsClsMap_cls2.get(i);
				pos1 = clsMap_cls.indexOf(cls1);
				pos2 = clsMap_cls.indexOf(cls2);
				cls_cls_prob[pos1][pos2] = ((double)clsClsMap_count.get(i)/(double)clsMap_count.get(pos1));
			}
			for(int i=0;i<textClsMap_cls.size();i++){
				cls = textClsMap_cls.get(i);
				text = textClsMap_text.get(i);
				pos1 = clsMap_cls.indexOf(cls);
				pos2 = distcText.indexOf(text);
				if(pos2<0)
					continue;
				cls_text_prob[pos1][pos2] = ((double)textClsMap_count.get(i)/(double)clsMap_count.get(pos1));
			}
			for(int i=0;i<clsMap_cls.size();i++){
				for(int j=0;j<clsMap_cls.size();j++){
					if(cls_cls_prob[i][j]!=0.0){
						clsClsProbWriter.write("("+cls_cls_prob[i][j].toString()+":"+j+")");
						clsClsProbWriter.write(",");
					}
				}
				clsClsProbWriter.write("\r\n");
			}
			for(int i=0;i<clsMap_cls.size();i++){
				for(int j=0;j<distcText.size();j++){
					if(cls_text_prob[i][j]!=0.0){
						textClsProbWriter.write("("+cls_text_prob[i][j].toString()+":"+j+")");
						textClsProbWriter.write(",");
					}
				}
				textClsProbWriter.write("\r\n");
			}
			
			clsMapReader.close();
			clsClsMapReader.close();
			textClsMapReader.close();
			distcTextMapReader.close();
			clsClsProbWriter.close();
			textClsProbWriter.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
// create files to store transition maps and counts
	public void create_text_maps(){
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Query q1 = session.createQuery("from Intro_learn");
		List<Intro_learn> list_intro = q1.list();
		Intro_learn intro = new Intro_learn();
		ArrayList<String> list_text = new ArrayList<String>();
		ArrayList<Integer> list_cls = new ArrayList<Integer>();
		// record the text sequence for future text_cls_prob
		ArrayList<String> distc_text = new ArrayList<String>();
		HashMap<Integer , Integer> cls_map = new HashMap<Integer , Integer>(); 
		HashMap<cls_cls , Integer> cls_cls_map = new HashMap<cls_cls , Integer>(); 
		HashMap<text_cls , Integer> text_cls_map = new HashMap<text_cls , Integer>(); 
		
		String introduction;
		text_cls tc;
		cls_cls cc;
		for(int i=0;i<list_intro.size();i++){
			intro = list_intro.get(i);
			introduction = intro.getIntroduction();
			list_text.clear();
			list_cls.clear();
			try{
			StrHandler.intro_string2array(introduction, list_text,  list_cls);
			}catch(Exception e){
				System.out.println(introduction);
			}
			for(int j=0;j<list_text.size()-1;j++){
				tc = new text_cls();
				cc = new cls_cls();
				tc.text=list_text.get(j);
				tc.cls=list_cls.get(j);
				cc.cls1=list_cls.get(j);
				cc.cls2=list_cls.get(j+1);
				if(cls_map.containsKey(list_cls.get(j)))
					cls_map.put(list_cls.get(j), cls_map.get(list_cls.get(j))+1);
				else
					cls_map.put(list_cls.get(j),1);
				if(text_cls_map.containsKey(tc))
					text_cls_map.put(tc, text_cls_map.get(tc)+1);
				else
					text_cls_map.put(tc, 1);
				if(cls_cls_map.containsKey(cc))
					cls_cls_map.put(cc, cls_cls_map.get(cc)+1);
				else
					cls_cls_map.put(cc, 1);
			}
		}
		try {
			FileWriter clsMapWriter = new FileWriter("cls_map.txt");
			FileWriter clsClsMapWriter = new FileWriter("cls_cls_map.txt");
			FileWriter textClsMapWriter = new FileWriter("text_cls_map.txt");
			FileWriter distcTextMapWriter = new FileWriter("distc_text_map.txt");
			Iterator<Entry<Integer , Integer>> iter = cls_map.entrySet().iterator(); 
			Entry<Integer , Integer> entry1;
			while (iter.hasNext()) { 
			    entry1 = iter.next(); 
			    //System.out.println(entry.getKey()+":"+entry.getValue());
			    clsMapWriter.write(entry1.getKey()+":"+entry1.getValue());
			    clsMapWriter.write("\r\n");
			} 
			Iterator<Entry<cls_cls , Integer>> iter2 = cls_cls_map.entrySet().iterator(); 
			Entry<cls_cls , Integer> entry2;
			while (iter2.hasNext()) { 
			    entry2 = iter2.next(); 
			    clsClsMapWriter.write((entry2.getKey()).getCls1()+","+(entry2.getKey()).getCls2()+":"+entry2.getValue());
			    clsClsMapWriter.write("\r\n");
			    //System.out.println(((cls_cls)entry.getKey()).getCls1()+","+((cls_cls)entry.getKey()).getCls2()+":"+entry.getValue());
			}
			Iterator<Entry<text_cls , Integer>> iter3 = text_cls_map.entrySet().iterator(); 
			Entry<text_cls , Integer> entry3;
			while (iter3.hasNext()) { 
			    entry3 = iter3.next(); 
			    if(entry3.getValue()>10){
					if(!distc_text.contains((entry3.getKey()).getText()))
						distc_text.add((entry3.getKey()).getText());
				    textClsMapWriter.write((entry3.getKey()).getText()+","+(entry3.getKey()).getCls()+":"+entry3.getValue());
				    textClsMapWriter.write("\r\n");
				    }
			    //System.out.println(((text_cls)entry.getKey()).getText()+","+((text_cls)entry.getKey()).getCls()+":"+entry.getValue());
			}
			for(int i=0;i<distc_text.size();i++)
				distcTextMapWriter.write(distc_text.get(i)+"\r\n");
			clsMapWriter.close();
			clsClsMapWriter.close();
			textClsMapWriter.close();
			distcTextMapWriter.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		session.flush();
		session.clear();
		session.getTransaction().commit();
		session.close();
	}
	
	
	
// seg intro_learn with dictionary tokens and introduction tokens
	public void add_tag_raw(int pidFrom, int pidTo){
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Query q1 = session.createQuery("from Intro_learn where pid>="+pidFrom+" and pid<"+pidTo+" order by pid");
		Query q3 = session.createQuery("from Com_tokens order by char_length(sText) DESC");
		Query q4 = session.createQuery("from Intro_seg where id>="+pidFrom+" and id<"+pidTo+"order by id");
		List<Intro_learn> list_intro = q1.list();
		List<Com_tokens> comtos = q3.list();
		List<Intro_seg> list_seg = q4.list();
		String introduction;

		for(int i=0;i<list_intro.size();i++){
			String seg = new String();
			Intro_learn intro = new Intro_learn();
			Intro_seg intro_seg = new Intro_seg();
			intro = list_intro.get(i);
			intro_seg = list_seg.get(i);
			introduction = intro.getIntroduction();
			//add the dictionary tag
			introduction = TagHandler.add_dictionary_tag(introduction, comtos);
			seg = intro_seg.getIntroduction();
			//add the normal tag 
			introduction = TagHandler.add_normal_tag(introduction, seg);
			
			Query q2 = session.createQuery("update Intro_learn set introduction = ? where pid = ?");			
			q2.setParameter(0, introduction);
			q2.setParameter(1, intro.getPid());
			q2.executeUpdate();
			if(i%200==0){
				System.out.println("percent: "+ (double) i/list_intro.size()+" at "+i);
				session.flush();
				session.clear();
			}
		}
		session.flush();
		session.clear();
		session.getTransaction().commit();
		session.close();
	}
	
//replace segmented introduction tag directly after add_tag_raw()
	public void add_tag(){
		
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Query q1 = session.createQuery("from Intro_learn");
		List<Intro_learn> list_intro = q1.list();
		Intro_learn intro = new Intro_learn();
		ArrayList<String> list_text = new ArrayList<String>();
		ArrayList<Integer> list_cls = new ArrayList<Integer>();
		String introduction;
		for(int i=0;i<list_intro.size();i++){
			intro = list_intro.get(i);
			introduction = intro.getIntroduction();
			list_text.clear();
			list_cls.clear();
			StrHandler.intro_string2array(introduction, list_text,  list_cls);
			for(int j=0;j<list_text.size();j++){
				Query q2 = session.createQuery("from All_com where company = ?");
				q2.setParameter(0, list_text.get(j));
				if(q2.list().size()!=0){
					list_cls.set(j, 888);
					continue;
				}
				
				Query q3 = session.createQuery("from All_pos where position = ?");
				q3.setParameter(0, list_text.get(j));
				if(q3.list().size()!=0)
					list_cls.set(j, 999);

			}
//			Query q2 = session.createQuery("update Intro_learn set introduction = ? where id = ?");
//			String intro_replace = StrHandler.intro_array2string(list_text,list_cls);
			
//			q2.setParameter(0, intro_replace);
//			q2.setParameter(1, intro.getId());
//			q2.executeUpdate();
//			if(isContainLiren(liren,experd.getPosition())){
//				Query q2 = session.createQuery("update ExperD set position = ? where id = ?");
//				
//				count++;
//				//System.out.println("-----------"+experd.getId());
//				//System.out.println(experd.getPosition().substring(0, experd.getPosition().indexOf("(")));
//				q2.setParameter(0, experd.getPosition().substring(0, experd.getPosition().indexOf("(")));
//				q2.setParameter(1, experd.getId());
//				q2.executeUpdate();
//			}
//			else
//				continue;
			if(i%500==0){
				session.flush();
				session.clear();
			}
		}
		session.flush();
		session.clear();
		session.getTransaction().commit();
		session.close();
	}
	
	public static void main(String args[]){
		System.out.println("\\(");
	}
}



