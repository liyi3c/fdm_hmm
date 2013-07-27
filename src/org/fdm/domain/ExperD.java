package org.fdm.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="experience_detail")
public class ExperD {
	int id;
	int wid;
	int pid;
	String position_classi;
	String start_year;
	String end_year;
	String finish_year;
	int stock_id;
	String position;
	String rank;
	
	public void setWid(int wid){
		this.wid=wid;
	}
	public void setPid(int pid){
		this.pid=pid;
	}
	public void setPosition_classi(String position_classi){
		this.position_classi=position_classi;
	}
	public void setStock_id(int stock_id){
		this.stock_id=stock_id;
	}
	public void setRank(String rank){
		this.rank=rank;
	}
	public void setStart_year(String start_year){
		this.start_year=start_year;
	}
	public void setFinish_year(String finish_year){
		this.finish_year=finish_year;
	}
	public void setEnd_year(String end_year){
		this.end_year=end_year;
	}
	public void setPosition(String position){
		this.position=position;
	}
	
	
	
	public int getWid(){
		return wid;
	}
	public int getPid(){
		return pid;
	}
	public String getPosition_classi(){
		return position_classi;
	}
	public int getStock_id(){
		return stock_id;
	}
	public String getRank(){
		return rank;
	}
	public String getStart_year(){
		return start_year;
	}
	public String getEnd_year(){
		return end_year;
	}
	public String getFinish_year(){
		return finish_year;
	}
	public String getPosition(){
		return position;
	}
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
