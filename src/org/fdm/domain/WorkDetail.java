package org.fdm.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="all_work_detail")
public class WorkDetail {
	int id;
	int cid = -1;
	String position_classi;
	Integer rank;
	String start ="0000-00-00";
	String end_year = "0000-00-00";
	String department = "";
	String position = "";

	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPosition_classi(){
		return position_classi;
	}
	public void setPosition_classi(String position_classi){
		this.position_classi=position_classi;
	}
	public Integer getRank(){
		return rank;
	}
	public void setRank(Integer rank){
		this.rank=rank;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd_year() {
		return end_year;
	}
	public void setEnd_year(String end_year) {
		this.end_year = end_year;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}


	

}
