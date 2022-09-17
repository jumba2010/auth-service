package jumba.auth.service.user.dto;

import lombok.Data;

@Data
public class OtpAuthRequest {
    private String phoneNumber;
    private String authCode;
}