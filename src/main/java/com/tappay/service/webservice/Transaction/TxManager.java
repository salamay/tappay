package com.tappay.service.webservice.Transaction;
import com.tappay.service.reactiverepository.Transaction.ReactiveTxRepository;
import com.tappay.service.webservice.Transaction.service.TxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TxManager {

    @Autowired
    public TxService service;
    @Autowired
    public ReactiveTxRepository txRepository;

    public TxService getService() {
        return service;
    }

    public ReactiveTxRepository getTxRepository() {
        return txRepository;
    }

}
