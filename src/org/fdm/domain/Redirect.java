package org.fdm.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="redirect")
public class Redirect {
	int id;
	String title;
	String rd_title;
	
	public String getTitle(){
		return title;
	}
	
	public String getRd_title(){
		return rd_title;
	}
	
	public void setTitle(String title){
		this.title=title;
	}
	public void setRd_title(String rd_title){
		this.rd_title=rd_title;
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
