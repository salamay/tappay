package com.tappay.service.webservice.Transaction.controller;


import com.tappay.service.security.Exception.MyException;
import com.tappay.service.webservice.FlutterWave.controller.WaveController;
import com.tappay.service.webservice.Transaction.TxManager;
import com.tappay.service.webservice.Transaction.context.TxContext;
import com.tappay.service.webservice.Transaction.model.Cash;
import com.tappay.service.webservice.Transaction.model.UserTransaction;
import com.tappay.service.webservice.Transaction.service.TxService;
import com.tappay.service.webservice.User.model.UserModel;
import com.tappay.service.webservice.utils.NullFieldChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
public class TransactionController {

    Logger logger= LoggerFactory.getLogger(TransactionController.class);

    private static String BAD_REQUEST="Bad Request";
    private static String SUCCESS="Success";
    @Autowired
    private TxManager txManager;
    @Autowired
    private TxContext txContext;


    @RequestMapping(value = "/transactions",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Object getTransactions(@RequestAttribute UserModel user) throws IllegalAccessException, MyException {
        if(!NullFieldChecker.containsNullField(user)){
            TxService txService= txManager.getService();
            Flux<UserTransaction> tx=txService.getTransactions();
            return Flux.create(sink -> {
                txContext.addSink(sink, user.getUid());
                tx.map(sink::next).subscribe();
            }).doOnCancel(()->
                    txContext.removeSink(user.getUid())
            );
        }else{
            throw new MyException("Invalid user");
        }
    }


    @RequestMapping(value = "/sendCash",method = RequestMethod.POST)
    public ResponseEntity<?> sendCash(@RequestAttribute UserModel user, @RequestBody Cash cash) throws IllegalAccessException, MyException {
        if(!NullFieldChecker.containsNullField(user)){
            TxService txService= txManager.getService();
            UserTransaction tx=txService.sendTransaction(user,cash);
            return ResponseEntity.ok(tx);
        }else{
            throw new MyException("Invalid user");
        }
    }
}
