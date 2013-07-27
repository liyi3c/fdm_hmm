package org.fdm.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="allcom")
public class All_com {
	String company;
	int id;
	public String getCompany(){
		return company;
	}
	
	public void setCompany(String company){
		this.company=company;
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
