package com.tappay.service.webservice.Balance.context;


import com.tappay.service.webservice.Balance.Model.UserBalance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.FluxSink;

import java.util.HashMap;
import java.util.Map;

@Service
public class BalanceContext {

    Logger logger= LoggerFactory.getLogger(BalanceContext.class);
    Map<Integer, FluxSink<Object>> balanceSink=new HashMap<>();

    public void addSink(FluxSink<Object> sink,int uid){
        balanceSink.put(uid,sink);
        logger.info("SINK(NOTIFICATION): Sink added");
        System.out.println("USERS(Balance): "+balanceSink.size());
    }

    public void removeSink(int uid){
        logger.info("SINK(NOTIFICATION): Sink removed");
        balanceSink.remove(uid);
        System.out.println("USERS(Balance): "+balanceSink.size());
    }

    public void sendEvent(UserBalance balance, int uid){
        logger.info("SINK(NOTIFICATION): Sending "+uid+" event");
        FluxSink<Object> sink=balanceSink.get(uid);
        if (sink!=null){
            sink.next(balance);
        }
    }
}
