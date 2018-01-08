package com.zx;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * author:ZhengXing
 * datetime:2018/1/8 0008 14:05
 */
public class Test {

	@org.junit.Test
	public void test() {
		HashMap<String, String> a = new HashMap<>();
		a.put("a", "1");
		a.put("b", "2");
		System.out.println(a.keySet());
		a.put("c", "3");
		System.out.println(a.keySet());

	}
}
