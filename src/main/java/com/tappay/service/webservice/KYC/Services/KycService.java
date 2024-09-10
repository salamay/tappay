package com.tappay.service.webservice.KYC.Services;

import com.tappay.service.jparepository.kyc.KycRepository;
import com.tappay.service.security.Exception.MyException;
import com.tappay.service.webservice.KYC.Model.Document;
import com.tappay.service.webservice.KYC.Model.Kyc;
import com.tappay.service.webservice.Notification.ENotification;
import com.tappay.service.webservice.Profile.Model.ProfileModel;
import com.tappay.service.webservice.Profile.ProfileManager;
import com.tappay.service.webservice.User.model.UserModel;
import com.tappay.service.webservice.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class KycService {

    Logger logger= LoggerFactory.getLogger(KycService.class);
    private final String doc_path="/documents/";
    @Autowired
    private ProfileManager profileService;
    @Autowired
    private KycRepository kycRepository;
    @Autowired
    private ENotification eNotification;

    public Document uploadDocument(MultipartFile multipartFile) throws MyException {
        try {
            String originalFileName = multipartFile.getOriginalFilename();
            // Get extension of the original file
            assert originalFileName != null;
            Path path = Paths.get(originalFileName);
            String[] extArray = path.getFileName().toString().split("\\.");
            String fileExtension = extArray[extArray.length - 1];
            logger.info("File extension: "+fileExtension);
            String newFileName = UUID.randomUUID()+ "." + fileExtension;
            String dir=System.getProperty("user.dir")+doc_path;
            File file=new File(dir+newFileName);
            boolean status=file.createNewFile();
            if(status){
                Files.write(file.toPath(), multipartFile.getBytes());
                String url= Constants.baseUrl +"/loadDocument/"+newFileName;
                Document document=new Document();
                document.setUrl(url);
                return document;
            }else{
                throw new MyException("unable to upload document");
            }
        } catch (IOException | MyException e) {
            logger.error(e.getMessage());
            throw new MyException("An error occurred while uploading document");
        }
    }


    public Kyc submitKyc(UserModel user, Kyc kyc) throws MyException {
        try {
            int uid=user.getUid();
            Optional<ProfileModel> profile= profileService.getRepository().findById(String.valueOf(uid));
            if (profile.isPresent()){
                ProfileModel p=profile.get();
                kyc.setUid(uid);
                kyc.setUser_uid(uid);
                String[] extArray = kyc.getId_file_url().split("\\.");
                String fileExtension = extArray[extArray.length - 1];
                kyc.setFile_type(fileExtension);
                kyc.setStatus(Constants.KycStatus.Pending.name());
                kycRepository.save(kyc);
                p.setFirst_name(kyc.getFirst_name());
                p.setLast_name(kyc.getLast_name());
                p.setPhone_number(kyc.getPhone_number());
                p.setKyc_id_verification_type(kyc.getId_type());
                p.setKyc_verification_status(Constants.KycStatus.Pending.name());
                p.setKyc_id_verification__url(kyc.getId_file_url());
                profileService.getRepository().save(p);
                eNotification.sendKycEmailSubmittedEmail(p.getEmail(),kyc.getFirst_name(),"KYC Submitted");
            }else{
                throw new MyException("User not found");
            }

            return kyc;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new MyException("An error occurred while submitting kyc");
        }
    }


    public Kyc getKycInformation(int uid) throws MyException {
        try {
            Optional<Kyc> kyc=kycRepository.findById(uid);
            return kyc.orElseGet(Kyc::new);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new MyException("An error occurred while getting kyc information");
        }
    }
}
