package jumba.auth.service.user.service;

import jumba.auth.service.user.dto.OtpAuthRequest;

/**
 * @author jumba
 */
public interface OtpAuthService {
    /**
     * @param phoneNumber
     * @param appName
     */
    void verifyUserPhoneNumber(String phoneNumber, String appName);

    /**
     * @param otpAuthRequest
     */
    void authenticateUser(OtpAuthRequest otpAuthRequest);
}
