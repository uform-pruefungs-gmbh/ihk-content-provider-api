# Quick Start Guide - Java Client

## ⚡ 5-Minuten Setup

### 1. Voraussetzungen prüfen

```powershell
# Java Version prüfen (21 erforderlich)
java -version

# Maven prüfen
mvn -version
```

### 2. Projekt bauen

```powershell
# In das Projektverzeichnis wechseln
cd api-clients/java

# Option A: Mit Maven direkt
mvn clean install

# Option B: Mit PowerShell Build-Script
.\build.ps1

# Option C: Nur Code-Generierung
mvn clean generate-sources
# oder
.\build.ps1 -GenerateOnly
```

### 3. Generierte Interfaces prüfen

```powershell
# Generierte Dateien anzeigen
ls target/generated-sources/openapi/src/main/java/io/pruefung/api/spi/client/
```

**Erwartete Interfaces:**

- `OneTimePasswordApi.java` - OTP-Verwaltung
- `HandleStateApi.java` - Status & Locks
- `ServerSentEventsApi.java` - SSE Monitoring
- `ExecutionEntriesApi.java` - Ergebnis-Abruf

### 4. In eigenem Projekt verwenden

#### Maven Dependency

```xml
<dependency>
    <groupId>io.pruefung.api.spi</groupId>
    <artifactId>ihk-content-provider-java-client</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

#### Spring Boot Controller erstellen

```java
import io.pruefung.api.spi.client.OneTimePasswordApi;
import io.pruefung.api.spi.client.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyOtpController implements OneTimePasswordApi {

    @Override
    public ResponseEntity<OneTimePassword> createOtp(List<OtpRequestItem> items) {
        // Ihre Implementierung hier
        OneTimePassword otp = new OneTimePassword();
        otp.setOtpId("my-otp-123");
        otp.setOtpType(OneTimePassword.OtpTypeEnum.TAN);
        // ... weitere Eigenschaften setzen

        return ResponseEntity.status(201).body(otp);
    }
}
```

## 🔧 Häufige Aufgaben

### Interfaces nach OpenAPI-Änderung neu generieren

```powershell
# 1. OpenAPI-Datei editieren
code ../../api-definitions/src/main/resources/openapi/ihk-content-provider-spi.yaml

# 2. Neu generieren
mvn clean generate-sources

# 3. Projekt neu bauen
mvn clean install
```

### Nur Models (ohne APIs) generieren

Editieren Sie `pom.xml`:

```xml
<configOptions>
    <interfaceOnly>true</interfaceOnly>
    <generateApis>false</generateApis>  <!-- APIs nicht generieren -->
    <generateModels>true</generateModels> <!-- Nur Models -->
</configOptions>
```

### Maven Cache leeren

```powershell
mvn clean
Remove-Item -Recurse -Force ~/.m2/repository/org/openapitools
mvn install
```

## 📦 Build-Ausgaben

Nach erfolgreichem Build finden Sie:

```
target/
├── ihk-content-provider-java-client-1.0.0-SNAPSHOT.jar      # Haupt-JAR
├── ihk-content-provider-java-client-1.0.0-SNAPSHOT-sources.jar  # Quellen
├── ihk-content-provider-java-client-1.0.0-SNAPSHOT-javadoc.jar  # Javadoc
└── generated-sources/
    └── openapi/
        └── src/main/java/
            └── io/pruefung/api/spi/client/  # Generierte Interfaces
```

## 🎯 Nächste Schritte

1. **Beispiele ansehen**: Siehe `src/main/java/io/pruefung/api/spi/example/`
2. **Dokumentation lesen**: `README.md` im Projektverzeichnis
3. **API testen**: Implementieren Sie die Interfaces in Ihrer Spring Boot App
4. **CI/CD einrichten**: Integrieren Sie den Build in Ihre Pipeline

## 🐛 Troubleshooting

| Problem                      | Lösung                                                    |
| ---------------------------- | --------------------------------------------------------- |
| Maven nicht gefunden         | Maven installieren: https://maven.apache.org/download.cgi |
| Java-Version falsch          | Java 21 installieren: https://adoptium.net/               |
| OpenAPI-Datei nicht gefunden | Pfad in `pom.xml` prüfen                                  |
| Generierung schlägt fehl     | `mvn clean` ausführen und erneut versuchen                |

## 📞 Support

- **Dokumentation**: Siehe `README.md`
- **OpenAPI-Spezifikation**: `../../api-definitions/src/main/resources/openapi/ihk-content-provider-spi.yaml`
- **Beispiele**: `../../api-definitions/src/main/resources/openapi/examples/`
