package com.shequ.baliu.holder;

import android.util.Log;

public class ShequSortModelHolder {

	private String groupId;
	private String name;
	private String sortLetters;

	public ShequSortModelHolder() {
	}

	public void setGroupid(String id) {
		this.groupId = id;
	}

	public String getId() {
		return groupId;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
