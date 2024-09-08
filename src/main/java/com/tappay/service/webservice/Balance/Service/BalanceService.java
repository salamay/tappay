package com.tappay.service.webservice.Balance.Service;

import com.tappay.service.reactiverepository.Balance.ReactiveBalanceRepository;
import com.tappay.service.security.Exception.MyException;
import com.tappay.service.webservice.Balance.Model.UserBalance;
import com.tappay.service.webservice.User.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BalanceService {
    @Autowired
    private ReactiveBalanceRepository reactiveBalanceRepository;

    Logger logger= LoggerFactory.getLogger(BalanceService.class);

    public Mono<UserBalance> getUserBalance(UserModel userModel)throws Exception{
        try{
            return reactiveBalanceRepository.findBalance(userModel.getUid());
        }catch (Exception e){
            logger.error(e.toString());
            throw new MyException("Error getting balance");
        }
    }
}
