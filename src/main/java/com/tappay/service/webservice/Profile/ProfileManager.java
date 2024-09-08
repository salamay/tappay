package com.tappay.service.webservice.Profile;


import com.tappay.service.jparepository.Profile.ProfileRepository;
import com.tappay.service.webservice.Profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfileManager {
    @Autowired
    private ProfileService service;

    @Autowired
    private ProfileRepository repository;
    public ProfileService getService(){
        return service;
    }
    public ProfileRepository getRepository(){
        return repository;
    }

}
