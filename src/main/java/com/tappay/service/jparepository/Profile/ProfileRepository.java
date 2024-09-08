package com.tappay.service.jparepository.Profile;

import com.tappay.service.webservice.Profile.Model.ProfileModel;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<ProfileModel,String> {

}
