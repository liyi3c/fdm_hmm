package org.fdm.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="work_relation")
public class WorkRelation {
	int rid;
	int cid1;
	int pid1;
	int cid2;
	int pid2;
	int xflag;
	double weight;
	//int stock_id;
	String company;
	String start_date;
	String end_date;

	public void setCid1(int cid1){
		this.cid1=cid1;
	}
	public void setCid2(int cid2){
		this.cid2=cid2;
	}
	public void setXflag(int xflag){
		this.xflag=xflag;
	}
//	public void setStock_id(int stock_id){
//		this.stock_id=stock_id;
//	}
	public void setPid1(int pid1){
		this.pid1=pid1;
	}
	public void setPid2(int pid2){
		this.pid2=pid2;
	}
	public void setCompany(String company){
		this.company=company;
	}
	public void setStart_date(String start_date){
		this.start_date=start_date;
	}
	public void setEnd_date(String end_date){
		this.end_date=end_date;
	}
	public void setWeight(double weight){
		this.weight = weight;
	}
	
	public int getCid1(){
		return cid1;
	}
	public int getXflag(){
		return xflag;
	}
	public int getCid2(){
		return cid2;
	}
	public int getPid1(){
		return pid1;
	}
	public int getPid2(){
		return pid2;
	}
//	public int getStock_id(){
//		return stock_id;
//	}
	public String getCompany(){
		return company;
	}
	public String getStart_date(){
		return start_date;
	}
	public String getEnd_date(){
		return end_date;
	}
	public double getWeight(){
		return weight;
	}
	@Id
	@GeneratedValue
	public int getRid(){
		return rid;
	}
	public void setRid(int rid){
		this.rid=rid;
	}
}
