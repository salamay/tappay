package com.tappay.service.webservice.Balance.controller;

import com.tappay.service.security.Exception.MyException;
import com.tappay.service.webservice.Balance.Model.UserBalance;
import com.tappay.service.webservice.Balance.Service.BalanceService;
import com.tappay.service.webservice.Balance.context.BalanceContext;
import com.tappay.service.webservice.User.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BalanceController {

    Logger logger= LoggerFactory.getLogger(BalanceController.class);

    private static String BAD_REQUEST="Bad Request";
    private static String SUCCESS="Success";
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private BalanceContext balanceContext;

    @RequestMapping(value = "/getBalances",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Object getBalances(@RequestAttribute UserModel user) throws Exception {
        if (user.getEmail()!=null){
            logger.info("Getting balance for user: "+user.getEmail());
            Mono<UserBalance> balances=balanceService.getUserBalance(user);
            return Flux.create(sink->{
                balanceContext.addSink(sink,user.getUid());
                balances.map(sink::next).subscribe();
            }).doOnCancel(()->{
                balanceContext.removeSink(user.getUid());
            });
        }else{
            throw new MyException("Invalid user");
        }
    }

}
