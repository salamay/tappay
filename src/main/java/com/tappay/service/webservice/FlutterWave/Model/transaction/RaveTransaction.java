package com.tappay.service.webservice.FlutterWave.Model.transaction;

import com.google.gson.annotations.SerializedName;

public class RaveTransaction{

	@SerializedName("data")
	private Data data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public Data getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public String getStatus(){
		return status;
	}
}