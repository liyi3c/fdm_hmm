package org.fdm.util;

import java.util.ArrayList;
import java.util.List;

import org.fdm.domain.Com_tokens;

public class TagHandler {

	public static String add_dictionary_tag(String intro, List<Com_tokens> comtos){
		int size = comtos.size();
		int tagp=0;
		String intro2 = new String();
		ArrayList<String> raw = new ArrayList<String>();
		ArrayList<String> tag = new ArrayList<String>();
		for(int i=0;i<size;i++){
			intro2 = intro;
			while((tagp=intro2.indexOf('{'))!=-1){
				raw.add(intro2.substring(0, tagp));
				intro2 = intro2.substring(tagp, intro2.length());
				tag.add(intro2.substring(0,intro2.indexOf('}')+1));
				intro2 = intro2.substring(intro2.indexOf('}')+1);
			}
			raw.add(intro2);
			for(int j=0;j<raw.size();j++){
				raw.set(j, raw.get(j).replace(comtos.get(i).getsText(), "{"+comtos.get(i).getsTextTokens().substring(0,comtos.get(i).getsTextTokens().length()-1)+"}"));
			}
			intro="";
			int j=0;
			for(j=0;j<raw.size()-1;j++){
				intro = intro+raw.get(j)+tag.get(j);
			}
			intro = intro+raw.get(j);
			raw.clear();
			tag.clear();
		}
		return intro;
	}
	
	public static String add_normal_tag(String intro, String seg){
		String temp = new String();
		String seg_temp = new String();
		String intro_pre = new String();
		int point=0;
		int seg_point=0;
		int doll_cont=0;
		temp = intro;
		seg_temp=seg;
		intro_pre = "";
		int point2=0;
		while(((point = temp.indexOf("{"))!=-1&&doll_cont%2==0)||((point = temp.indexOf("}"))!=-1&&doll_cont%2==1)){
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
