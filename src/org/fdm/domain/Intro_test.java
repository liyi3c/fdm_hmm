package org.fdm.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="intro_raw_test_twice_tokens")
public class Intro_test {
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
