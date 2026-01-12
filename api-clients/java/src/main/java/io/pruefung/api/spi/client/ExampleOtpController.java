package io.pruefung.api.spi.client;

import io.pruefung.api.spi.client.OneTimePasswordApi;
import io.pruefung.api.spi.client.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Beispiel-Implementierung der OneTimePasswordApi Interface.
 * 
 * Diese Klasse zeigt, wie die generierten Interfaces implementiert werden
 * können.
 * In einer produktiven Anwendung würde hier die tatsächliche Business-Logik
 * implementiert werden.
 */
@RestController
public class ExampleOtpController implements OneTimePasswordApi {

    /**
     * Erstellt One-Time-Passwords für Prüfungszugriffe.
     * 
     * @param otpRequestItems Liste der OTP-Anforderungen
     * @return ResponseEntity mit dem erstellten OneTimePassword
     */
    @Override
    public ResponseEntity<OneTimePassword> createOtp(List<OtpRequestItem> otpRequestItems) {
        // Beispiel-Implementierung
        if (otpRequestItems == null || otpRequestItems.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Nehme das erste Item als Beispiel
        OtpRequestItem firstItem = otpRequestItems.get(0);

        // Erstelle OTP
        OneTimePassword otp = new OneTimePassword();
        otp.setOtpId(generateOtpId());
        otp.setOtpType(OneTimePassword.OtpTypeEnum.TAN);
        otp.setContentId(firstItem.getContentId());
        otp.setSubjectId(firstItem.getSubjectId());
        otp.setDuration("PT120M"); // 120 Minuten

        // Zusätzliche Zeit aus Request übernehmen
        if (firstItem.getAdditionaltime() != null) {
            otp.setAdditionaltime(firstItem.getAdditionaltime());
        } else {
            otp.setAdditionaltime("PT0S"); // Default: keine zusätzliche Zeit
        }

        otp.setOtpstatus(OtpStatusType.CREATED);
        otp.setExpiration(OffsetDateTime.now().plusHours(24)); // 24 Stunden gültig

        // Optional: Participation ID setzen
        if (firstItem.getParticipation() != null) {
            otp.setParticipationId(firstItem.getParticipation().getParticipationId());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(otp);
    }

    /**
     * Generiert eine eindeutige OTP-ID.
     * 
     * @return Generierte OTP-ID als String
     */
    private String generateOtpId() {
        // Beispiel-Implementierung: UUID oder eigene Logik
        return UUID.randomUUID().toString().substring(0, 9);
    }
}
