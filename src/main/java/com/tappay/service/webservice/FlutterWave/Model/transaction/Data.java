package com.tappay.service.webservice.FlutterWave.Model.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Data{

	@JsonProperty("device_fingerprint")
	private String deviceFingerprint;

	@JsonProperty("tx_ref")
	private String txRef;

	@JsonProperty("amount")
	private int amount;

	@JsonProperty("flw_ref")
	private String flwRef;

	@JsonProperty("ip")
	private String ip;

	@JsonProperty("auth_model")
	private String authModel;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("merchant_fee")
	private int merchantFee;

	@JsonProperty("payment_type")
	private String paymentType;

	@JsonProperty("account_id")
	private int accountId;

	@JsonProperty("charged_amount")
	private int chargedAmount;

	@JsonProperty("narration")
	private String narration;

	@JsonProperty("currency")
	private String currency;

	@JsonProperty("app_fee")
	private int appFee;

	@JsonProperty("processor_response")
	private String processorResponse;

	@JsonProperty("id")
	private int id;

	@JsonProperty("card")
	private Card card;
	@JsonProperty("account")
	private Account account;
	@JsonProperty("status")
	private String status;

	@JsonProperty("customer")
	private Customer customer;

	public String getDeviceFingerprint(){
		return deviceFingerprint;
	}

	public String getTxRef(){
		return txRef;
	}

	public int getAmount(){
		return amount;
	}

	public String getFlwRef(){
		return flwRef;
	}

	public String getIp(){
		return ip;
	}

	public String getAuthModel(){
		return authModel;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getMerchantFee(){
		return merchantFee;
	}

	public String getPaymentType(){
		return paymentType;
	}

	public int getAccountId(){
		return accountId;
	}

	public int getChargedAmount(){
		return chargedAmount;
	}

	public String getNarration(){
		return narration;
	}

	public String getCurrency(){
		return currency;
	}

	public int getAppFee(){
		return appFee;
	}

	public String getProcessorResponse(){
		return processorResponse;
	}

	public int getId(){
		return id;
	}

	public Card getCard(){
		return card;
	}
	public Account getAccount(){
		return account;
	}


	public String getStatus(){
		return status;
	}

	public Customer getCustomer(){
		return customer;
	}
}