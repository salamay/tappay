package com.tappay.service.webservice.Auth.Service;

import com.tappay.service.jparepository.Profile.ProfileRepository;
import com.tappay.service.jparepository.User.UserRepository;
import com.tappay.service.reactiverepository.Balance.ReactiveBalanceRepository;
import com.tappay.service.security.Exception.MyException;
import com.tappay.service.security.Jwt.Jwtutils;
import com.tappay.service.webservice.Auth.PlaceHolder.RegistrationModel;
import com.tappay.service.webservice.Balance.Model.UserBalance;
import com.tappay.service.webservice.Profile.Model.ProfileModel;
import com.tappay.service.webservice.Auth.PlaceHolder.SignInModel;
import com.tappay.service.webservice.Notification.ENotification;
import com.tappay.service.webservice.Otp.Service.OtpService;
import com.tappay.service.webservice.User.MyUserDetail;
import com.tappay.service.webservice.User.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Optional;
import java.util.Random;

import static com.tappay.service.webservice.utils.Constants.currencyMap;

@Service
public class AuthService {
    Logger logger= LoggerFactory.getLogger(AuthService.class);
    private static String ROLE_USER="ROLE_USER";
    private static String ACCOUNT_ACTIVE="ACCOUNT_ACTIVE";
    private static String ACCOUNT_INACTIVE="ACCOUNT_INACTIVE";
    private static String ACCOUNT_LOCK="ACCOUNT_LOCK";
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ENotification eNotification;
    @Autowired
    private Jwtutils jwtUtils;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReactiveBalanceRepository reactiveBalanceRepository;



    public String authenticate(SignInModel signInModel,MyUserDetail userDetail) throws MyException {
        logger.info("Creating user auth token");
        try{
            if(userDetail!=null){
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInModel.getEmail(), signInModel.getPassword()));
                String token= jwtUtils.generateToken(userDetail,String.valueOf(userDetail.getUid()));
                return token;
            }else {
                return null;
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new MyException("Invalid credentials");
        }
    }
//
//    @Override
//    public boolean checkDevice(SignInModel signInModel) throws MyException {
//        MyUserDetail userDetail=(MyUserDetail) userDetailsService.loadUserByUsername(signInModel.getUsername());
//        if(userDetail!=null){
//            List<String> devices=userDetail.getDeviceLogin();
//            String loggedDevice=devices.stream().filter(e->e.equals(signInModel.getDevice())).findAny().orElse(null);
//            if (loggedDevice!=null){
//                //return true if it has this device
//                return true;
//            }else{
//                return false;
//            }
//        }else {
//            throw new MyException("An error occured while signing in");
//        }
//
//    }



    public void sendOtp(String email) throws MyException {
        logger.info("Sending sign in OTP");
        //Create OTP
        StringBuilder code= new StringBuilder();
        for(int i=0;i<6;i++){
            Random random=new Random();
            code.append(random.nextInt(9));
        }
        try{
            otpService.saveSignInOtp(email,code.toString());
            eNotification.sendOtpEmail(email,code.toString(),"OTP");

        }catch (Exception e){
            logger.error(e.getMessage());
            throw new MyException("Unable to send OTP");
        }
    }
    public void sendRegOtp(String email) throws MyException {
        if(checkIfUserExist(email)){
            throw new MyException("User already exist");
        }
        logger.info("Sending registration in OTP");
        //Create OTP
        StringBuilder code= new StringBuilder();
        for(int i=0;i<6;i++){
            Random random=new Random();
            code.append(random.nextInt(9));
        }
        try{
            otpService.saveRegOtp(email,code.toString());
            eNotification.sendOtpEmail(email,code.toString(),"OTP");

        }catch (Exception e){
            logger.error(e.getMessage());
            throw new MyException("Unable to send OTP");
        }
    }

    @Transactional(transactionManager = "transactionManager")
    public void registerUser(RegistrationModel registrationModel) throws MyException {
        try {
            if(checkIfUserExist(registrationModel.getEmail())){
                throw new MyException("User already exist");
            }
            int uid=generateRandomNumber(9);
            logger.info("Creating user");
            UserModel userModel=new UserModel();
            userModel.setUid(uid);
            userModel.setEmail(registrationModel.getEmail());
            userModel.setPassword(registrationModel.getPassword());
            userModel.setRoles(ROLE_USER);
            userModel.setAccount_status(ACCOUNT_ACTIVE);
            userModel.setEnabled(true);
            userModel.setDevice_token(registrationModel.getDevice_token());
            logger.info("Creating profile");
            ProfileModel profilemodel=new ProfileModel();
            profilemodel.setUid(uid);
            profilemodel.setEmail(registrationModel.getEmail());
            profilemodel.setDate_joined(new Date(System.currentTimeMillis()));
            profilemodel.setAccount_status(ACCOUNT_ACTIVE);
            profilemodel.setUser_uid(uid);
            logger.info("Creating Balance");
            UserBalance userBalance=new UserBalance();
            userBalance.setUid(uid);
            userBalance.setUser_uid(uid);
            userBalance.setBalance(BigDecimal.ZERO);
            userBalance.setCountry_code(registrationModel.getCountry_code());
            userBalance.setCurrency(currencyMap.get(registrationModel.getCountry_code()));
            userRepository.save(userModel);
            profileRepository.save(profilemodel);
            reactiveBalanceRepository.save(userBalance)
                    .subscribe(e->{
                logger.info("User registered successfully");
            });
        }catch (Exception e){
            logger.error(e.toString());
            throw new MyException("Unable to register user");
        }

    }

    public boolean checkIfUserExist(String email) throws MyException {
        logger.info("Checking if user exist");
        try{
            Optional<UserModel> userOptional=userRepository.findByEmail(email);
            return userOptional.isPresent();
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new MyException("Unable to check if user exist");
        }
    }


    public int generateRandomNumber(int length){
        //Create OTP
        StringBuilder code= new StringBuilder();
        for(int i=0;i<length;i++){
            Random random=new Random();
            code.append(random.nextInt(9));
        }
        return Integer.parseInt(code.toString());
    }
}
