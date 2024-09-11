package com.tappay.service.webservice.Transaction.service;

import com.tappay.service.reactiverepository.Balance.ReactiveBalanceRepository;
import com.tappay.service.reactiverepository.Transaction.ReactiveTxRepository;
import com.tappay.service.security.Exception.MyException;
import com.tappay.service.webservice.Balance.Model.UserBalance;
import com.tappay.service.webservice.Balance.Service.BalanceService;
import com.tappay.service.webservice.Balance.context.BalanceContext;
import com.tappay.service.webservice.Profile.Model.ProfileModel;
import com.tappay.service.webservice.Profile.ProfileManager;
import com.tappay.service.webservice.Profile.service.ProfileService;
import com.tappay.service.webservice.Transaction.TxManager;
import com.tappay.service.webservice.Transaction.context.TxContext;
import com.tappay.service.webservice.Transaction.model.Cash;
import com.tappay.service.webservice.Transaction.model.UserTransaction;
import com.tappay.service.webservice.User.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.tappay.service.webservice.utils.Constants.*;

@Service
public class TxService {
    Logger logger= LoggerFactory.getLogger(TxService.class);
    @Autowired
    private TxManager txManager;
    @Autowired
    private ProfileManager profileManager;
    @Autowired
    private ReactiveBalanceRepository reactiveBalanceRepository;
    @Autowired
    private BalanceContext balanceContext;
    @Autowired
    private BalanceService balanceService;

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

    public UserTransaction sendTransaction(UserModel user, Cash cash) throws MyException {
        logger.info("Sending cash from "+user.getUid()+" to "+cash.getTo());
        try {
            int receiverId=Integer.parseInt(cash.getTo());
            ProfileService profileService= profileManager.getService();
            if (profileService.ifIsCurrentUser(user,receiverId)){
                throw new MyException("Cannot send cash to self");
            }else{
                BigDecimal amount=BigDecimal.valueOf(Double.parseDouble(cash.getAmount()));
                if(balanceService.checkBalance(user.getUid(),amount.doubleValue())){
                    UserTransaction tx=saveLocalSenderTransaction(user,cash);
                    saveLocalReceiverTransaction(user,cash);
                    reactiveBalanceRepository.deductBalance(user.getUid(),amount).block();
                    UserBalance senderBalance=reactiveBalanceRepository.findBalance(user.getUid()).block();
                    assert senderBalance != null;
                    logger.info("Sender balance deducted: New balance "+senderBalance.getBalance());
                    balanceContext.sendEvent(senderBalance,user.getUid());
                    reactiveBalanceRepository.updateBalance(receiverId,amount).block();
                    UserBalance receiverBalance=reactiveBalanceRepository.findBalance(receiverId).block();
                    assert receiverBalance != null;
                    logger.info("Receiver balance deducted: New balance "+receiverBalance.getBalance());
                    balanceContext.sendEvent(receiverBalance,receiverId);
                    return tx;
                }else{
                    throw new MyException("Insufficient balance");
                }
            }

        }catch (Exception e){
            logger.error(e.getMessage());
            throw new MyException(e.getMessage());
        }
    }

    private UserTransaction saveLocalSenderTransaction(UserModel user,Cash cash) throws MyException {
        try {
            int receiverId=Integer.parseInt(cash.getTo());
            LocalDate date=LocalDate.now();
            ReactiveTxRepository txRepository = txManager.getTxRepository();
            ProfileService profileService= profileManager.getService();
            ProfileModel receiver=profileService.getProfile(receiverId);
            ProfileModel sender=profileService.getProfile(user.getUid());
            String senderName=sender.getLast_name()+" "+sender.getFirst_name();
            String receiverName=receiver.getLast_name()+" "+receiver.getFirst_name();
            UserTransaction senderTx=new UserTransaction();
            senderTx.setUid(sender.getUid());
            senderTx.setUser_uid(sender.getUid());
            senderTx.setAmount(cash.getAmount());
            senderTx.setCurrency(cash.getCurrency());
            senderTx.setType(TRANSFER);
            senderTx.setNarration(cash.getNarration());
            senderTx.setTransfer_fee(TX_FEE);
            senderTx.setPayment_method(TAP_PAY);
            senderTx.setDate(date);
            senderTx.setSender(senderName);
            senderTx.setReceiver(receiverName);
            senderTx.setTransaction_status(COMPLETED);
            txRepository.save(senderTx).doOnSuccess(e->{
                logger.info("Sender transaction saved");
                TxContext txContext=txManager.getTxContext();
                txContext.sendEvent(senderTx,user.getUid());
            }).subscribe();
            return senderTx;
        }catch (Exception e){
            logger.info(e.getMessage());
            throw new MyException("Unable to save receiver transaction");
        }
    }
    private UserTransaction saveLocalReceiverTransaction(UserModel user,Cash cash) throws MyException {
        try {
            int receiverId=Integer.parseInt(cash.getTo());
            LocalDate date=LocalDate.now();
            ReactiveTxRepository txRepository = txManager.getTxRepository();
            ProfileService profileService= profileManager.getService();
            ProfileModel receiver=profileService.getProfile(receiverId);
            ProfileModel sender=profileService.getProfile(user.getUid());
            UserTransaction receiverTx=new UserTransaction();
            receiverTx.setUid(receiver.getUid());
            receiverTx.setUser_uid(receiver.getUid());
            receiverTx.setAmount(cash.getAmount());
            receiverTx.setCurrency(cash.getCurrency());
            receiverTx.setType(TRANSFER);
            receiverTx.setTransfer_fee(TX_FEE);
            receiverTx.setPayment_method(TAP_PAY);
            receiverTx.setNarration(cash.getNarration());
            receiverTx.setDate(date);
            if(receiver.getKyc_verification_status().equals(KYC_VERIFIED)){
                String receiverName=receiver.getLast_name()+" "+receiver.getFirst_name();
                receiverTx.setReceiver(receiverName);
            }
            if(sender.getKyc_verification_status().equals(KYC_VERIFIED)){
                String senderName=sender.getLast_name()+" "+sender.getFirst_name();
                receiverTx.setSender(senderName);
            }
            receiverTx.setTransaction_status(COMPLETED);
            txRepository.save(receiverTx).doOnSuccess(e->{
                logger.info("Receiver transaction saved");
                TxContext txContext=txManager.getTxContext();
                txContext.sendEvent(receiverTx,receiverId);
            }).subscribe();
            return receiverTx;
        }catch (Exception e){
            logger.info(e.getMessage());
            throw new MyException("Unable to save receiver transaction");
        }
    }

}
