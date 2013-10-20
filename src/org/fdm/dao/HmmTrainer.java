package org.fdm.dao;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.fdm.domain.AllDictTokens;
import org.fdm.domain.Intro_learn;
import org.fdm.domain.Intro_seg;
import org.fdm.model.HmmModel;
import org.fdm.util.StrHandler;
import org.fdm.util.TagHandler;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import exp.check.io.IoExpHandler;

@Component
public class HmmTrainer {
	private SessionFactory sessionFactory;
	private String modelName;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Resource
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	ArrayList<String> list_text = new ArrayList<String>();
	ArrayList<Integer> list_cls = new ArrayList<Integer>();
	ArrayList<String> distc_text = new ArrayList<String>(); // record the text sequence for future text_cls_prob
	LinkedHashMap<Integer , Integer> cls_map = new LinkedHashMap<Integer , Integer>(); 
	LinkedHashMap<cls_cls , Integer> cls_cls_map = new LinkedHashMap<cls_cls , Integer>(); 
	LinkedHashMap<text_cls , Integer> text_cls_map = new LinkedHashMap<text_cls , Integer>(); 
	ArrayList<Integer> clsMap_cls = new ArrayList<Integer>();
	ArrayList<Integer> clsMap_count = new ArrayList<Integer>();
	ArrayList<Integer> clsClsMap_cls1 = new ArrayList<Integer>();
	ArrayList<Integer> clsClsMap_cls2 = new ArrayList<Integer>();
	ArrayList<Integer> clsClsMap_count = new ArrayList<Integer>();
	ArrayList<Integer> textClsMap_cls = new ArrayList<Integer>();
	ArrayList<String> textClsMap_text = new ArrayList<String>();
	ArrayList<Integer> textClsMap_count = new ArrayList<Integer>();
	Double cls_cls_prob[][];
	Double cls_text_prob[][];
	
	public void genHmmModel() {
		genTextMaps();	
		genProbMats();
		HmmModel hmm = new HmmModel(cls_cls_prob, cls_text_prob, clsMap_cls, distc_text);
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(modelName);
			oos = new ObjectOutputStream(fos);
	        oos.writeObject(hmm);
		} catch (IOException ioe) {
			throw new RuntimeException("Err to serialize hmm to file '" + modelName + "'. Details: \r\n" + ioe.getMessage());
		} finally {
			IoExpHandler.closeOutputStream(oos);
			IoExpHandler.closeOutputStream(fos);
		}
	}
	
	/**
	 * generate prob mats from text-cls maps
	 */
	private void genProbMats(){
		clsMap_cls.addAll(this.cls_map.keySet());
		clsMap_count.addAll(this.cls_map.values());
		
		ArrayList<cls_cls> keys1 = new ArrayList<cls_cls>(this.cls_cls_map.keySet());
		for(int i = 0; i < keys1.size(); i++) {
			clsClsMap_cls1.add(keys1.get(i).getCls1());
			clsClsMap_cls2.add(keys1.get(i).getCls2());
		}
		clsClsMap_count.addAll(this.cls_cls_map.values());

		ArrayList<text_cls> keys2 = new ArrayList<text_cls>(this.text_cls_map.keySet());
		for(int i = 0; i < keys2.size(); i++) {
			textClsMap_text.add(keys2.get(i).getText());
			textClsMap_cls.add(keys2.get(i).getCls());
		}
		textClsMap_count.addAll(this.text_cls_map.values());

		int num = clsMap_cls.size();
		int num1 = distc_text.size();
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
			pos2 = distc_text.indexOf(text);
			if(pos2<0)
				continue;
			cls_text_prob[pos1][pos2] = ((double)textClsMap_count.get(i)/(double)clsMap_count.get(pos1));
		}
	}
	
	/**
	 * compute transition maps and counts
	 */
	private void genTextMaps(){
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Query q1 = session.createQuery("from Intro_learn");
		@SuppressWarnings("unchecked")
		List<Intro_learn> list_intro = q1.list();
		Intro_learn intro = new Intro_learn();
		
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
		Iterator<Entry<text_cls , Integer>> iter = text_cls_map.entrySet().iterator(); 
		Entry<text_cls , Integer> entry;
		while (iter.hasNext()) { 
            entry = iter.next(); 
            if(entry.getValue()>10){
                if(!distc_text.contains((entry.getKey()).getText()))
                	distc_text.add((entry.getKey()).getText());
            }
        }
		
		session.flush();
		session.clear();
		session.getTransaction().commit();
		session.close();
	}
	
	/**
	 * seg intro_learn with dictionary tokens and introduction tokens
	 * @param pidFrom
	 * @param pidTo
	 */
	public void add_tag(int pidFrom, int pidTo){
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Query q1 = session.createQuery("from Intro_learn where pid>="+pidFrom+" and pid<"+pidTo+" order by pid");
		Query q3 = session.createQuery("from AllDictTokens order by char_length(sText) DESC");
		Query q4 = session.createQuery("from Intro_seg where id>="+pidFrom+" and id<"+pidTo+"order by id");
		@SuppressWarnings("unchecked")
		List<Intro_learn> list_intro = q1.list();
		@SuppressWarnings("unchecked")
		List<AllDictTokens> dicTokens = q3.list();
		@SuppressWarnings("unchecked")
		List<Intro_seg> list_seg = q4.list();
		String introduction;
		String segIntro;
		Intro_learn intro;
		Intro_seg intro_seg;
		
		for(int i=0;i<list_intro.size();i++){
			intro = list_intro.get(i);
			intro_seg = list_seg.get(i);
			introduction = intro.getIntroduction();
			segIntro = intro_seg.getIntroduction();
			//add the dictionary tag
			introduction = TagHandler.add_dictionary_tag(introduction, dicTokens);
			//add the normal tag 
			introduction = TagHandler.add_normal_tag(introduction, segIntro);
			
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
	
	public static void main(String args[]){
		System.out.println("\\(");
	}
	
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
}



