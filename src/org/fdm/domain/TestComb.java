package org.fdm.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="testcomcomb")
public class TestComb {
	String com1;
	String com2;
	int id;
	public String getCom1(){
		return com1;
	}
	
	public void setCom1(String com1){
		this.com1=com1;
	}
	public String getCom2(){
		return com2;
	}
	
	public void setCom2(String com2){
		this.com2=com2;
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
