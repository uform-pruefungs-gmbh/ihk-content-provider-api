package io.pruefung.api.spi.client;

import io.pruefung.api.spi.client.HandleStateApi;
import io.pruefung.api.spi.client.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Beispiel-Implementierung der HandleStateApi Interface.
 * 
 * Diese Klasse zeigt, wie Status-Management und Lock-Funktionen
 * implementiert werden können.
 */
@RestController
public class ExampleHandleStateController implements HandleStateApi {

    /**
     * Ruft den aktuellen Status von Prüfungen ab.
     * 
     * @param otpIds Liste der OTP-IDs
     * @return Liste der Status-Objekte
     */
    @Override
    public ResponseEntity<List<OtpStatus>> getExaminationStatus(List<String> otpIds) {
        List<OtpStatus> statusList = new ArrayList<>();

        for (String otpId : otpIds) {
            OtpStatus status = new OtpStatus();
            status.setOtpId(otpId);
            status.setStatus(OtpStatusType.RUNNING);
            status.setLastUpdated(OffsetDateTime.now());

            statusList.add(status);
        }

        return ResponseEntity.ok(statusList);
    }

    /**
     * Sperrt oder entsperrt Prüfungen nach den definierten Zustandsübergängen.
     * 
     * Zustandsübergänge:
     * - created + lock → created
     * - created + unlock → runnable
     * - runnable + lock → created
     * - runnable + unlock → running
     * - running + lock → suspended
     * - running + unlock → running
     * - suspended + lock → suspended
     * - suspended + unlock → running
     * 
     * @param lockState "lock" oder "unlock"
     * @param otpIds    Liste der OTP-IDs
     * @return Status nach der Operation
     */
    @Override
    public ResponseEntity<OtpStatus> handleLocksExaminations(String lockState, List<String> otpIds) {
        // Validierung
        if (!"lock".equals(lockState) && !"unlock".equals(lockState)) {
            return ResponseEntity.badRequest().build();
        }

        if (otpIds == null || otpIds.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Beispiel: Bearbeite erste OTP-ID
        String otpId = otpIds.get(0);

        // Hier würde der aktuelle Status aus der Datenbank abgerufen werden
        // Für dieses Beispiel verwenden wir einen Standard-Status
        OtpStatusType currentState = OtpStatusType.RUNNING;

        // Bestimme den neuen Status basierend auf aktuellem Status und Lock-Operation
        OtpStatusType newState = getNewState(currentState, lockState);

        OtpStatus status = new OtpStatus();
        status.setOtpId(otpId);
        status.setStatus(newState);
        status.setLastUpdated(OffsetDateTime.now());

        return ResponseEntity.ok(status);
    }

    /**
     * Bestimmt den neuen Zustand basierend auf aktuellem Zustand und
     * Lock-Operation.
     * 
     * @param currentState Der aktuelle Status der Prüfung
     * @param lockState    "lock" oder "unlock"
     * @return Der neue Status nach der Operation
     */
    private OtpStatusType getNewState(OtpStatusType currentState, String lockState) {
        if ("lock".equals(lockState)) {
            // Lock-Operationen
            return switch (currentState) {
                case CREATED, RUNNABLE -> OtpStatusType.CREATED;
                case RUNNING, SUSPENDED -> OtpStatusType.SUSPENDED;
                default -> currentState;
            };
        } else {
            // Unlock-Operationen
            return switch (currentState) {
                case CREATED -> OtpStatusType.RUNNABLE;
                case RUNNABLE, RUNNING, SUSPENDED -> OtpStatusType.RUNNING;
                default -> currentState;
            };
        }
    }

    /**
     * Fügt zusätzliche Zeit zu Prüfungen hinzu.
     * 
     * @param addTimeRequests Liste der Zeitänderungsanfragen
     * @return Status nach der Operation
     */
    @Override
    public ResponseEntity<OtpStatus> addTime(List<AddTimeRequest> addTimeRequests) {
        if (addTimeRequests == null || addTimeRequests.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        AddTimeRequest firstRequest = addTimeRequests.get(0);

        // Beispiel-Implementierung
        OtpStatus status = new OtpStatus();
        status.setOtpId(firstRequest.getOtpId());
        status.setStatus(OtpStatusType.RUNNING);
        status.setLastUpdated(OffsetDateTime.now());

        // Hier würde die tatsächliche Zeitverlängerung implementiert werden

        return ResponseEntity.ok(status);
    }
}
