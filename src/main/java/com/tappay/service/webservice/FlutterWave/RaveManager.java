package com.tappay.service.webservice.FlutterWave;


import com.tappay.service.webservice.FlutterWave.service.RaveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RaveManager {

    Logger logger= LoggerFactory.getLogger(RaveManager.class);
    public static RaveManager instance;
    private RaveService raveService;

    public static synchronized RaveManager getInstance(){
        if(instance==null){
            instance=new RaveManager();
        }
        return instance;
    }

    public RaveService getRaveService(){
        if(raveService==null){
            raveService=new RaveService();
        }
        return raveService;
    }

}
