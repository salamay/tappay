package com.tappay.service.webservice.FlutterWave.Model.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Account{

	@JsonProperty("account_name")
	private String accountName;

	public String getAccountName(){
		return accountName;
	}
}