package org.fdm.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="auditor_rawdata")
public class Auditor {
	int id;
	int stkcd;
	int year;
	String date;
	String auditor;
	String name;
	String title;
	String background;
	String backgroundSeg;
	
	public Auditor(){}
	
	public Auditor(int id, int stkcd, int year, String date, String auditor,
			String name, String title, String background, String backgroundSeg) {
		super();
		this.id = id;
		this.stkcd = stkcd;
		this.year = year;
		this.date = date;
		this.auditor = auditor;
		this.name = name;
		this.title = title;
		this.background = background;
		this.backgroundSeg = backgroundSeg;
	}

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStkcd() {
		return stkcd;
	}

	public void setStkcd(int stkcd) {
		this.stkcd = stkcd;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public String getBackgroundSeg() {
		return backgroundSeg;
	}

	public void setBackgroundSeg(String backgroundSeg) {
		this.backgroundSeg = backgroundSeg;
	}
	
}
