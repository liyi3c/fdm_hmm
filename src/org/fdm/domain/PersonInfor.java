package org.fdm.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="allinformation3")
public class PersonInfor {
	int id;
	int type;
	int pid;
	String finish_year;
	String name;
	int age;
	
	String rank;
	int stock_id;
	String position_classi;
	String position;
	String gender;
	int education;
	String title;
	String begin_year;
	String end_year;
	String introduction;
	String introductionSeg;
	String note;
	int if_pay;
	int total;
	int allowance;
	int stock_num;
	int if_mark;
	public String getFinish_year() {
		return finish_year;
	}
	public void setFinish_year(String finish_year) {
		this.finish_year = finish_year;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getRank(){
		return rank;
	}
	public void setRank(String rank){
		this.rank=rank;
	}
	public int getStock_id() {
		return stock_id;
	}
	public void setStock_id(int stock_id) {
		this.stock_id = stock_id;
	}
	public String getPosition_classi() {
		return position_classi;
	}
	public void setPosition_classi(String position_classi) {
		this.position_classi = position_classi;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getEducation() {
		return education;
	}
	public void setEducation(int education) {
		this.education = education;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBegin_year() {
		return begin_year;
	}
	public void setBegin_year(String begin_year) {
		this.begin_year = begin_year;
	}
	public String getEnd_year() {
		return end_year;
	}
	public void setEnd_year(String end_year) {
		this.end_year = end_year;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getIntroductionSeg() {
		return introductionSeg;
	}
	public void setIntroductionSeg(String introductionSeg) {
		this.introductionSeg = introductionSeg;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public int getIf_pay() {
		return if_pay;
	}
	public void setIf_pay(int if_pay) {
		this.if_pay = if_pay;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getAllowance() {
		return allowance;
	}
	public void setAllowance(int allowance) {
		this.allowance = allowance;
	}
	public int getStock_num() {
		return stock_num;
	}
	public void setStock_num(int stock_num) {
		this.stock_num = stock_num;
	}
	public int getIf_mark() {
		return if_mark;
	}
	public void setIf_mark(int if_mark) {
		this.if_mark = if_mark;
	}

	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
