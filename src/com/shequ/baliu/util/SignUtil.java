package com.shequ.baliu.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SignUtil {

	public static String getSignature(String timestamp,
			String nonce) {
		String[] arr = new String[] { StaticVariableSet.IM_SERVER_TOKEN, timestamp, nonce };

		// 将 token、timestamp、nonce 三个参数进行字典序排序
		Arrays.sort(arr);

		StringBuilder content = new StringBuilder();
		for (String str : arr) {
			content.append(str);
		}
		MessageDigest md = null;
		String tmpStr = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			// 将三个参数字符串拼接成一个字符串进行 sha1 加密
			byte[] digest = md.digest(content.toString().getBytes());
			tmpStr = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return tmpStr;
	}

	private static String byteToStr(byte[] digest) {
		String strDigest = "";
		for (int i = 0; i < digest.length; i++) {
			strDigest += byteToHexStr(digest[i]);
		}
		return strDigest;
	}

	private static String byteToHexStr(byte b) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(b >>> 4) & 0X0F];
		tempArr[1] = Digit[b & 0X0F];

		String s = new String(tempArr);
		return s;
	}
}
