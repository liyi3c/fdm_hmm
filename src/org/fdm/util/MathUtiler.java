package org.fdm.util;

public class MathUtiler {
	//find the max value of doubles
	public static int double_max(Double[] d, int size){
		int tag=0;
		Double max=0.0;
		for(int i=0;i<size;i++){
			if(d[i]>max){
				tag=i;
				max = d[i];
			}
		}
		return tag;
	}
}
