package com.tappay.service.webservice.FlutterWave.Model.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Card{

	@JsonProperty("country")
	private String country;

	@JsonProperty("first_6digits")
	private String first6digits;

	@JsonProperty("last_4digits")
	private String last4digits;

	@JsonProperty("expiry")
	private String expiry;

	@JsonProperty("type")
	private String type;

	@JsonProperty("issuer")
	private String issuer;

	public String getCountry(){
		return country;
	}

	public String getFirst6digits(){
		return first6digits;
	}

	public String getLast4digits(){
		return last4digits;
	}

	public String getExpiry(){
		return expiry;
	}

	public String getType(){
		return type;
	}

	public String getIssuer(){
		return issuer;
	}
}