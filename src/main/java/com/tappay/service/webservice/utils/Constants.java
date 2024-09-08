package com.tappay.service.webservice.utils;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static Map<String,String> currencyMap=new HashMap<>(){{
        put("US","USD");
        put("NG","NGN");
    }};
    public static String CHARGE_COMPLETED="charge.completed";
    public static String CARD="card";
    public static String TX_CHARGE="Charge";
    public static String COMPLETED="Completed";
    public static String ACCOUNT="account";
}
