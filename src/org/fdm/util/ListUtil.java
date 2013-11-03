package org.fdm.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtil<T> {
	private List<T> list;

	public ListUtil() {
		super();
	}
	
	public ListUtil(List<T> list) {
		super();
		this.list = list;
	}
	
	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public boolean containsSubList(T in, ArrayList<Integer> beginIndexList, ArrayList<Integer> endIndexList) {
		beginIndexList.clear();
		endIndexList.clear();
		if(in instanceof String) {
			String strIn = (String)in; // string to be match
			String strInBegin = strIn.substring(0, 1);
			@SuppressWarnings("unchecked")
			List<String> strList = (List<String>)list;
			
			String strListElemBegin;
			String strListElemCat;
			
			for(int i = 0; i < strList.size(); i++) {
				strListElemBegin = strList.get(i).substring(0, 1);
				if(!strListElemBegin.equalsIgnoreCase(strInBegin)) // no possible to match the next ones
					continue;
				// if match first one, go on
				strListElemCat = "";
				for(int j = i; j < strList.size(); j++) {
					strListElemCat = strListElemCat + strList.get(j);
					if(strIn.equalsIgnoreCase(strListElemCat)) {
						beginIndexList.add(i);
						endIndexList.add(j);
						i = j+1;
						break;
					}
					if(!strIn.startsWith(strListElemCat))
						break;
				}
			}
		}
		
		return beginIndexList.size()==0?false:true;
	}
	
	/**
	 * test
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<String> sl = new ArrayList<String>();
		sl.add("在");
		sl.add("这里");
		sl.add("我们");
		sl.add("在");
		sl.add("这里");
		sl.add("。");
		sl.add("马上");
		sl.add("就");
		sl.add("要离开");
		sl.add("了");
		sl.add("在");
		sl.add("这里");
		sl.add("了");
		sl.add("在");
		ListUtil<String> u = new ListUtil<String>(sl);
		ArrayList<Integer> beginIndexList = new ArrayList<Integer>();
		ArrayList<Integer> endIndexList = new ArrayList<Integer>();
		if(u.containsSubList("在这里啊", beginIndexList, endIndexList)) {
			for(int i = 0 ; i < beginIndexList.size(); i++)
				for(int j = beginIndexList.get(i); j <= endIndexList.get(i); j++)
					System.out.println(sl.get(j));
		}
	}
}
