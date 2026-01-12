/**
 * Beispiel-Implementierungen der IHK Content Provider SPI API.
 * 
 * <p>
 * Dieses Package enthält Beispiel-Implementierungen der generierten
 * API-Interfaces.
 * Diese Beispiele zeigen, wie die Interfaces in einer produktiven Anwendung
 * implementiert werden können.
 * </p>
 * 
 * <h2>Verfügbare Beispiele:</h2>
 * <ul>
 * <li>{@link io.pruefung.api.spi.example.ExampleOtpController} -
 * One-Time-Password Verwaltung</li>
 * <li>{@link io.pruefung.api.spi.example.ExampleHandleStateController} -
 * Status- und Lock-Management</li>
 * </ul>
 * 
 * <h2>Verwendung:</h2>
 * 
 * <pre>
 * {
 *     &#64;code
 *     &#64;RestController
 *     public class MyController implements OneTimePasswordApi {
 * 
 *         @Override
 *         public ResponseEntity<OneTimePassword> createOtp(List<OtpRequestItem> items) {
 *             // Ihre Implementierung
 *             return ResponseEntity.ok(otp);
 *         }
 *     }
 * }
 * </pre>
 * 
 * @since 1.0.0
 * @version 1.0.0-SNAPSHOT
 */
package io.pruefung.api.spi.client;
