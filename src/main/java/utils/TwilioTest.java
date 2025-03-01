package utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioTest {
    public static final String ACCOUNT_SID = "AC5a28208bac0dac7d3083921236541445";
    public static final String AUTH_TOKEN = "33d5ec47a6ea7f711d40dd73b52e3083";
    public static final String TWILIO_PHONE_NUMBER = "+21699153514";

    public static void main(String[] args) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        String toPhoneNumber = "+216 99153514"; // Replace with a verified phone number
        String messageBody = "Hello from Twilio!";

        try {
            Message message = Message.creator(
                    new PhoneNumber(toPhoneNumber), // To
                    new PhoneNumber(TWILIO_PHONE_NUMBER), // From
                    messageBody // Message content
            ).create();

            System.out.println("SMS sent successfully! SID: " + message.getSid());
        } catch (Exception e) {
            System.err.println("Failed to send SMS: " + e.getMessage());
            e.printStackTrace();
        }
    }
}