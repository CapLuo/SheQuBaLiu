package com.shequ.baliu.util;

import java.util.Comparator;

import com.shequ.baliu.holder.ShequSortModelHolder;

public class PinyinComparator implements Comparator<ShequSortModelHolder> {

	public int compare(ShequSortModelHolder o1, ShequSortModelHolder o2) {
		// 这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
		if (o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}
}