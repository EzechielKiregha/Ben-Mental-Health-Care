package drg.mentalhealth.support.service;

import org.springframework.stereotype.Service;
// import com.twilio.Twilio;
// import com.twilio.rest.api.v2010.account.Message;
// import com.twilio.type.PhoneNumber;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OTPService {

    public static final String ACCOUNT_SID = "ACbf70e09e11b2179b7808e8289e5c663e";
    public static final String AUTH_TOKEN = "2cccf44898f91657ff3590720f10bd7f";
    // VA8830b100b910906dbad8bacf59ae25b0

    private final Map<String, Integer> otpData = new HashMap<>();

    public Integer generateOTP(String key) {
        Random random = new Random();
        Integer otp = 100000 + random.nextInt(900000); // 6-digit OTP
        otpData.put(key, otp);
        return otp;
    }

    
    public Integer sendOtpViaSMS(String toPhoneNumber) {
        Integer code = generateOTP(toPhoneNumber);
        // Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        // Message message = Message.creator(
        //         new PhoneNumber(toPhoneNumber), // To number
        //         new PhoneNumber("+16626748068"),   // From Twilio number
        //         "Verification Code is : " + code)
        //     .create();

        return code;
    }

    public boolean validateOTP(String key, Integer otp) {
        Integer storedOtp = otpData.get(key);
        if (storedOtp != null && storedOtp.equals(otp)) {
            otpData.remove(key); // Remove after use
            return true;
        }
        return false;
        
    }

    public void clearOTP(String key) {
        otpData.remove(key);
    }


}
