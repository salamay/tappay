package com.tappay.service.webservice.Transaction.context;


import com.tappay.service.webservice.Balance.Model.UserBalance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.FluxSink;

import java.util.HashMap;
import java.util.Map;

@Service
public class TxContext {

    Logger logger= LoggerFactory.getLogger(TxContext.class);
    Map<Integer, FluxSink<Object>> txSink=new HashMap<>();

    public void addSink(FluxSink<Object> sink,int uid){
        txSink.put(uid,sink);
        logger.info("SINK(NOTIFICATION): Sink added");
        System.out.println("USERS(TRANSACTION): "+txSink.size());
    }

    public void removeSink(int uid){
        logger.info("SINK(NOTIFICATION): Sink removed");
        txSink.remove(uid);
        System.out.println("USERS(TRANSACTION): "+txSink.size());
    }

    public void sendEvent(UserBalance balance, int uid){
        logger.info("SINK(NOTIFICATION): Sending "+uid+" transaction event");
        FluxSink<Object> sink=txSink.get(uid);
        if (sink!=null){
            sink.next(balance);
        }
    }
}
