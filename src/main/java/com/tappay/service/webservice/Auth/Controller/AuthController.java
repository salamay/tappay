package com.tappay.service.webservice.Auth.Controller;

import com.tappay.service.security.Exception.ErrorModel;
import com.tappay.service.security.Exception.MyException;
import com.tappay.service.security.Jwt.JwtResponse;
import com.tappay.service.security.Jwt.Jwtutils;
import com.tappay.service.webservice.Auth.PlaceHolder.RegistrationModel;
import com.tappay.service.webservice.Auth.PlaceHolder.SignInModel;
import com.tappay.service.webservice.Auth.Service.AuthService;
import com.tappay.service.webservice.Otp.Service.OtpService;
import com.tappay.service.webservice.User.MyUserDetail;
import com.tappay.service.webservice.User.MyUserDetailsService;
import com.tappay.service.webservice.utils.NullFieldChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    Logger logger= LoggerFactory.getLogger(AuthController.class);

    private static String BAD_REQUEST="Bad Request";
    private static String SUCCESS="Success";
    @Autowired
    private Jwtutils jwtutils;
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private AuthService authService;
    @Autowired
    private OtpService otpService;

    @RequestMapping(value = "/hello")
    private String hello()  {
      return "hello";
    }

    @RequestMapping(value = "/getSignInOtp")
    private void getAuthOtp(@RequestAttribute SignInModel signInModel) throws MyException, IllegalAccessException {
        if (!NullFieldChecker.containsNullField(signInModel)){
            MyUserDetail userDetail=(MyUserDetail) myUserDetailsService.loadUserByUsername(signInModel.getEmail());
            if(userDetail!=null){
                authService.sendOtp(signInModel.getEmail());
            }else {
                throw new MyException("User does not exist");
            }
        }
    }

    @RequestMapping(value = "/authenticate",method = RequestMethod.POST)
    public ResponseEntity<?> authenticateUser(@RequestBody SignInModel signInModel) throws IllegalAccessException, MyException {
        if (!NullFieldChecker.containsNullField(signInModel)){
            MyUserDetail userDetail=null;
            try{
                userDetail=(MyUserDetail) myUserDetailsService.loadUserByUsername(signInModel.getEmail());
                String token= authService.authenticate(signInModel,userDetail);
                JwtResponse jwtResponse=new JwtResponse();
                jwtResponse.setToken(token);
                return ResponseEntity.ok(jwtResponse);
            }catch (Exception e){
                throw new MyException("User not found");
            }
        }else{
            return ResponseEntity.badRequest().body(BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getRegOtp")
    public void getRegOtp(@RequestBody RegistrationModel registrationModel) throws MyException, IllegalAccessException {
        if (!NullFieldChecker.containsNullField(registrationModel)){
            authService.sendRegOtp(registrationModel.getEmail());
        }
    }
    @RequestMapping(value = "/registerUser",method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody RegistrationModel registrationModel,@RequestParam String otp) throws IllegalAccessException, MyException {
        if (!NullFieldChecker.containsNullField(registrationModel)){
            String otpCode=otpService.getRegOtp(registrationModel.getEmail());
            if(!otpCode.equals(otp)){
                return ResponseEntity.badRequest().body(new MyException("Invalid OTP"));
            }
            authService.registerUser(registrationModel);
            return ResponseEntity.ok(SUCCESS);
        }else{
            return ResponseEntity.badRequest().body(new ErrorModel(BAD_REQUEST, HttpStatus.BAD_REQUEST.value()));
        }
    }

}
