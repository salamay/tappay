package com.tappay.service.jparepository.User;

import com.tappay.service.webservice.User.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel,String> {
    @Query(value = "select * from user where email=?1",nativeQuery = true)
    Optional<UserModel> findByEmail(String email);
}
