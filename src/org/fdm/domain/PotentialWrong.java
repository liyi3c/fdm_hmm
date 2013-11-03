package org.fdm.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tblPotentialWrongTaggedIntro")
public class PotentialWrong {
	int nID;
	String sMarkToken;
	String sPotentialWrong1;
	String sPotentialWrong2;
	String sPotentialWrong3;
	int nTag;
	int nTag1;
	int nTag2;
	int nTag3;
	String sSuccesorAll;
	public PotentialWrong(String sMarkToken, String sPotentialWrong1,
			String sPotentialWrong2, String sPotentialWrong3, int nTag,
			int nTag1, int nTag2, int nTag3, String sSuccesorAll) {
		super();
		this.sMarkToken = sMarkToken;
		this.sPotentialWrong1 = sPotentialWrong1;
		this.sPotentialWrong2 = sPotentialWrong2;
		this.sPotentialWrong3 = sPotentialWrong3;
		this.nTag = nTag;
		this.nTag1 = nTag1;
		this.nTag2 = nTag2;
		this.nTag3 = nTag3;
		this.sSuccesorAll = sSuccesorAll;
	}
	@Id
	@GeneratedValue
	public int getnID() {
		return nID;
	}
	
	public void setnID(int nID) {
		this.nID = nID;
	}
	
	public String getsMarkToken() {
		return sMarkToken;
	}
	public void setsMarkToken(String sMarkToken) {
		this.sMarkToken = sMarkToken;
	}
	public String getsPotentialWrong1() {
		return sPotentialWrong1;
	}
	public void setsPotentialWrong1(String sPotentialWrong1) {
		this.sPotentialWrong1 = sPotentialWrong1;
	}
	public String getsPotentialWrong2() {
		return sPotentialWrong2;
	}
	public void setsPotentialWrong2(String sPotentialWrong2) {
		this.sPotentialWrong2 = sPotentialWrong2;
	}
	public String getsPotentialWrong3() {
		return sPotentialWrong3;
	}
	public void setsPotentialWrong3(String sPotentialWrong3) {
		this.sPotentialWrong3 = sPotentialWrong3;
	}
	public int getnTag() {
		return nTag;
	}
	public void setnTag(int nTag) {
		this.nTag = nTag;
	}
	public int getnTag1() {
		return nTag1;
	}
	public void setnTag1(int nTag1) {
		this.nTag1 = nTag1;
	}
	public int getnTag2() {
		return nTag2;
	}
	public void setnTag2(int nTag2) {
		this.nTag2 = nTag2;
	}
	public int getnTag3() {
		return nTag3;
	}
	public void setnTag3(int nTag3) {
		this.nTag3 = nTag3;
	}
	public String getsSuccesorAll() {
		return sSuccesorAll;
	}
	public void setsSuccesorAll(String sSuccesorAll) {
		this.sSuccesorAll = sSuccesorAll;
	}
}
