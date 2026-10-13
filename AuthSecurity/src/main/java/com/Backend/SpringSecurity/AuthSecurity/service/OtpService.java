package com.Backend.SpringSecurity.AuthSecurity.service;
import com.Backend.SpringSecurity.AuthSecurity.model.EmailOtp;
import com.Backend.SpringSecurity.AuthSecurity.repository.EmailOtpRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class OtpService {

 private final SecureRandom random = new SecureRandom();
 private final EmailOtpRepository emailOtpRepository;
 private final EmailService emailService;

   public OtpService(EmailOtpRepository emailOtpRepository, EmailService emailService) {
       this.emailOtpRepository = emailOtpRepository;
       this.emailService = emailService;
   }
     public String generateOtp() {
       int number = random.nextInt(1_000_000);
        return String.format("%06d", number);
     }

     public void sendOtp(String email) {
       String otp = generateOtp();
      
       LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(3600);
       EmailOtp emailOtp = emailOtpRepository.findByEmail(email).orElse(new EmailOtp());
       emailOtp.setOtp(otp);
       emailOtp.setEmail(email);
       emailOtp.setExpiryTime(expiryDate);
       emailOtpRepository.save(emailOtp);
       emailService.sendOtp(email ,otp);
   }

    public void verifyOtp(String email, String otp) {
       EmailOtp savedOtp = emailOtpRepository.findByEmail(email).orElseThrow(()->
               new RuntimeException("OTP Not Found"));

       if(LocalDateTime.now().isAfter(savedOtp.getExpiryTime())){
           throw new RuntimeException("OTP Expired");
       }
       if(otp.equals(savedOtp.getOtp())){
           throw new RuntimeException("OTP Verified");
       }
        emailOtpRepository.delete(savedOtp);
    }
}

