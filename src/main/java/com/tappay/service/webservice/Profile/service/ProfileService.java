package com.tappay.service.webservice.Profile.service;

import com.tappay.service.jparepository.Profile.ProfileRepository;
import com.tappay.service.security.Exception.MyException;
import com.tappay.service.webservice.Profile.Model.ProfileModel;
import com.tappay.service.webservice.Profile.ProfileManager;
import com.tappay.service.webservice.Transaction.service.TxService;
import com.tappay.service.webservice.User.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    Logger logger= LoggerFactory.getLogger(ProfileService.class);
    @Autowired
    private ProfileManager profileManager;

    public ProfileModel getProfile(int uid) throws MyException {
        logger.info("Getting profile for user: "+uid);
        try {
            ProfileRepository profileRepository=profileManager.getRepository();
            return profileRepository.findById(String.valueOf(uid)).orElseThrow(()->new MyException("Profile not found"));
        }catch (Exception e){
            logger.error("Error getting profile for user: "+uid);
            throw new MyException("Error getting profile");
        }
    }

    public boolean ifIsCurrentUser(UserModel user, int uid) throws MyException {
        logger.info("Checking if user is current user");
        try {
            return user.getUid()==uid;
        }catch (Exception e){
            logger.error("Error checking if user is current user");
            throw new MyException("Error checking if user is current user");
        }
    }

}
