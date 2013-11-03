package org.fdm.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="all_dict")
public class All_dict {
	String sText;
	int nID;
	String sType;
	String sRemark;
	
	public All_dict(){}
	
	public All_dict(String sText, String sType, String sRemark) {
		super();
		this.sText = sText;
		this.sType = sType;
		this.sRemark = sRemark;
	}
	public String getsText() {
		return sText;
	}
	public void setsText(String sText) {
		this.sText = sText;
	}
	public String getsType() {
		return sType;
	}
	public void setsType(String sType) {
		this.sType = sType;
	}
	public String getsRemark() {
		return sRemark;
	}
	public void setsRemark(String sRemark) {
		this.sRemark = sRemark;
	}

	@Id
	@GeneratedValue
	public int getnID() {
		return nID;
	}
	public void setnID(int nID) {
		this.nID = nID;
	}
}
