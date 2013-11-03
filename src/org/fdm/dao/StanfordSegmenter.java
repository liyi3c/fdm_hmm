package org.fdm.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.fdm.domain.Intro_seg;
import org.fdm.util.StrHandler;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

@Component
public class StanfordSegmenter {
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Resource
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void segment() {
		Properties props = new Properties();
	    props.setProperty("sighanCorporaDict", "data");
	    // props.setProperty("NormalizationTable", "data/norm.simp.utf8");
	    // props.setProperty("normTableEncoding", "UTF-8");
	    // below is needed because CTBSegDocumentIteratorFactory accesses it
	    props.setProperty("serDictionary","data/dict-chris6.ser.gz");
	    props.setProperty("inputEncoding", "UTF-8");
	    props.setProperty("sighanPostProcessing", "true");
	    //props.setProperty("testFile", "intros/in");
	    CRFClassifier<CoreLabel> segmenter = new CRFClassifier<CoreLabel>(props);
	    segmenter.loadClassifierNoExceptions("data/ctb.gz", props);
	    
	    Session session = sessionFactory.openSession();
		session.beginTransaction();
		Query q = session.createQuery("from Intro_seg where pid<5");
		@SuppressWarnings("unchecked")
		List<Intro_seg> sIntroList = q.list();
		String intro_stf_seg;
		int tag;
		String[] splitList;
		ArrayList<String> textList = new ArrayList<String>();
		ArrayList<Integer> tagList = new ArrayList<Integer>();
		ArrayList<String> textNewList = new ArrayList<String>();
		ArrayList<Integer> tagNewList = new ArrayList<Integer>();
		int cnt = 0;
    	for(Intro_seg introSeg : sIntroList) {
    		textList.clear();
    		tagList.clear();
    		textNewList.clear();
    		tagNewList.clear();
    		StrHandler.introString2array(introSeg.getIntroduction(), textList, tagList);
    		for(int i = 0; i < textList.size(); i++) {
    			intro_stf_seg = segmenter.classifyToString(textList.get(i));
    			if(intro_stf_seg.length() <= 3)
    				continue;
    			intro_stf_seg = intro_stf_seg.substring(0, intro_stf_seg.length()-3);
    			tag = tagList.get(i);
    			splitList = intro_stf_seg.split(" ");
    			for(int j = 0; j < splitList.length; j++) {
    				textNewList.add(splitList[j]);
    				tagNewList.add(tag);
    			}
    		}
    		intro_stf_seg = StrHandler.introArray2string(textNewList, tagNewList);
//    		System.out.println("begin:" + intro_stf_seg);
    		Query q3 = session.createQuery("update Intro_seg set introduction = ? where pid = ?");			
			q3.setParameter(0, intro_stf_seg);
			q3.setParameter(1, introSeg.getPid());
			q3.executeUpdate();
			cnt++;
			if(cnt%1000==0){
				System.out.println("percent: "+ (double) cnt/sIntroList.size()+" at "+ cnt);
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
