package org.fdm.model;

import java.io.Serializable;
import java.util.ArrayList;

public class HmmModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4239206724102445121L;
	private Double cls_cls_prob[][];
	private Double cls_text_prob[][];
	private ArrayList<Integer> clsMap_cls = new ArrayList<Integer>();
	private ArrayList<String> distc_text = new ArrayList<String>();
	public HmmModel(Double[][] cls_cls_prob, Double[][] cls_text_prob,
			ArrayList<Integer> clsMap_cls, ArrayList<String> distc_text) {
		super();
		this.cls_cls_prob = cls_cls_prob;
		this.cls_text_prob = cls_text_prob;
		this.clsMap_cls = clsMap_cls;
		this.distc_text = distc_text;
	}
	public Double[][] getCls_cls_prob() {
		return cls_cls_prob;
	}
	public void setCls_cls_prob(Double[][] cls_cls_prob) {
		this.cls_cls_prob = cls_cls_prob;
	}
	public Double[][] getCls_text_prob() {
		return cls_text_prob;
	}
	public void setCls_text_prob(Double[][] cls_text_prob) {
		this.cls_text_prob = cls_text_prob;
	}
	public ArrayList<Integer> getClsMap_cls() {
		return clsMap_cls;
	}
	public void setClsMap_cls(ArrayList<Integer> clsMap_cls) {
		this.clsMap_cls = clsMap_cls;
	}
	public ArrayList<String> getDistc_text() {
		return distc_text;
	}
	public void setDistc_text(ArrayList<String> distc_text) {
		this.distc_text = distc_text;
	}
}
