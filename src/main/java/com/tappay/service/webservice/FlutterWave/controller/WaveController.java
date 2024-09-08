package com.tappay.service.webservice.FlutterWave.controller;

import com.tappay.service.jparepository.TxLog.TxLogRepository;
import com.tappay.service.jparepository.User.UserRepository;
import com.tappay.service.reactiverepository.Balance.ReactiveBalanceRepository;
import com.tappay.service.reactiverepository.UserTransaction.UserTransactionRepository;
import com.tappay.service.webservice.Balance.context.BalanceContext;
import com.tappay.service.webservice.FlutterWave.Model.transaction.ChargeEvent;
import com.tappay.service.webservice.FlutterWave.RaveManager;
import com.tappay.service.webservice.FlutterWave.service.RaveService;
import com.tappay.service.webservice.Transaction.model.UserTransaction;
import com.tappay.service.webservice.TxLog.TxLog;
import com.tappay.service.webservice.User.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import static com.tappay.service.webservice.utils.Constants.*;

@RestController
public class WaveController {

    String hash="tappay";
    Logger logger= LoggerFactory.getLogger(WaveController.class);

    private static String BAD_REQUEST="Bad Request";
    private static String SUCCESS="Success";
    @Autowired
    private ReactiveBalanceRepository reactiveBalanceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TxLogRepository txLogRepository;
    @Autowired
    private UserTransactionRepository userTransactionRepository;
    @Autowired
    private BalanceContext balanceContext;


    @RequestMapping(value = "/rave_webhook",method = RequestMethod.POST)
    public ResponseEntity<?> raveWebhook(@RequestHeader("Header-Name") String headerValue, @RequestBody ChargeEvent event) {
        logger.info("Webhook call");
        if(headerValue.equals(hash)){
            logger.info("Webhook received from FlutterWave: "+headerValue);
            if (event.getEvent().equals(CHARGE_COMPLETED)){
                updateBalance(event);
                saveTransaction(event);
                return ResponseEntity.ok("Success");
            }else{
                logger.info("Invalid webhook request: "+headerValue);
                return ResponseEntity.badRequest().body("Bad Request");
            }
        }else{
            return ResponseEntity.badRequest().body("Bad Request");
        }
    }

    private void updateBalance(ChargeEvent event){
       try{
           int id=event.getData().getId();
           String tx_ref=event.getData().getTxRef();
           String email=event.getData().getCustomer().getEmail();
           String dateString=event.getData().getCreatedAt();
           double amount=event.getData().getChargedAmount();
           Instant instant = Instant.parse(dateString);
           LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
           RaveService raveService= RaveManager.getInstance().getRaveService();
           if(txLogRepository.findById(String.valueOf(id)).isPresent()){
               logger.info("Transaction already verified: "+tx_ref);
               return;
           }
           raveService.verifyTransaction(String.valueOf(id),s->{
               try {
                   Optional<UserModel> user=userRepository.findByEmail(email);
                   if(user.isPresent()){
                       UserModel u=user.get();
                       reactiveBalanceRepository.updateBalance(u.getUid(), BigDecimal.valueOf(amount))
                               .doOnSuccess(e->{
                                   logger.info("Balance updated: "+tx_ref);
                                   reactiveBalanceRepository.findBalance(u.getUid())
                                           .doOnSuccess(ss->{
                                               reactiveBalanceRepository.findBalance(u.getUid()).doOnSuccess(balance -> {
                                                   balanceContext.sendEvent(balance,u.getUid());
                                               }).subscribe();

                                           })
                                           .subscribe();
                               }).subscribe();
                       logger.info("Saving transaction log: "+tx_ref);
                       TxLog txLog=new TxLog();
                       txLog.setId(id);
                       txLog.setTx_ref(tx_ref);
                       txLogRepository.save(txLog);

                   }else{
                       logger.info("User not found: "+email);
                   }
               }catch (Exception ex){
                   logger.error("Error in updating balance: "+ex.getMessage());
               }
           });
       }catch (Exception e){
              logger.error("Error in updating balance: "+e.getMessage());
       }
    }

    private void saveTransaction(ChargeEvent event){
        try{
            int id=event.getData().getId();
            String tx_ref=event.getData().getTxRef();
            String email=event.getData().getCustomer().getEmail();
            String fullName=event.getData().getCustomer().getName();
            String dateString=event.getData().getCreatedAt();
            String paymentType=event.getData().getPaymentType();
            double amount=event.getData().getChargedAmount();
            Instant instant = Instant.parse(dateString);
            LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            Optional<UserModel> user=userRepository.findByEmail(email);
            if(user.isPresent()){
                UserModel u=user.get();
                logger.info("Saving transaction: "+tx_ref);
                UserTransaction userTransaction=new UserTransaction();
                userTransaction.setId(String.valueOf(id));
                userTransaction.setUid(u.getUid());
                userTransaction.setAmount(String.valueOf(amount));
                userTransaction.setDate(localDate);
                userTransaction.setType(TX_CHARGE);
                userTransaction.setTx_ref(tx_ref);
                userTransaction.setSender(fullName);
                userTransaction.setReceiver(fullName);
                userTransaction.setUser_uid(u.getUid());
                userTransaction.setTransaction_status(COMPLETED);
                userTransaction.setTransfer_fee("0.00");
                if(paymentType.equals(CARD)){
                    userTransaction.setPayment_method(CARD);
                }else if(paymentType.equals(ACCOUNT)){
                    userTransaction.setPayment_method(ACCOUNT);
                }else{
                    userTransaction.setPayment_method("Unknown");
                }
                userTransactionRepository.save(userTransaction).doOnSuccess(e->{
                    logger.info("Transaction saved: "+tx_ref);
                }).subscribe();
            }else{
                logger.info("User not found: "+email);
            }
        }catch (Exception e){
            logger.error("Error in saving transaction: "+e.getMessage());
        }
    }
}
