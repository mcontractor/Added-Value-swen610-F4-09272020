package com.my_pls;
import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;


// Resources from https://github.com/sargue/mailgun

public class sendEmail{
    public void sendEmail_content(String senderEmail,String subject, String content) {
        Configuration configuration = new Configuration()
                .domain("foo.com")
                .apiKey("key-xxxxxx")
                .from("Added-Value myPLS", "foo@hello.com");
        Mail.using(configuration)
                .to(senderEmail)
                .subject(subject)
                .text(content)
                .build()
                .send();
    }

}
