package org.fdm.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="companyinfor")
public class ComBasic {
	int Id;
	int Stkcd;
	int Crcd;
	String Stknme;
	int Indcd;
	String Indnme;
	String Nindcd;
	String Nindnme;
	int Listexg;
	String Conme;
	String Conmee;
	Date Estbdt;
	Date Ipodt;
	Date Listdt;
	String Regadd;
	String Regplc;
	int Regcap1;
	int Regcap2;
	int Nstaff;
	int Ndrct;
	int Nspv;

	public int getStkcd(){
		return Stkcd;
	}
	public void setStkcd(int Stkcd){
		this.Stkcd = Stkcd;
	}
	
	public int getCrcd(){
		return Crcd;
	}
	public void setCrcd(int Crcd){
		this.Crcd = Crcd;
	}
	
	public String getStknme(){
		return Stknme;
	}
	public void setStknme(String Stknme){
		this.Stknme = Stknme;
	}

	public int getIndcd(){
		return Indcd;
	}
	public void setIndcd(int Indcd){
		this.Indcd = Indcd;
	}

	public String getIndnme(){
		return Indnme;
	}
	public void setIndnme(String Indnme){
		this.Indnme = Indnme;
	}

	public String getNindcd(){
		return Nindcd;
	}
	public void setNindcd(String Nindcd){
		this.Nindcd = Nindcd;
	}
	
	public String getNindnme(){
		return Nindnme;
	}
	public void setNindnme(String Nindnme){
		this.Nindnme = Nindnme;
	}

	public int getListexg(){
		return Listexg;
	}
	public void setListexg(int Listexg){
		this.Listexg = Listexg;
	}

	public String getConme(){
		return Conme;
	}
	public void setConme(String Conme){
		this.Conme = Conme;
	}

	public String getConmee(){
		return Conmee;
	}
	public void setConmee(String Conmee){
		this.Conmee = Conmee;
	}

	public Date getEstbdt(){
		return Estbdt;
	}
	public void setEstbdt(Date Estbdt){
		this.Estbdt = Estbdt;
	}

	public Date getIpodt(){
		return Ipodt;
	}
	public void setIpodt(Date Ipodt){
		this.Ipodt = Ipodt;
	}

	public Date getListdt(){
		return Listdt;
	}
	public void setListdt(Date Listdt){
		this.Listdt = Listdt;
	}
		
	public String getRegadd(){
		return Regadd;
	}
	public void setRegadd(String Regadd){
		this.Regadd = Regadd;
	}

	public String getRegplc(){
		return Regplc;
	}
	public void setRegplc(String Regplc){
		this.Regplc =Regplc;
	}

	public int getRegcap1(){
		return Regcap1;
	}
	public void setRegcap1(int Regcap1){
		this.Regcap1 = Regcap1;
	}

	public int getRegcap2(){
		return Regcap2;
	}
	public void setRegcap2(int Regcap2){
		this.Regcap2 = Regcap2;
	}

	public int getNstaff(){
		return Nstaff;
	}
	public void setNstaff(int Nstaff){
		this.Nstaff = Nstaff;
	}

	public int getNdrct(){
		return Ndrct;
	}
	public void setNdrct(int Ndrct){
		this.Ndrct = Ndrct;
	}

	public int getNspv(){
		return Nspv;
	}
	public void setNspv(int Nspv){
		this.Nspv = Nspv;
	}
	
	//////////////////////////////////////////////
	
	@Id
	@GeneratedValue
	public int getId(){
		return Id;
	}
	public void setId(int Id){
		this.Id = Id;
	}

}
