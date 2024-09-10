package com.tappay.service.webservice.KYC.Controller;

import com.tappay.service.security.Exception.MyException;
import com.tappay.service.webservice.KYC.KycManager;
import com.tappay.service.webservice.KYC.Model.Document;
import com.tappay.service.webservice.KYC.Model.Kyc;
import com.tappay.service.webservice.KYC.Services.KycService;
import com.tappay.service.webservice.User.model.UserModel;
import com.tappay.service.webservice.utils.NullFieldChecker;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class KycController {

    Logger logger= LoggerFactory.getLogger(KycController.class);
    private final String doc_path="/documents/";
    private static String BAD_REQUEST="BAD REQUEST";
    @Autowired
    private KycManager kycManager;


    @RequestMapping(value = "submitKyc",method = RequestMethod.POST)
    public ResponseEntity<?> submitKyc(@RequestAttribute UserModel user, @RequestBody Kyc kyc) throws MyException, IOException, IllegalAccessException {
        logger.info("Submitting kyc:user:"+user.getUid());
        if (!NullFieldChecker.containsNullField(user)){
            KycService kycService=kycManager.getKycService();
            Kyc submittedKyc=kycService.submitKyc(user,kyc);
            return ResponseEntity.ok(submittedKyc);
        }else {
            throw new MyException("Invalid user");
        }
    }

    @RequestMapping(value = "getKyc",method = RequestMethod.GET)
    public ResponseEntity<?> getKyc(@RequestAttribute UserModel user) throws MyException, IllegalAccessException {
        logger.info("Getting kyc:user:"+user.getUid());
        if (!NullFieldChecker.containsNullField(user)){
            KycService kycService=kycManager.getKycService();
            Kyc submittedKyc=kycService.getKycInformation(user.getUid());
            return ResponseEntity.ok(submittedKyc);
        }else {
            throw new MyException("Invalid user");
        }
    }

    @RequestMapping(value = "uploadDocument",method = RequestMethod.POST)
    public ResponseEntity<?> uploadDocument(@RequestAttribute UserModel user, @RequestBody MultipartFile file) throws MyException, IOException, IllegalAccessException {
        logger.info("Uploading document");
        if (!NullFieldChecker.containsNullField(user)){
            KycService kycService=kycManager.getKycService();
            Document doc=kycService.uploadDocument(file);
            return ResponseEntity.ok(doc);
        }else {
            throw new MyException("Invalid user");
        }
    }


    @RequestMapping(value = "loadDocument/{filename}",method = RequestMethod.GET)
    public void loadDocument(@RequestAttribute UserModel user,@PathVariable String filename, HttpServletResponse response) throws MyException, IllegalAccessException {
        if (!NullFieldChecker.containsNullField(user)){
            if(filename!=null){
                try{
                    response.setContentType("image/png");
                    response.setHeader("Content-Disposition","attachment; filename="+filename);
                    response.setHeader("Content-Transfer-Encoding","binary");
                    File file=new File(System.getProperty("user.dir")+doc_path+filename);
                    BufferedOutputStream bos=new BufferedOutputStream(response.getOutputStream());
                    FileInputStream fis=new FileInputStream(System.getProperty("user.dir")+doc_path+filename);
                    Path path=file.toPath();
                    int len;
                    byte[] buf=new byte[(int) Files.size(path)];
                    while((len=fis.read(buf))>0){
                        bos.write(buf,0,len);
                    }
                    bos.close();
                    response.flushBuffer();
                }catch (Exception e){
                    logger.error(e.getMessage());
                    throw new MyException("Unable to load document");
                }
            }else{
                throw new MyException("Invalid file");
            }
        }else{
            throw new MyException("Invalid user");
        }
    }
}
