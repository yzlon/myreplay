package com.yzl.test;

import org.springframework.stereotype.Component;

@Component
public class User {
	private String name = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
