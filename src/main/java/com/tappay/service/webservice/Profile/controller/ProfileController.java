package com.tappay.service.webservice.Profile.controller;


import com.tappay.service.security.Exception.MyException;
import com.tappay.service.webservice.Profile.Model.ProfileModel;
import com.tappay.service.webservice.Profile.ProfileManager;
import com.tappay.service.webservice.Profile.service.ProfileService;
import com.tappay.service.webservice.Transaction.model.UserTransaction;
import com.tappay.service.webservice.Transaction.service.TxService;
import com.tappay.service.webservice.User.model.UserModel;
import com.tappay.service.webservice.utils.NullFieldChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ProfileController {

    private static String BAD_REQUEST="Bad Request";
    private static String SUCCESS="Success";

    @Autowired
    private ProfileManager profileManager;


    @RequestMapping(value = "/profile")
    public ResponseEntity<?> getTransactions(@RequestAttribute UserModel user) throws IllegalAccessException, MyException {
        if(!NullFieldChecker.containsNullField(user)){
            ProfileService profileService= profileManager.getService();
            ProfileModel profileModel= profileService.getProfile(user);
            return ResponseEntity.ok(profileModel);
        }else{
            throw new MyException("Invalid user");
        }
    }
}
