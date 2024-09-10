package com.tappay.service.webservice.KYC;

import com.tappay.service.jparepository.kyc.KycRepository;
import com.tappay.service.webservice.KYC.Services.KycService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KycManager {

    @Autowired
    private KycService kycService;

    @Autowired
    private KycRepository kycRepository;

    public KycService getKycService() {
        return kycService;
    }

    public KycRepository getKycRepository() {
        return kycRepository;
    }
}
