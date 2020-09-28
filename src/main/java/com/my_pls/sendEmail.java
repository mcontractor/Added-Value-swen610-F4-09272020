package com.my_pls;
import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;
// String API_Key = "431814ad6dcc29e374b5e02e345c2ac1-aff2d1b9-2d0f8917"; hackln not used

// Resources from https://github.com/sargue/mailgun

public class sendEmail{
    public static void sendEmail_content(String senderEmail,String subject, String content) {
        Configuration configuration = new Configuration()
                .domain("bestspeaker.lk")
                .apiKey("key-f64cc3ddd3dca59f8eccbc3b5b85fa02")
                .from("Added-Value myPLS", "mailgun@bestspeaker.lk");
        Mail.using(configuration)
                .to(senderEmail)
                .subject(subject)
                .text(content)
                .build()
                .send();
    }

}
