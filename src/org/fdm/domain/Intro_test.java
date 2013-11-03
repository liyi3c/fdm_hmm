package org.fdm.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="intro_sae_stf_seg_test")
public class Intro_test {
	String introduction;
	int pid;
	public String getIntroduction(){
		return introduction;
	}
	
	public void setIntroduction(String introduction){
		this.introduction=introduction;
	}
	
	@Id
	@GeneratedValue
	public int getPid(){
		return pid;
	}
	public void setPid(int pid){
		this.pid=pid;
	}
}
