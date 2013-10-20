/**
 * 
 */
package org.fdm.util;

/**
 * @author liyi
 *
 */

public class CleanDuplicate {
	//if return 0 the two input string are duplicates as names, else return 1
	public static int findDup(String s1, String s2){
		int n;
		//define the index of begining and ending of Chinese or English Name
		int firstCnIndex = -1;
		int firstEnIndex = -1;
		int lastEnIndex = -1;
		int lastCnIndex = -1;
		
		int firstCnIndex2 = -1;
		int firstEnIndex2 = -1;
		int lastEnIndex2 = -1;
		int lastCnIndex2 = -1;
		
		//replace all 
		for(int i=0;i<s1.length();i++){
			n = (int)s1.charAt(i);
			if(!(19968<=n&&n<=40623)&&!(97<=n&&n<=122||65<=n&&n<=90))
				s1 = s1.replace(s1.charAt(i),' ');
		}
		for(int i=0;i<s2.length();i++){
			n = (int)s2.charAt(i);
			if(!(19968<=n&&n<=40623)&&!(97<=n&&n<=122||65<=n&&n<=90))
				s2 = s2.replace(s2.charAt(i),' ');
		}
		
		s1 = s1.replaceAll("\u0020", "");
		s2 = s2.replaceAll("\u0020", "");
			
		if(s1.compareTo(s2)==0){
			return 0;
		}
		else{
			firstCnIndex = StrHandler.getFirstCnIndex(s1);
			firstEnIndex = StrHandler.getFirstEnIndex(s1);
			lastEnIndex = StrHandler.getLastEnIndex(s1);
			lastCnIndex = StrHandler.getLastCnIndex(s1);
			firstCnIndex2 = StrHandler.getFirstCnIndex(s2);
			firstEnIndex2 = StrHandler.getFirstEnIndex(s2);
			lastEnIndex2 = StrHandler.getLastEnIndex(s2);
			lastCnIndex2= StrHandler.getLastCnIndex(s2);
			if((lastEnIndex2==-1||(lastEnIndex2-firstEnIndex2)!=(lastEnIndex-firstEnIndex))&&(lastCnIndex2==-1||(lastCnIndex2-firstCnIndex2)!=(lastCnIndex-firstCnIndex)))
				return 1;
			else if(s1.regionMatches(true,firstEnIndex, s2, firstEnIndex2, lastEnIndex-firstEnIndex+1)||s1.regionMatches(true,firstCnIndex, s2, firstCnIndex2, lastCnIndex-firstCnIndex+1))
				return 0;
		}
		return 1;
		
	}
	
	/*public static void main(String args[]){
		int k = CleanDuplicate.findDup("ssd","ss");
		System.out.print(k);
	}*/
}
