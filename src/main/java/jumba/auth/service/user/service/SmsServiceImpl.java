package jumba.auth.service.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {
    @Value("${sms.auth.key}")
    private String authKey;

    @Value("${sms.route}")
    private String route;

    @Value("${sms.api.url}")
    private String apiUrl;

    @Override
    public void sendMessage(String message, String senderId, String[] phoneNumbers) {
        URLConnection myURLConnection = null;
        URL myURL = null;
        BufferedReader reader = null;
        //encoding message
        String encoded_message = URLEncoder.encode(message, Charset.defaultCharset());
        StringBuilder sbPostData = new StringBuilder(apiUrl)
                .append("authkey=").append(this.authKey).
                append("&mobiles=").append(Arrays.toString(phoneNumbers)).
                append("&message=").append(encoded_message)
                .append("&route=").append(this.route)
                .append("&sender=").append(senderId);
        apiUrl = sbPostData.toString();
        try {
            myURL = new URL(apiUrl);
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
            String response;
            while ((response = reader.readLine()) != null)
                log.info(response);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
