package org.fdm.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="relation_company_hama")
public class RelationComHama {
	int id;
	int stock_id1;
	int stock_id2;
	double weight;
	@Id
	@GeneratedValue
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id=id;
	}
	public int getStock_id1() {
		return stock_id1;
	}
	public void setStock_id1(int stock_id1) {
		this.stock_id1 = stock_id1;
	}
	public int getStock_id2() {
		return stock_id2;
	}
	public void setStock_id2(int stock_id2) {
		this.stock_id2 = stock_id2;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
}
