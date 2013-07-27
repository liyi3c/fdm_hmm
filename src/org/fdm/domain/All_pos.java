package org.fdm.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="allpos")
public class All_pos {
	String position;
	int id;
	public String getPosition(){
		return position;
	}
	
	public void setPosition(String position){
		this.position=position;
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
