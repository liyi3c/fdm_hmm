package org.fdm.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.fdm.domain.AllDictTokens;
import org.fdm.domain.All_dict;
import org.fdm.domain.Intro_learn;
import org.fdm.domain.PotentialWrong;
import org.fdm.util.ListUtil;
import org.fdm.util.StrHandler;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import exp.check.io.IoExpHandler;

enum AwdComPrior {
	AwdPrior1("获"), 
	AwdPrior2("享受"), 
	AwdPrior3("当选"), 
	AwdPrior4("获得"), 
	AwdPrior5("取得"), 
	AwdPrior6("荣获"),
	AwdPrior7("被"),
	AwdPrior8("被评为"),
	AwdPrior9("拥有"),
	AwdPrior10("入选"),
	AwdPrior11("成为");
	
	private final static int priorTag = Tag.AwdCompanyPriorTag.tagValue();
	private final static int curenTag = Tag.AwdCompanyTag.tagValue();
	
	private String tok;
	
	private AwdComPrior(String tok) {
		this.tok = tok;
	}
	
	@Override
	public String toString() {
		return String.valueOf(tok);
	}
	
	public static boolean contains(String type) {
		for (AwdComPrior t : AwdComPrior.values()) {
			if (t.toString().equals(type))
				return true;
		}
		return false;
	}
	
	public static ArrayList<String> toStringList() {
		ArrayList<String> ret = new ArrayList<String>();
		for (AwdComPrior t : AwdComPrior.values()) {
			ret.add(t.toString());
		}
		return ret;
	}
	
	public static HashMap<String, Integer> toStringMap() {
		HashMap<String, Integer> ret = new HashMap<String, Integer>();
		for (int i = 0; i < AwdComPrior.values().length; i++) {
			AwdComPrior t = AwdComPrior.values()[i];
			ret.put(t.toString(), i);
		}
		return ret;
	}
	
	public static int getPriorTag() {
		return priorTag;
	}
	
	public static int getCurenTag() {
		return curenTag;
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
	Prior37("退休"), Prior38("解任"), Prior39("解聘"), Prior40("请辞"),
	Prior41("负责"), Prior42("担任过"), Prior43("系"), Prior44("组建"),
	Prior45("参与筹备"), Prior46("离任"), Prior47("挂职"), Prior48("代行"),
	Prior49("调"), Prior50("亦是"), Prior51("派驻");
	private final static int tag = Tag.CompanyPriorTag.tagValue();
	
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
	
	public static int getTag() {
		return tag;
	}
}

enum ComPriorSpec {
	Prior1("、"), Prior2("，"), Prior3(":"), 
	Prior4("及"), Prior5(";"), Prior6("和"),
	Prior7(","), Prior8("以及");
	private final static int tag = Tag.CompanyPriorSpecialTag.tagValue();
	
	private String tok;
	
	private ComPriorSpec(String tok) {
		this.tok = tok;
	}
	
	@Override
	public String toString() {
		return String.valueOf(tok);
	}
	
	public static boolean contains(String type) {
		for (ComPriorSpec t : ComPriorSpec.values()) {
			if (t.toString().equals(type))
				return true;
		}
		return false;
	}
	
	public static ArrayList<String> toStringList() {
		ArrayList<String> ret = new ArrayList<String>();
		for (ComPriorSpec t : ComPriorSpec.values()) {
			ret.add(t.toString());
		}
		return ret;
	}
	
	public static HashMap<String, Integer> toStringMap() {
		HashMap<String, Integer> ret = new HashMap<String, Integer>();
		for (int i = 0; i < ComPriorSpec.values().length; i++) {
			ComPriorSpec t = ComPriorSpec.values()[i];
			ret.put(t.toString(), i);
		}
		return ret;
	}
	
	public static int getTag() {
		return tag;
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
	
	public void addTag() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Query q1 = session.createQuery("from Intro_learn where pid >= 5 order by pid");
		Query q2 = session.createQuery("from All_dict order by char_length(sText) DESC");
		@SuppressWarnings("unchecked")
		List<Intro_learn> list_intro = q1.list();
		@SuppressWarnings("unchecked")
		List<All_dict> dictList = q2.list();
		
		int tag;
		String intro;
		ArrayList<String> textList = new ArrayList<String>();
		ArrayList<Integer> tagList = new ArrayList<Integer>();
		ArrayList<Integer> beginIndexList = new ArrayList<Integer>();
		ArrayList<Integer> endIndexList = new ArrayList<Integer>();
		ListUtil<String> u = new ListUtil<String>();
		
		for(int i=0;i<list_intro.size();i++){
			intro = list_intro.get(i).getIntroduction();
			textList.clear();
			tagList.clear();
			StrHandler.introString2array(intro, textList, tagList);
			u.setList(textList);
			for(int j = 0; j < dictList.size(); j++) {
				if(u.containsSubList(dictList.get(j).getsText(), beginIndexList, endIndexList)) {
					if(dictList.get(j).getsType().equals("company"))
						tag = Tag.CompanyTag.tagValue();
					else if(dictList.get(j).getsType().equals("department"))
						tag = Tag.DepartmentTag.tagValue();
					else if(dictList.get(j).getsType().equals("position"))
						tag = Tag.PositionTag.tagValue();
					else if(dictList.get(j).getsType().equals("location"))
						tag = Tag.LocationTag.tagValue();
					else if(dictList.get(j).getsType().equals("degree"))
						tag = Tag.DegreeTag.tagValue();
					else 
						throw new RuntimeException("Invalid dictionary type");
					
					for(int ii = 0 ; ii < beginIndexList.size(); ii++) {
						for(int jj = beginIndexList.get(ii); jj <= endIndexList.get(ii); jj++)
							if(tagList.get(jj) != 888 && tagList.get(jj) != 999 && tagList.get(jj) != 777
									&& tagList.get(jj) != 881 && tagList.get(jj) != 991)
								tagList.set(jj, tag);
					}		
				}
			}
			intro = StrHandler.introArray2string(textList, tagList);
			Query q3 = session.createQuery("update Intro_learn set introduction = ? where pid = ?");			
			q3.setParameter(0, intro);
			q3.setParameter(1, list_intro.get(i).getPid());
			q3.executeUpdate();
			if(i%1000==0){
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
	
	public void addPriorTokenTag(int tag) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Query q1 = session.createQuery("from Intro_learn");
		@SuppressWarnings("unchecked")
		List<Intro_learn> list_intro = q1.list();
		String introduction;
		Intro_learn intro;
		HashMap<String, Integer> ComPriorMap = ComPrior.toStringMap();
		HashMap<String, Integer> ComPriorSpecMap = ComPriorSpec.toStringMap();
		HashMap<String, Integer> AwdPriorMap = AwdComPrior.toStringMap();
		for(int j = 0; j < list_intro.size(); j++) {
			intro = list_intro.get(j);
			ArrayList<String> textList = new ArrayList<String>();
			ArrayList<Integer> tagList = new ArrayList<Integer>();
			introduction = intro.getIntroduction();
			StrHandler.introString2array(introduction, textList, tagList);
			String prior1;
			String prior2;
			boolean isChanged = false;
			for(int i = 2; i < textList.size(); i++) {
				if (tag == 888) {
					
					if (tagList.get(i) == tag) {
						prior1 = textList.get(i-1);
						prior2 = textList.get(i-2);
						if(ComPriorMap.containsKey(prior1)) {
							tagList.set(i-1, ComPrior.getTag());
							isChanged = true;
						}
						if(ComPriorMap.containsKey(prior2)) {
							tagList.set(i-2, ComPrior.getTag());
							isChanged = true;
						}
						
						if(AwdPriorMap.containsKey(prior1)) {
							tagList.set(i-1, AwdComPrior.getPriorTag());
							tagList.set(i, AwdComPrior.getCurenTag());
							isChanged = true;
						}
						if(AwdPriorMap.containsKey(prior2)) {
							tagList.set(i-2, AwdComPrior.getPriorTag());
							tagList.set(i, AwdComPrior.getCurenTag());
							isChanged = true;
						}
						
						if(ComPriorSpecMap.containsKey(prior1)) {
							tagList.set(i-1, ComPriorSpec.getTag());
							isChanged = true;
						}
						if(ComPriorSpecMap.containsKey(prior2)) {
							tagList.set(i-2, ComPriorSpec.getTag());
							isChanged = true;
						}
					}
				}
			}
			
			if (j%1000 == 0) {
				System.out.println("Percent: " + (double)j/list_intro.size());
				session.flush();
				session.clear();
			}
			
			if(isChanged) {
				introduction = StrHandler.introArray2string(textList, tagList);
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
	
	
	public void findParen(String yourParen, String findParen) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Query q2 = session.createQuery("from All_dict where sText like '%"+yourParen+"%' and sText != '%"+findParen+"'");
		@SuppressWarnings("unchecked")
		List<All_dict> dictList = q2.list();
		String dict;
		
		for(int i=0;i<dictList.size();i++){
			dict = dictList.get(i).getsText();
			if(!dict.endsWith(findParen) && !dict.contains(findParen)){
				if(findParen.equals(")"))
					dict = dict+"前身)";
				else
					dict = "("+dict;
				
				Query q1 = session.createQuery("from Intro_raw where introduction like '%"+dict+"%'");
				if(q1.list().size()!=0) {
					Query q3 = session.createQuery("update All_dict set sText = ?, sRemark = ? where nID = ?");
					q3.setParameter(0, dict);
					q3.setParameter(1, "addParen");
					q3.setParameter(2, dictList.get(i).getnID());
					q3.executeUpdate();
				}
			}
		}
		
		session.flush();
		session.clear();
		session.getTransaction().commit();
		session.close();
	}
	
	@Deprecated
	public void prepareCleanDict() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Query q2 = session.createQuery("from All_dict where stext like '%办事处' and sRemark != 'correct'");
		@SuppressWarnings("unchecked")
		List<All_dict> dictList = q2.list();
		String dict;
		PrintWriter pw = new PrintWriter(System.out);
		Pattern p = Pattern.compile("(?<com>.*政府)(?<dep>.*办事处)");
		for(int i=0;i<dictList.size();i++){
			dict = dictList.get(i).getsText();
			Matcher m = p.matcher(dict);
			if(m.matches()){
//				pw.print(m.group("com")+"\t");
//				pw.print(m.group("dep")+"\n");
				Query q1 = session.createQuery("update All_dict set sText = ?, sRemark = ? where sText = ?");
				q1.setString(0, m.group("com"));
				q1.setString(1, m.group("dep"));
				q1.setString(2, dict);
				q1.executeUpdate();
			}
		}
		
		pw.close();
		session.flush();
		session.clear();
		session.getTransaction().commit();
		session.close();
	}
	
	@Deprecated
	public void cleanDict() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Query q2 = session.createQuery("from All_dict where sRemark not like '%correct%' " +
				"and sRemark not like '%add%'" +
				"and sRemark not like '%NA%'");
		@SuppressWarnings("unchecked")
		List<All_dict> dictList = q2.list();
		String dict;
		String sRemark;
		for(int i=0;i<dictList.size();i++){
			dict = dictList.get(i).getsText();
			sRemark = dictList.get(i).getsRemark();
//			System.out.println(dict+"\t"+sRemark);
			Query q1 = session.createQuery("update All_dict set sRemark = ? where sText = ?");
			q1.setString(0, "correct");
			q1.setString(1, dict);
			q1.executeUpdate();
			session.save(new All_dict(sRemark,"department", "correct"));
		}
		
		session.flush();
		session.clear();
		session.getTransaction().commit();
		session.close();
	}
	
	@Deprecated
	public void getTokensWithGivenTagAfterGivenToken(int tag, String givenTok) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Query q1 = session.createQuery("from Intro_learn");
		@SuppressWarnings("unchecked")
		List<Intro_learn> list_intro = q1.list();
		FileOutputStream fos1 = null;
		OutputStreamWriter osw = null;
		String outFileName = givenTok;
		if(outFileName.equals(":"))
			outFileName = "冒号";
		else if(outFileName.equals(";"))
			outFileName = "分号";
		else if(outFileName.equals("/"))
			outFileName = "slash";
		else if(outFileName.equals("?"))
			outFileName = "Q";
		else if(outFileName.equals("*"))
			outFileName = "star";
		HashMap<String, Integer> tokenCountMap = new HashMap<String, Integer>();
		MapValueComparator<String, Integer> cntComp = new MapValueComparator<String, Integer>(tokenCountMap);
		TreeMap<String, Integer> treeMap = new TreeMap<String, Integer>(cntComp);
		try {
			fos1 = new FileOutputStream(new File(outFileName+"Next"+tag+".tok"));
			osw = new OutputStreamWriter(fos1);
			String introduction;
			String target;
			for(Intro_learn intro : list_intro) {
				ArrayList<String> textList = new ArrayList<String>();
				ArrayList<Integer> tagList = new ArrayList<Integer>();
				introduction = intro.getIntroduction();
				StrHandler.introString2array(introduction, textList, tagList);
				for(int i = 7; i < textList.size()-7; i++) {
					if(textList.get(i).equals(givenTok) && tagList.get(i) != tag) {
						if(tagList.get(i+1) == tag) {
							target = textList.get(i-7)+textList.get(i-6)+textList.get(i-5)
									+textList.get(i-4)+textList.get(i-3)+textList.get(i-2)+textList.get(i-1)
									+givenTok+textList.get(i+1)+textList.get(i+2)+textList.get(i+3)
									+textList.get(i+4)+textList.get(i+5)+textList.get(i+6)+textList.get(i+7);
							if(tokenCountMap.containsKey(target))
								tokenCountMap.put(target, tokenCountMap.get(target)+1);
							else 
								tokenCountMap.put(target, 1);
						}
					}
				}
			}
			
			treeMap.putAll(tokenCountMap);
			for(String key : treeMap.keySet())
				osw.write(key + " : " + tokenCountMap.get(key) + "\r\n");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IoExpHandler.closeWriter(osw);
			IoExpHandler.closeOutputStream(fos1);
		}
		session.flush();
		session.clear();
		session.getTransaction().commit();
		session.close();
	}
	
	@Deprecated
	public void getTokensBeforeGivenTag(int givenTag, int offset, String tagName) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Query q1 = session.createQuery("from Intro_learn");
		@SuppressWarnings("unchecked")
		List<Intro_learn> list_intro = q1.list();
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			fos = new FileOutputStream(new File(tagName+"Prior"+offset+".tok"));
			osw = new OutputStreamWriter(fos);
			String introduction;
			HashMap<String, Integer> priorTextCntMap = new HashMap<String, Integer>();
			MapValueComparator<String, Integer> cntComp = new MapValueComparator<String, Integer>(priorTextCntMap);
			TreeMap<String, Integer> treeMap = new TreeMap<String, Integer>(cntComp);
			
			for(Intro_learn intro : list_intro) {
				ArrayList<String> textList = new ArrayList<String>();
				ArrayList<Integer> tagList = new ArrayList<Integer>();
				introduction = intro.getIntroduction();
				StrHandler.introString2array(introduction, textList, tagList);
				String prior;
				for(int i = offset; i < textList.size(); i++) {
					if(tagList.get(i) == givenTag && tagList.get(i-offset) != givenTag) {
						prior = textList.get(i-offset);
						if(priorTextCntMap.containsKey(prior))
							priorTextCntMap.put(prior, priorTextCntMap.get(prior) + 1);
						else
							priorTextCntMap.put(prior, 1);
					}
				}
			}
			
			treeMap.putAll(priorTextCntMap);
			for(String key : treeMap.keySet())
				osw.write((key + " : " + priorTextCntMap.get(key)) + "\r\n");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IoExpHandler.closeWriter(osw);
			IoExpHandler.closeOutputStream(fos);
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
	
	@Deprecated
	public void getPotentialWrongTaggedSuccessor(int supposedTag) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Query q1 = session.createQuery("from Intro_learn");
		@SuppressWarnings("unchecked")
		List<Intro_learn> list_intro = q1.list();
		String introduction;
		HashMap<String, Integer> ComPriorMap = ComPrior.toStringMap();
		HashMap<String, Integer> AwdPriorMap = AwdComPrior.toStringMap();
		FileOutputStream fos1 = null;
		OutputStreamWriter osw1 = null;
		try {
			fos1 = new FileOutputStream(new File("PotentialWrongTaggedSuccessor" + supposedTag + ".tok"));
			osw1 = new OutputStreamWriter(fos1);
			for(Intro_learn intro : list_intro) {
				ArrayList<String> textList = new ArrayList<String>();
				ArrayList<Integer> tagList = new ArrayList<Integer>();
				introduction = intro.getIntroduction();
				StrHandler.introString2array(introduction, textList, tagList);
				int successor1;
				int successor2;
				for(int i = 0; i < textList.size()-3; i++) {
					successor1 = tagList.get(i+1);
					successor2 = tagList.get(i+2);
					switch (supposedTag) {
						case 888 : {
							if (ComPriorMap.containsKey(textList.get(i))) {
								if (successor1 != 888 && successor2 != 888) {
									osw1.write(textList.get(i) + "|" + tagList.get(i) + "\t" + 
											textList.get(i+1) + "|" + tagList.get(i+1) + "\t" + 
											textList.get(i+2) + "|" + tagList.get(i+2) + "\t" + "\r\n");
									session.save(new PotentialWrong(textList.get(i),textList.get(i+1),textList.get(i+2),textList.get(i+3), 
										tagList.get(i),tagList.get(i+1),tagList.get(i+2), tagList.get(i+3), ""));
								}
							}
						}
						case 889 : {
							if (AwdPriorMap.containsKey(textList.get(i))) {
								if (successor1 != 889 && successor2 != 889)
									osw1.write(textList.get(i) + "|" + tagList.get(i) + "\t" + 
											textList.get(i+1) + "|" + tagList.get(i+1) + "\t" + 
											textList.get(i+2) + "|" + tagList.get(i+2) + "\t" + "\r\n");
							}
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IoExpHandler.closeWriter(osw1);
			IoExpHandler.closeOutputStream(fos1);
		}
		session.flush();
		session.clear();
		session.getTransaction().commit();
		session.close();
	}
}
