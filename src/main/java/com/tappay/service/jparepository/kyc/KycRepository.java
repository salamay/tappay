package com.tappay.service.jparepository.kyc;

import com.tappay.service.webservice.KYC.Model.Kyc;
import org.springframework.data.repository.CrudRepository;

public interface KycRepository extends CrudRepository<Kyc,Integer> {
}
