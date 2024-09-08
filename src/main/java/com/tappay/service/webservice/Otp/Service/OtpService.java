package com.tappay.service.webservice.Otp.Service;

import com.tappay.service.security.Exception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class OtpService {
    Logger logger= LoggerFactory.getLogger(OtpService.class);
    Map<String,Otp> regOtp;
    Map<String,Otp> signInOtp;
    Map<String,Otp> transactionOtp;


    @Bean
    public void initOtp(){
        regOtp=new HashMap<>();
        signInOtp=new HashMap<>();
        transactionOtp=new HashMap<>();
    }


    public void saveRegOtp(String username,String code){
        logger.info("Saving registration otp for user: "+username);
        System.out.println("OTP:"+code);
        Otp otp=new Otp(code,new Date(System.currentTimeMillis()+1000*60*10));
        regOtp.put(username,otp);
    }


    public String getRegOtp(String username) throws MyException {
        try {
            Otp otp=regOtp.get(username);
            if (otp!=null){
                if(otp.getExpiryDate().after(new Date(System.currentTimeMillis()))){
                    String code=otp.getOtp();
                    regOtp.remove(username);
                    return code;
                }else{
                    throw new MyException("This OTP has expired");
                }
            }else{
                throw new MyException("OTP has been used or does not exist");
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new MyException(e.getMessage());
        }
    }

    public void saveSignInOtp(String username,String code){
        logger.info("saving sign in otp for user: "+username);
        System.out.println("OTP:"+code);
        Otp otp=new Otp(code,new Date(System.currentTimeMillis()+1000*60*10));
        signInOtp.put(username,otp);
    }

    public String getSignInOtp(String username) throws MyException {
        try {
            Otp otp=signInOtp.get(username);
            if (otp!=null){
                if(otp.getExpiryDate().after(new Date(System.currentTimeMillis()))){
                    String code=otp.getOtp();
                    signInOtp.remove(username);
                    return code;
                }else{
                    throw new MyException("This OTP has expired");
                }
            }else{
                throw new MyException("OTP has been used or does not exist");
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new MyException(e.getMessage());
        }
    }


    public void saveTxOtp(String username,String code){
        logger.info("saving transaction in otp for user: "+username);
        System.out.println("OTP:"+code);
        Otp otp=new Otp(code,new Date(System.currentTimeMillis()+1000*60*10));
        transactionOtp.put(username,otp);
    }

    public String getTxOtp(String username) throws MyException {
        try {
            Otp otp=transactionOtp.get(username);
            if (otp!=null){
                if(otp.getExpiryDate().after(new Date(System.currentTimeMillis()))){
                    String code=otp.getOtp();
                    transactionOtp.remove(username);
                    return code;
                }else{
                    throw new MyException("This OTP has expired");
                }
            }else{
                throw new MyException("OTP has been used or does not exist");
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new MyException(e.getMessage());
        }
    }


    public class Otp{
        private String otp;
        private Date expiryDate;

        public Otp(String otp, Date expiryDate) {
            this.otp = otp;
            this.expiryDate = expiryDate;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public Date getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(Date expiryDate) {
            this.expiryDate = expiryDate;
        }
    }
}
