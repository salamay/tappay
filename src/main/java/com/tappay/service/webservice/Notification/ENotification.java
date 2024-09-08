package com.tappay.service.webservice.Notification;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import com.tappay.service.security.Exception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ENotification {
    private final String from="viola@wordlrad.xyz";
    private final String replyTo="viola@wordlrad.xyz";
    private final String welcomeEmailTemplate="d-f8403663457c4d12a445e8894b18c053";
    private final String otpEmailTemplate="d-e00d4e6a26df4b7ca7e39c76e34ad619";
    private final String incomingPaymentTemplate="d-abf67edca168414887f693a403add3b7";
    private final String outgoingPaymentTemplate="d-e37dcea3c97246a1828cc37a2600104f";
    private final String withdrawalConfirmedTemplate="d-bbb4b3a6f6aa470e98f199883978a1e8";
    private final String depositConfirmedTemplate="d-c4ae5c9d1ac54a7da4735588d39eebff";
    private final String kycSubmittedTemplate="d-c563208894a34bdea76c10a93f4b58d9";
    SendGrid sg = new SendGrid(System.getenv("SG"));
    private Logger logger= LoggerFactory.getLogger(ENotification.class);

    public void sendWelcomeEmail(String to,String receiverName,String subject) throws IOException, MyException {
        try {
            Personalization personalization = new Personalization();
            Mail mail = new Mail();
            mail.setTemplateId(welcomeEmailTemplate);
            personalization.addTo(new Email(to,receiverName));
            personalization.addDynamicTemplateData("name",receiverName);
//            personalization.addDynamicTemplateData("message",message);
//            personalization.addDynamicTemplateData("message_two",message_two);
            mail.addPersonalization(personalization);
            mail.setFrom(new Email(from, "Viola"));
            mail.setReplyTo(new Email(replyTo, "Support team"));
            mail.setSubject(subject);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            throw new MyException("Unable to send email");
        }
    }


    public void sendOtpEmail(String to, String otp, String subject) throws IOException, MyException {
        try {
            Personalization personalization = new Personalization();
            Mail mail = new Mail();
            mail.setTemplateId(otpEmailTemplate);
            personalization.addTo(new Email(to,"Viola"));
            personalization.addDynamicTemplateData("otp",otp);
//            personalization.addDynamicTemplateData("message",message);
//            personalization.addDynamicTemplateData("message_two",message_two);
            mail.addPersonalization(personalization);
            mail.setFrom(new Email(from, "Viola"));
            mail.setReplyTo(new Email(replyTo, "Support team"));
            mail.setSubject(subject);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
        } catch (IOException ex) {
           logger.error(ex.getMessage());
           throw new MyException("Unable to send email");
        }
    }


    public void sendIncomingTxEmail(String to, String subject, String amount,String asset,String toAddress) throws IOException, MyException {
        try {
            Personalization personalization = new Personalization();
            Mail mail = new Mail();
            mail.setTemplateId(incomingPaymentTemplate);
            personalization.addTo(new Email(to,"Viola"));
            personalization.addDynamicTemplateData("amount", amount);
            personalization.addDynamicTemplateData("symbol", asset);
            personalization.addDynamicTemplateData("address", toAddress);
            mail.addPersonalization(personalization);
            mail.setFrom(new Email(from, "Viola"));
            mail.setReplyTo(new Email(replyTo, "Support team"));
            mail.setSubject(subject);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
        } catch (IOException ex) {
           logger.error(ex.getMessage());
           throw new MyException("Unable to send email");
        }
    }

    public void sendOutgoingTxEmail(String to, String subject, String amount,String address,String asset) throws IOException, MyException {
        try {
            Personalization personalization = new Personalization();
            Mail mail = new Mail();
            mail.setTemplateId(outgoingPaymentTemplate);
            personalization.addTo(new Email(to,"Viola"));
            personalization.addDynamicTemplateData("amount", amount);
            personalization.addDynamicTemplateData("symbol", asset);
            personalization.addDynamicTemplateData("address", address);
            mail.addPersonalization(personalization);
            mail.setFrom(new Email(from, "Viola"));
            mail.setReplyTo(new Email(replyTo, "Support team"));
            mail.setSubject(subject);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
        } catch (IOException ex) {
           logger.error(ex.getMessage());
           throw new MyException("Unable to send email");
        }
    }

    
    public void sendWithdrawalConfirmedEmail(String to, String subject, String amount,String fromAddress,String asset) throws IOException, MyException {
        try {
            Personalization personalization = new Personalization();
            Mail mail = new Mail();
            mail.setTemplateId(withdrawalConfirmedTemplate);
            personalization.addTo(new Email(to,"Viola"));
            personalization.addDynamicTemplateData("amount", amount);
            personalization.addDynamicTemplateData("symbol", asset);
            personalization.addDynamicTemplateData("address", fromAddress);
            mail.addPersonalization(personalization);
            mail.setFrom(new Email(from, "Viola"));
            mail.setReplyTo(new Email(replyTo, "Support team"));
            mail.setSubject(subject);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
        } catch (IOException ex) {
           logger.error(ex.getMessage());
           throw new MyException("Unable to send email");
        }
    }

    public void sendDepositConfirmedEmail(String to, String subject, String amount,String asset,String fromAddress) throws IOException, MyException {
        try {
            Personalization personalization = new Personalization();
            Mail mail = new Mail();
            mail.setTemplateId(depositConfirmedTemplate);
            personalization.addTo(new Email(to,"Viola"));
            personalization.addDynamicTemplateData("amount", amount);
            personalization.addDynamicTemplateData("symbol", asset);
            personalization.addDynamicTemplateData("address", fromAddress);
            mail.addPersonalization(personalization);
            mail.setFrom(new Email(from, "Viola"));
            mail.setReplyTo(new Email(replyTo, "Support team"));
            mail.setSubject(subject);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
        } catch (IOException ex) {
           logger.error(ex.getMessage());
           throw new MyException("Unable to send email");
        }
    }

    public void sendKycEmailSubmittedEmail(String to,String receiverName,String subject) throws IOException, MyException {
        try {
            Personalization personalization = new Personalization();
            Mail mail = new Mail();
            mail.setTemplateId(kycSubmittedTemplate);
            personalization.addTo(new Email(to,receiverName));
            personalization.addDynamicTemplateData("name",receiverName);
//            personalization.addDynamicTemplateData("message",message);
//            personalization.addDynamicTemplateData("message_two",message_two);
            mail.addPersonalization(personalization);
            mail.setFrom(new Email(from, "Viola"));
            mail.setReplyTo(new Email(replyTo, "Support team"));
            mail.setSubject(subject);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            throw new MyException("Unable to send email");
        }
    }
}
