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

    @Autowired
    private ProfileManager profileManager;
    Logger logger= LoggerFactory.getLogger(ProfileService.class);

    public ProfileModel getProfile(UserModel user) throws MyException {
        logger.info("Getting profile for user: "+user.getUid());
        try {
            ProfileRepository profileRepository=profileManager.getRepository();
            return profileRepository.findById(String.valueOf(user.getUid())).orElseThrow(()->new MyException("Profile not found"));
        }catch (Exception e){
            logger.error("Error getting profile for user: "+user.getUid());
            throw new MyException("Error getting profile");
        }
    }

}
