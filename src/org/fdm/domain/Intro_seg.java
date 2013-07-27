package org.fdm.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="intro")
public class Intro_seg {
	String introduction;
	int id;
	public String getIntroduction(){
		return introduction;
	}
	
	public void setIntroduction(String introduction){
		this.introduction=introduction;
	}
	
	@Id
	@GeneratedValue
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id=id;
	}
}
