package org.fdm.util;

import java.util.ArrayList;
import java.util.List;

import org.fdm.domain.AllDictTokens;

public class TagHandler {

	public static String add_dictionary_tag(String intro, List<AllDictTokens> comtos){
		int size = comtos.size();
		int tagP=0;
		String intro2 = new String();
		ArrayList<String> raw = new ArrayList<String>();
		ArrayList<String> tag = new ArrayList<String>();
		for(int i=0;i<size;i++){
			intro2 = intro;
			// separate introduction to raw text list and tagged text list
			while((tagP=intro2.indexOf('{'))!=-1){
				raw.add(intro2.substring(0, tagP));
				intro2 = intro2.substring(tagP, intro2.length());
				tag.add(intro2.substring(0,intro2.indexOf('}')+1));
				intro2 = intro2.substring(intro2.indexOf('}')+1);
			}
			raw.add(intro2);
			// replace raw with tagged text
			String replacer;
			String replaced;
			for(int j=0;j<raw.size();j++){
				replaced = comtos.get(i).getsText();
				replacer = comtos.get(i).getsTextTokens();
				raw.set(j, raw.get(j).replace(replaced, "{" + replacer.substring(0,replacer.length()-1) + "}"));
			}
			// rebuild introduction
			intro="";
			for(int j=0;j<raw.size()-1;j++){
				intro = intro+raw.get(j)+tag.get(j);
			}
			intro = intro+raw.get(raw.size()-1);
			raw.clear();
			tag.clear();
		}
		return intro;
	}
	
	public static String add_normal_tag(String intro, String segIntro){
		String temp;
		String seg_temp;
		String intro_pre;
		int point=0;
		int point2=0;
		int seg_point=0;
		int doll_cont=0;
		
		temp = intro;
		seg_temp=segIntro;
		intro_pre = "";
		
		while(((point = temp.indexOf("{"))!=-1 && doll_cont%2==0)||((point = temp.indexOf("}"))!=-1 && doll_cont%2==1)){
			doll_cont++;
			
			if(doll_cont%2==0){
				intro_pre = intro_pre+temp.substring(0, point);
				point2 = point;
				point = Locator.getP(temp.substring(0, point));
				temp = temp.substring(point2+1);
				seg_point = Locator.getSegP(seg_temp,point);
				seg_temp = seg_temp.substring(seg_point+1);
			}else{
				temp = temp.substring(point+1);
				seg_point = Locator.getSegP(seg_temp,point);
				intro_pre = intro_pre+seg_temp.substring(0,seg_point+1);
				seg_temp = seg_temp.substring(seg_point+1);
			}
			
		}
		return intro_pre;
	}
}
