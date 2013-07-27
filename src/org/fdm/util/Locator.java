package org.fdm.util;

public class Locator {
	
	public static int getSegP(String seg, int p){
		int temp_p=0;
		int tag_cont=0;
		int index=0;
		while(temp_p!=p+1){
//			if(index>=seg.length())
//				System.out.println(seg);
			char at= seg.charAt(index);
			index++;
			if(at=='\t'){
				tag_cont++;
				continue;
			}else if(tag_cont%3==0){
				temp_p++;
			}
		}
		return index-2;
	}
	
	public static int getP(String intro){
		int p=0;
		int tag_cont=0;
		for(int i=0;i<intro.length();i++)
			if(intro.charAt(i)=='\t'){
				tag_cont++;
				continue;
			}else if(tag_cont%3==0){
				p++;
			}
		
		return p;
	}
	
}
