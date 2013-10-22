package org.fdm.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.fdm.domain.Intro_learn;
import org.fdm.util.StrHandler;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import exp.check.io.IoExpHandler;

enum AwdPrior {
	AwdPrior1("获"), 
	AwdPrior2("享受"), 
	AwdPrior3("当选"), 
	AwdPrior4("获得"), 
	AwdPrior5("取得"), 
	AwdPrior6("荣获"), 
	AwdPrior7("被评为");
	private final static int priorCls = 8891;
	private final static int curenCls = 889;
	
	private String tok;
	
	private AwdPrior(String tok) {
		this.tok = tok;
	}
	
	@Override
	public String toString() {
		return String.valueOf(tok);
	}
	
	public static boolean contains(String type) {
		for (AwdPrior t : AwdPrior.values()) {
			if (t.toString().equals(type))
				return true;
		}
		return false;
	}
	
	public static ArrayList<String> toStringList() {
		ArrayList<String> ret = new ArrayList<String>();
		for (AwdPrior t : AwdPrior.values()) {
			ret.add(t.toString());
		}
		return ret;
	}
	
	public static HashMap<String, Integer> toStringMap() {
		HashMap<String, Integer> ret = new HashMap<String, Integer>();
		for (int i = 0; i < AwdPrior.values().length; i++) {
			AwdPrior t = AwdPrior.values()[i];
			ret.put(t.toString(), i);
		}
		return ret;
	}
	
	public static int getPriorCls() {
		return priorCls;
	}
	
	public static int getCurenCls() {
		return curenCls;
	}
}

enum ComPrior {
	Prior1("为"), Prior2("于"), Prior3("任教"), Prior4("任职"), 
	Prior5("任"), Prior6("供职"), Prior7("兼任"), Prior8("兼职"), 
	Prior9("兼"), Prior10("出任"), Prior11("创办"), Prior12("创建"), 
	Prior13("创立"), Prior14("加入"), Prior15("加盟"), Prior16("历任"), 
	Prior17("原任"), Prior18("后任"), Prior19("在"), Prior20("就任"), 
	Prior21("就职"), Prior22("就读"), Prior23("工作"), Prior24("开创"), 
	Prior25("担任"), Prior26("曾"), Prior27("毕业"), Prior28("现任"), 
	Prior29("调入"), Prior30("进入"), Prior31("聘为"), Prior32("从事"), 
	Prior33("辞去"), Prior34("辞职"), Prior35("卸任"), Prior36("免去"), 
	Prior37("退休"), Prior38("解任"), Prior39("解聘"), Prior40("请辞");
	private final static int cls = 8881;
	
	private String tok;
	
	private ComPrior(String tok) {
		this.tok = tok;
	}
	
	@Override
	public String toString() {
		return String.valueOf(tok);
	}
	
	public static boolean contains(String type) {
		for (ComPrior t : ComPrior.values()) {
			if (t.toString().equals(type))
				return true;
		}
		return false;
	}
	
	public static ArrayList<String> toStringList() {
		ArrayList<String> ret = new ArrayList<String>();
		for (ComPrior t : ComPrior.values()) {
			ret.add(t.toString());
		}
		return ret;
	}
	
	public static HashMap<String, Integer> toStringMap() {
		HashMap<String, Integer> ret = new HashMap<String, Integer>();
		for (int i = 0; i < ComPrior.values().length; i++) {
			ComPrior t = ComPrior.values()[i];
			ret.put(t.toString(), i);
		}
		return ret;
	}
	
	public static int getCls() {
		return cls;
	}
}

@Component
public class HmmTrainerData {
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Resource
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void addPriorTokenTag(int cls) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Query q1 = session.createQuery("from Intro_learn");
		@SuppressWarnings("unchecked")
		List<Intro_learn> list_intro = q1.list();
		String introduction;
		Intro_learn intro;
		HashMap<String, Integer> ComPriorMap = ComPrior.toStringMap();
		HashMap<String, Integer> AwdPriorMap = AwdPrior.toStringMap();
		for(int j = 0; j < list_intro.size(); j++) {
			intro = list_intro.get(j);
			ArrayList<String> textList = new ArrayList<String>();
			ArrayList<Integer> clsList = new ArrayList<Integer>();
			introduction = intro.getIntroduction();
			StrHandler.intro_string2array(introduction, textList, clsList);
			String prior1;
			String prior2;
			boolean isChanged = false;
			for(int i = 2; i < textList.size(); i++) {
				if (cls == 888) {
					
					if (clsList.get(i) != cls && textList.get(i).equals("本公司")) {
						clsList.set(i, cls);
						isChanged = true;
					}
					
					if (clsList.get(i) == cls) {
						prior1 = textList.get(i-1);
						prior2 = textList.get(i-2);
						if(ComPriorMap.containsKey(prior1)) {
							clsList.set(i-1, ComPrior.getCls());
							isChanged = true;
						}
						if(ComPriorMap.containsKey(prior2)) {
							clsList.set(i-2, ComPrior.getCls());
							isChanged = true;
						}
						
						if(AwdPriorMap.containsKey(prior1)) {
							clsList.set(i-1, AwdPrior.getPriorCls());
							clsList.set(i, AwdPrior.getCurenCls());
							isChanged = true;
						}
						if(AwdPriorMap.containsKey(prior2)) {
							clsList.set(i-2, AwdPrior.getPriorCls());
							clsList.set(i, AwdPrior.getCurenCls());
							isChanged = true;
						}
					}
				}
			}
			
			if (j%400 == 0) {
				System.out.println("Percent: " + (double)j/list_intro.size());
				session.flush();
				session.clear();
			}
			
			if(isChanged) {
				introduction = StrHandler.intro_array2string(textList, clsList);
				Query q2 = session.createQuery("update Intro_learn set introduction = ? where pid = ?");			
				q2.setParameter(0, introduction);
				q2.setParameter(1, intro.getPid());
				q2.executeUpdate();
			}
		}
		
		session.flush();
		session.clear();
		session.getTransaction().commit();
		session.close();
	}
	
	public void getPriorToken(int cls, String clsStr) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Query q1 = session.createQuery("from Intro_learn");
		@SuppressWarnings("unchecked")
		List<Intro_learn> list_intro = q1.list();
		FileOutputStream fos1 = null;
		OutputStreamWriter osw1 = null;
		FileOutputStream fos2 = null;
		OutputStreamWriter osw2 = null;
		try {
			fos1 = new FileOutputStream(new File(clsStr+"Prior1.tok"));
			osw1 = new OutputStreamWriter(fos1);
			fos2 = new FileOutputStream(new File(clsStr+"Prior2.tok"));
			osw2 = new OutputStreamWriter(fos2);
			String introduction;
			HashMap<String, Integer> priorTextCntMap1 = new HashMap<String, Integer>();
			MapValueComparator<String, Integer> cntComp1 = new MapValueComparator<String, Integer>(priorTextCntMap1);
			TreeMap<String, Integer> treeMap1 = new TreeMap<String, Integer>(cntComp1);
			
			HashMap<String, Integer> priorTextCntMap2 = new HashMap<String, Integer>();
			MapValueComparator<String, Integer> cntComp2 = new MapValueComparator<String, Integer>(priorTextCntMap2);
			TreeMap<String, Integer> treeMap2 = new TreeMap<String, Integer>(cntComp2);
			
			for(Intro_learn intro : list_intro) {
				ArrayList<String> textList = new ArrayList<String>();
				ArrayList<Integer> clsList = new ArrayList<Integer>();
				introduction = intro.getIntroduction();
				StrHandler.intro_string2array(introduction, textList, clsList);
				String prior1;
				String prior2;
				for(int i = 2; i < textList.size(); i++) {
					if(clsList.get(i) == cls) {
						prior1 = textList.get(i-1);
						prior2 = textList.get(i-2);
						if(priorTextCntMap1.containsKey(prior1))
							priorTextCntMap1.put(prior1, priorTextCntMap1.get(prior1) + 1);
						else
							priorTextCntMap1.put(prior1, 1);
						if(priorTextCntMap2.containsKey(prior2))
							priorTextCntMap2.put(prior2, priorTextCntMap2.get(prior2) + 1);
						else
							priorTextCntMap2.put(prior2, 1);
					}
				}
			}
			
			treeMap1.putAll(priorTextCntMap1);
			for(String key : treeMap1.keySet())
				osw1.write((key + " : " + priorTextCntMap1.get(key)) + "\r\n");
			treeMap2.putAll(priorTextCntMap2);
			for(String key : treeMap2.keySet())
				osw2.write((key + " : " + priorTextCntMap2.get(key)) + "\r\n");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IoExpHandler.closeWriter(osw1);
			IoExpHandler.closeOutputStream(fos1);
			IoExpHandler.closeWriter(osw2);
			IoExpHandler.closeOutputStream(fos2);
		}
		session.flush();
		session.clear();
		session.getTransaction().commit();
		session.close();
	}
	
	class MapValueComparator<K, V extends Comparable<V>> implements Comparator<K> {

	    Map<K, V> base;
	    public MapValueComparator(Map<K, V> base) {
	        this.base = base;
	    }

	    // Note: this comparator imposes orderings that are inconsistent with equals.    
	    public int compare(K a, K b) {
	        if (base.get(a).compareTo(base.get(b)) > 0) {
	        	// DESC
	            return -1;
	        } else {
	            return 1;
	        } // returning 0 would merge keys
	    }
	}
}
