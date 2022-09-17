package jumba.auth.service.user.service;

public interface SmsService {
    public void sendMessage(final String message,final String senderId, final  String[] phoneNumbers);
}
