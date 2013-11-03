package org.fdm.dao;

public enum Tag {
	DepartmentTag("777"),
	
	LocationTag("881"),
	
	CompanyTag("888"),
	CompanyPriorTag("8881"),
	CompanyPriorSpecialTag("8889"),
	
	AwdCompanyTag("889"),
	AwdCompanyPriorTag("8891"),
	
	DegreeTag("991"),
	
	PositionTag("999");
	
	private int tag;
	
	private Tag(String tag) {
		this.tag = Integer.parseInt(tag);
	}
	
	@Override
	public String toString() {
		return String.valueOf(tag);
	}
	
	public static boolean contains(int tag) {
		for (Tag t : Tag.values()) {
			if (t.tagValue() == tag)
				return true;
		}
		return false;
	}
	
	public int tagValue() {
		return tag;
	}
}
