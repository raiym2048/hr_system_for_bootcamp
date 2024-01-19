package com.example.hr_system;

import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import com.twilio.twiml.messaging.Redirect;
import com.twilio.type.PhoneNumber;

import java.net.URI;
import java.math.BigDecimal;

public class WhatsAppSender {
    // Find your Account Sid and Token at twilio.com/console
    public static final String ACCOUNT_SID = "AC0c5cfcfaf849af610ef00c213e03fafc";
    public static final String AUTH_TOKEN = "10451afed2262da163987226c0ae9e5a";
    public static void main(String[] args) {
        Message message = new Message.Builder("whatsapp:+996700624890")
                .build();
        Message message2 = new Message.Builder("whatsapp:+14155238886   ")
                .build();
        MessagingResponse response = new MessagingResponse.Builder()
                .message(message).message(message2).build();

        try {
            System.out.println(response.toXml());
        } catch (TwiMLException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        Body body = new Body.Builder("Hello World!").build();
//        Message message = new Message.Builder().body(body).build();
//        Redirect redirect = new Redirect
//                .Builder("https://demo.twilio.com/welcome/sms/").build();
//        MessagingResponse response = new MessagingResponse.Builder()
//                .message(message).redirect(redirect).build();
//
//        try {
//            System.out.println(response.toXml());
//        } catch (TwiMLException e) {
//            e.printStackTrace();
//        }
//    }

//    public static void main(String[] args) {
//        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//        Message message = Message.creator(
//                new com.twilio.type.PhoneNumber("whatsapp:+996700624890"),
//                new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
//                "Your appointment is coming up on July 21 at 3PM"
//
//        ).create();
//
//        System.out.println(message.getSid());
//    }
}