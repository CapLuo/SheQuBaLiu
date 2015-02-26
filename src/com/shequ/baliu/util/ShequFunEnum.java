package com.shequ.baliu.util;

public enum ShequFunEnum {
	NONE, // 其他
	NEIGHBOUR, // 邻里圈
	SECONDHAND, // 社区二手交易
	REPAIR, // 维修
	EXPRESS, // 快递
	DISTRIBUTION, // 社区配送
	PROPERTY, // 物业
	TOGHTER, // 爱拼一族
	MEDIA, // 社区媒体
	BANKING, // 票查询
	;

	public static ShequFunEnum getIntToEnum(int value) {
		switch (value) {
		case 0:
			return NEIGHBOUR;
		case 1:
			return SECONDHAND;
		case 2:
			return REPAIR;
		case 3:
			return EXPRESS;
		case 4:
			return DISTRIBUTION;
		case 5:
			return PROPERTY;
		case 6:
			return TOGHTER;
		case 7:
			return MEDIA;
		case 8:
			return BANKING;
		default:
			break;
		}
		return NONE;
	}

	public static int getEnumToInt(ShequFunEnum fun) {
		switch (fun) {
		case NEIGHBOUR:
			return 0;
		case SECONDHAND:
			return 1;
		case REPAIR:
			return 2;
		case EXPRESS:
			return 3;
		case DISTRIBUTION:
			return 4;
		case PROPERTY:
			return 5;
		case TOGHTER:
			return 6;
		case MEDIA:
			return 7;
		case BANKING:
			return 8;
		default:
			break;
		}
		return -1;
	}
}
