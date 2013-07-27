package org.fdm.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="experience")
public class Exper {
	int wid;
	int pid;
	String start_year;
	String end_year;
	int stock_id;
	int position;
	int type;
	
	public void setPid(int pid){
		this.pid=pid;
	}
	public void setStock_id(int stock_id){
		this.stock_id=stock_id;
	}
	public void setType(int type){
		this.type=type;
	}
	public void setStart_year(String start_year){
		this.start_year=start_year;
	}
	public void setEnd_year(String end_year){
		this.end_year=end_year;
	}	
	public void setPosition(int position){
		this.position=position;
	}
	
	
	public int getPid(){
		return pid;
	}
	public int getStock_id(){
		return stock_id;
	}
	public int getType(){
		return type;
	}
	public String getStart_year(){
		return start_year;
	}
	public String getEnd_year(){
		return end_year;
	}
	public int getPosition(){
		return position;
	}
	
	@Id
	@GeneratedValue
	public int getWid() {
		return wid;
	}
	public void setWid(int wid) {
		this.wid = wid;
	}
}
