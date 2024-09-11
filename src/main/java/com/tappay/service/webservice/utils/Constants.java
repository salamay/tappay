package com.tappay.service.webservice.utils;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final String baseUrl="http://localhost:323";

    public static Map<String,String> currencyMap=new HashMap<>(){{
        put("US","USD");
        put("NG","NGN");
    }};
    public static String CHARGE_COMPLETED="charge.completed";
    public static String CARD="card";
    public static String TX_CHARGE="Charge";
    public static String TRANSFER="Transfer";
    public static String TX_FEE = "0.00";
    public static String TAP_PAY = "TapPay";
    public static String COMPLETED="Completed";
    public static String ACCOUNT="account";
    public static String KYC_VERIFIED="VERIFIED";
    public enum KycStatus{
        Pending,Verified,Failed
    }
}
