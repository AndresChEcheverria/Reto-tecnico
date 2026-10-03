package com.devsu.hackerearth.backend.client.model;

import javax.persistence.Entity;

@Entity
public class Client extends Person {
	private String password;
	private boolean isActive;
	public Client() {
	}

	public Client(String password, boolean isActive) {
		this.password = password;
		this.isActive = isActive;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
