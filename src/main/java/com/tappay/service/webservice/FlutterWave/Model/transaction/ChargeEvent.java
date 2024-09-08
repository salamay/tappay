package com.tappay.service.webservice.FlutterWave.Model.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChargeEvent{

	@JsonProperty("data")
	private Data data;

	@JsonProperty("event.type")
	private String eventType;

	@JsonProperty("event")
	private String event;

	public Data getData(){
		return data;
	}

	public String getEventType(){
		return eventType;
	}

	public String getEvent(){
		return event;
	}
}