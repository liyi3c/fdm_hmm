package org.fdm.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="all_dict_tokens")
public class AllDictTokens {
	String sText;
	String sTextTokens;
	int nID;
	String sType;
	String sRemark;
	
	public String getsText() {
		return sText;
	}
	public void setsText(String sText) {
		this.sText = sText;
	}
	public String getsTextTokens() {
		return sTextTokens;
	}
	public void setsTextTokens(String sTextTokens) {
		this.sTextTokens = sTextTokens;
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
