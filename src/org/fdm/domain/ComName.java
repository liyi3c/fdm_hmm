package org.fdm.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="comid")
public class ComName {
	String Conme;
	int Stkcd;
	public String getConme(){
		return Conme;
	}
	
	public void setConme(String Conme){
		this.Conme=Conme;
	}
	
	@Id
	@GeneratedValue
	public int getStkcd(){
		return Stkcd;
	}
	public void setStkcd(int Stkcd){
		this.Stkcd=Stkcd;
	}
}
