# IHK Content Provider SPI - Java Client

Java Client-Interfaces für die IHK Content Provider SPI API.

## 📋 Übersicht

Dieses Modul generiert Java-Interfaces basierend auf der OpenAPI-Spezifikation der IHK Content Provider SPI API. Die generierten Interfaces können direkt in Spring Boot-Projekten verwendet werden, um die SPI-Schnittstelle zu implementieren.

## 🏗️ Generierte Komponenten

### Interfaces

Das Projekt generiert folgende API-Interfaces:

- **OneTimePasswordApi** - OTP-Verwaltung
- **HandleStateApi** - Status- und Lock-Verwaltung
- **ServerSentEventsApi** - SSE Monitoring
- **ExecutionEntriesApi** - Prüfungsergebnis-Abruf

### Models

Alle Datenmodelle werden als POJOs generiert:

- `OneTimePassword`
- `Participation`
- `Participant`
- `OtpStatus`
- `OtpRequestItem`
- `AddTimeRequest`
- `MonitoringStream`, `MonitoringEvent`, `HeartbeatEvent`, `ErrorEvent`
- `ExecutionEntry`
- `Error`

## 🚀 Build & Installation

### Voraussetzungen

- Java 21
- Maven 3.9+
- OpenAPI-Spezifikation unter `../../api-definitions/src/main/resources/openapi/ihk-content-provider-spi.yaml`

### Build-Prozess

```bash
# Interfaces und Models generieren
mvn clean generate-sources

# Vollständiger Build
mvn clean install

# Build ohne Tests
mvn clean install -DskipTests
```

### Generated Sources

Die generierten Java-Klassen befinden sich unter:

```
target/generated-sources/openapi/src/main/java/
├── io.pruefung.api.spi.client/          # API Interfaces
│   ├── OneTimePasswordApi.java
│   ├── HandleStateApi.java
│   ├── ServerSentEventsApi.java
│   └── ExecutionEntriesApi.java
└── io.pruefung.api.spi.client.model/    # Data Models
    ├── OneTimePassword.java
    ├── Participant.java
    ├── OtpStatus.java
    └── ...
```

## 📦 Verwendung in eigenen Projekten

### Maven Dependency

```xml
<dependency>
    <groupId>io.pruefung.api.spi</groupId>
    <artifactId>ihk-content-provider-java-client</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Implementierung der Interfaces

```java
import io.pruefung.api.spi.client.OneTimePasswordApi;
import io.pruefung.api.spi.client.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OtpController implements OneTimePasswordApi {

    @Override
    public ResponseEntity<OneTimePassword> createOtp(List<OtpRequestItem> otpRequestItems) {
        // Implementierung
        OneTimePassword otp = new OneTimePassword();
        otp.setOtpId("generated-otp-id");
        otp.setOtpType(OtpTypeEnum.TAN);
        otp.setSubjectId("subject-123");
        otp.setDuration("PT120M");
        otp.setOtpstatus(OtpStatusType.CREATED);

        return ResponseEntity.status(201).body(otp);
    }
}
```

### Spring Boot Integration

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "io.pruefung.api.spi.client",
    "com.yourcompany.implementation"
})
public class SpiProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpiProviderApplication.class, args);
    }
}
```

## 🔧 Konfiguration

### OpenAPI Generator Optionen

Das POM ist konfiguriert mit:

- **interfaceOnly**: `true` - Nur Interfaces, keine Implementierungen
- **skipDefaultInterface**: `true` - Keine Default-Implementierungen
- **useSpringBoot3**: `true` - Spring Boot 3 Kompatibilität
- **useBeanValidation**: `true` - Jakarta Bean Validation
- **useJakartaEe**: `true` - Jakarta EE statt javax
- **dateLibrary**: `java8` - Verwendet `java.time.*` Klassen

### Package-Struktur

- **apiPackage**: `io.pruefung.api.spi.client`
- **modelPackage**: `io.pruefung.api.spi.client.model`
- **invokerPackage**: `io.pruefung.api.spi.client.invoker`

## 📝 API-Endpunkte

### OneTimePassword API

```http
POST /examinations/otp
```

Erstellt One-Time-Passwords für Prüfungszugriffe.

### HandleState API

```http
POST /examinations/status
PUT  /examinations/lockstate/{lock_state}
PUT  /examinations/addtime
```

Verwaltung von Prüfungsstatus, Locks und zusätzlicher Zeit.

### ServerSentEvents API

```http
POST /monitoring/sse
```

Server-Sent Events für Echtzeit-Monitoring.

### ExecutionEntries API

```http
POST /examinations/entries
```

Abruf von Prüfungsergebnissen und Teilnehmeraktionen.

## 🔐 Authentifizierung

Die API verwendet Bearer Token Authentifizierung:

```http
Authorization: Bearer <JWT_TOKEN>
```

## 🧪 Testing

```bash
# Alle Tests ausführen
mvn test

# Nur Compilation testen
mvn clean compile
```

## 📚 Weitere Dokumentation

- [OpenAPI Spezifikation](../../api-definitions/src/main/resources/openapi/ihk-content-provider-spi.yaml)
- [API Dokumentation](../../docs/)
- [Beispiele](../../api-definitions/src/main/resources/openapi/examples/)

## 🔄 Entwicklungs-Workflow

### 1. OpenAPI-Spezifikation ändern

```bash
# Editiere die Spezifikation
vim ../../api-definitions/src/main/resources/openapi/ihk-content-provider-spi.yaml
```

### 2. Interfaces neu generieren

```bash
mvn clean generate-sources
```

### 3. Build und Test

```bash
mvn clean install
```

### 4. In eigenem Projekt verwenden

```bash
mvn clean install
# Dann in Ihrem Projekt als Dependency einbinden
```

## 🐛 Troubleshooting

### Problem: OpenAPI-Datei nicht gefunden

**Lösung**: Überprüfen Sie den relativen Pfad zur OpenAPI-Spezifikation:

```bash
ls -la ../../api-definitions/src/main/resources/openapi/ihk-content-provider-spi.yaml
```

### Problem: Generierte Klassen nicht im Classpath

**Lösung**: Maven-Cache löschen und neu bauen:

```bash
mvn clean
rm -rf target/
mvn generate-sources
```

### Problem: Jakarta vs Javax Annotations

**Lösung**: Stellen Sie sicher, dass `useJakartaEe=true` in der POM-Konfiguration gesetzt ist.

## 📦 Build-Artefakte

Nach erfolgreichem Build:

- `target/ihk-content-provider-java-client-1.0.0-SNAPSHOT.jar` - Haupt-JAR mit Interfaces und Models
- `target/ihk-content-provider-java-client-1.0.0-SNAPSHOT-sources.jar` - Source-JAR
- `target/ihk-content-provider-java-client-1.0.0-SNAPSHOT-javadoc.jar` - Javadoc-JAR

## 🚀 Deployment

### Maven Local Repository

```bash
mvn clean install
```

### Maven Remote Repository

```bash
mvn clean deploy -DaltDeploymentRepository=your-repo::default::https://your-maven-repo.com/repository/snapshots
```

## 🔑 Wichtige Hinweise

1. **Nie generierte Klassen manuell ändern** - Alle Änderungen in `target/generated-sources/` werden beim nächsten Build überschrieben
2. **OpenAPI-Spezifikation ist die Single Source of Truth** - Alle Änderungen müssen in der YAML-Datei erfolgen
3. **Interface-Only** - Dieses Projekt generiert nur Interfaces, keine Implementierungen
4. **Spring Boot 3 + Jakarta EE** - Kompatibel mit modernen Spring Boot Anwendungen

## 📞 Support

Bei Fragen oder Problemen:

- **Issues**: GitHub Issues im Haupt-Repository
- **Dokumentation**: `../../docs/`

---

**Version**: 1.0.0-SNAPSHOT  
**Java Version**: 21  
**Spring Boot**: 3.4.1  
**OpenAPI Generator**: 7.10.0
