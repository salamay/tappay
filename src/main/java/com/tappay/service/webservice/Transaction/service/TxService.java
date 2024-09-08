package com.tappay.service.webservice.Transaction.service;

import com.tappay.service.reactiverepository.Transaction.ReactiveTxRepository;
import com.tappay.service.security.Exception.MyException;
import com.tappay.service.webservice.Transaction.TxManager;
import com.tappay.service.webservice.Transaction.model.UserTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class TxService {
    @Autowired
    private TxManager txManager;
    Logger logger= LoggerFactory.getLogger(TxService.class);
    public Flux<UserTransaction> getTransactions() throws MyException {
        logger.info("Getting transactions");
        try {
            ReactiveTxRepository txRepository = txManager.getTxRepository();
            return txRepository.findAll();
        }catch (Exception e){
            logger.error("Error getting transactions: "+e.getMessage());
            throw new MyException("Error getting transactions");
        }
    }
}
