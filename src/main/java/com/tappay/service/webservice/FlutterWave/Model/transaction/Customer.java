package com.tappay.service.webservice.FlutterWave.Model.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Customer{

	@JsonProperty("name")
	private String name;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("phone_number")
	private Object phoneNumber;

	@JsonProperty("id")
	private int id;

	@JsonProperty("email")
	private String email;

	public String getName(){
		return name;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public Object getPhoneNumber(){
		return phoneNumber;
	}

	public int getId(){
		return id;
	}

	public String getEmail(){
		return email;
	}
}