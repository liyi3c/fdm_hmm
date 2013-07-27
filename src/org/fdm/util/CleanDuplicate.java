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
	static public int findDup(String s1, String s2){
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
		
//System.out.print((int)a);		
		if(s1.compareTo(s2)==0){
			return 0;
		}
		else{
			firstCnIndex=getFirstCnIndex(s1);
			firstEnIndex=getFirstEnIndex(s1);
			lastEnIndex=getLastEnIndex(s1);
			lastCnIndex=getLastCnIndex(s1);
			firstCnIndex2 = getFirstCnIndex(s2);
			firstEnIndex2 = getFirstEnIndex(s2);
			lastEnIndex2 = getLastEnIndex(s2);
			lastCnIndex2= getLastCnIndex(s2);
			//System.out.print(s1.regionMatches(true,firstEnIndex, s2, firstEnIndex2, lastEnIndex-firstEnIndex+1));
			if((lastEnIndex2==-1||(lastEnIndex2-firstEnIndex2)!=(lastEnIndex-firstEnIndex))&&(lastCnIndex2==-1||(lastCnIndex2-firstCnIndex2)!=(lastCnIndex-firstCnIndex)))
				return 1;
			else if(s1.regionMatches(true,firstEnIndex, s2, firstEnIndex2, lastEnIndex-firstEnIndex+1)||s1.regionMatches(true,firstCnIndex, s2, firstCnIndex2, lastCnIndex-firstCnIndex+1))
				return 0;
		}
		return 1;
		
	}
	static public int getFirstCnIndex(String s){
		int n;
		
		for(int i=0;i<s.length();i++){
			n = (int)s.charAt(i);
			if(19968<=n&&n<=40623){
				return i;
			}
		}
		return -1;
	}
	
	static public int getFirstEnIndex(String s){
		int n;
		
		for(int i=0;i<s.length();i++){
			n = (int)s.charAt(i);
			//97 122 65 90
			if((97<=n&&n<=122)||(65<=n&&n<=90))
				return i;
		}
		return -1;
	}
	
	static public int getLastCnIndex(String s){
		int n;
		int stat = 0;
		for(int i=0;i<s.length();i++){
			n = (int)s.charAt(i);
			if(19968<=n&&n<=40623){
				stat = 1;
				if(i==s.length()-1)
					return i;
			}
			else if(stat==1&&!(19968<=n&&n<=40623)){
				return i-1;
			}
		}
		return -1;
	}
	
	static public int getLastEnIndex(String s){
		int n;
		int stat = 0;
		for(int i=0;i<s.length();i++){
			n = (int)s.charAt(i);
			if((97<=n&&n<=122)||(65<=n&&n<=90)||n==46){
				stat = 1;
				if(i==s.length()-1)
					return i;
			}
			//97 122 65 90
			if(stat==1&&!((97<=n&&n<=122)||(65<=n&&n<=90)||n==46))
				return i-1;
		}
		return -1;
	}
	
	
	/*public static void main(String args[]){
		int k = CleanDuplicate.findDup("ssd","ss");
		System.out.print(k);
	}*/
}
