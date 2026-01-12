# Java Client Projekt - Übersicht

## 📁 Projektstruktur

```
api-clients/java/
│
├── pom.xml                          # Maven Projektkonfiguration
├── README.md                        # Vollständige Dokumentation
├── QUICKSTART.md                    # 5-Minuten Setup-Guide
├── .gitignore                       # Git Ignore-Regeln
├── build.ps1                        # PowerShell Build-Script
│
├── src/
│   └── main/
│       └── java/
│           └── io/pruefung/api/spi/example/
│               ├── package-info.java                    # Package-Dokumentation
│               ├── ExampleOtpController.java            # Beispiel: OTP API
│               └── ExampleHandleStateController.java    # Beispiel: State API
│
└── target/                          # Build-Ausgabe (nach mvn install)
    ├── generated-sources/
    │   └── openapi/
    │       └── src/main/java/
    │           ├── io/pruefung/api/spi/client/          # Generierte API-Interfaces
    │           │   ├── OneTimePasswordApi.java
    │           │   ├── HandleStateApi.java
    │           │   ├── ServerSentEventsApi.java
    │           │   └── ExecutionEntriesApi.java
    │           │
    │           ├── io/pruefung/api/spi/client/model/    # Generierte Data Models
    │           │   ├── OneTimePassword.java
    │           │   ├── Participant.java
    │           │   ├── Participation.java
    │           │   ├── OtpStatus.java
    │           │   ├── OtpStatusType.java
    │           │   ├── OtpRequestItem.java
    │           │   ├── AddTimeRequest.java
    │           │   ├── MonitoringStream.java
    │           │   ├── MonitoringEvent.java
    │           │   ├── HeartbeatEvent.java
    │           │   ├── ErrorEvent.java
    │           │   ├── ExecutionEntry.java
    │           │   └── Error.java
    │           │
    │           └── io/pruefung/api/spi/client/invoker/  # Invoker & Config
    │
    ├── classes/                     # Kompilierte Klassen
    ├── ihk-content-provider-java-client-1.0.0-SNAPSHOT.jar
    ├── ihk-content-provider-java-client-1.0.0-SNAPSHOT-sources.jar
    └── ihk-content-provider-java-client-1.0.0-SNAPSHOT-javadoc.jar
```

## 🎯 Hauptkomponenten

### 1. API-Interfaces (generiert)

Alle API-Endpunkte als Java-Interfaces:

| Interface             | Beschreibung             | Endpunkte                                                                                                |
| --------------------- | ------------------------ | -------------------------------------------------------------------------------------------------------- |
| `OneTimePasswordApi`  | OTP-Verwaltung           | `POST /examinations/otp`                                                                                 |
| `HandleStateApi`      | Status & Lock-Management | `POST /examinations/status`<br>`PUT /examinations/lockstate/{lock_state}`<br>`PUT /examinations/addtime` |
| `ServerSentEventsApi` | SSE Monitoring           | `POST /monitoring/sse`                                                                                   |
| `ExecutionEntriesApi` | Ergebnis-Abruf           | `POST /examinations/entries`                                                                             |

### 2. Data Models (generiert)

Alle Datenstrukturen als POJOs:

- **Request Models**: `OtpRequestItem`, `AddTimeRequest`
- **Response Models**: `OneTimePassword`, `OtpStatus`, `ExecutionEntry`
- **Domain Models**: `Participant`, `Participation`
- **Enums**: `OtpStatusType`
- **Streaming**: `MonitoringStream`, `MonitoringEvent`, `HeartbeatEvent`, `ErrorEvent`

### 3. Beispiel-Implementierungen

Referenz-Implementierungen in `src/main/java/io/pruefung/api/spi/example/`:

- `ExampleOtpController` - Zeigt OTP-Erstellung
- `ExampleHandleStateController` - Zeigt Status-Management

## 🚀 Verwendung

### Als Maven Dependency

```xml
<dependency>
    <groupId>io.pruefung.api.spi</groupId>
    <artifactId>ihk-content-provider-java-client</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Interface implementieren

```java
@RestController
public class MyController implements OneTimePasswordApi {
    @Override
    public ResponseEntity<OneTimePassword> createOtp(List<OtpRequestItem> items) {
        // Implementierung
    }
}
```

## 🔧 Build-Befehle

```bash
# Vollständiger Build
mvn clean install

# Nur Code-Generierung
mvn generate-sources

# Mit PowerShell Script
.\build.ps1

# Nur Generierung
.\build.ps1 -GenerateOnly
```

## 📋 Konfiguration

### OpenAPI Generator Settings (in pom.xml)

```xml
<configOptions>
    <interfaceOnly>true</interfaceOnly>           <!-- Nur Interfaces -->
    <skipDefaultInterface>true</skipDefaultInterface>
    <useSpringBoot3>true</useSpringBoot3>         <!-- Spring Boot 3 -->
    <useBeanValidation>true</useBeanValidation>   <!-- Jakarta Validation -->
    <useJakartaEe>true</useJakartaEe>             <!-- Jakarta EE -->
    <dateLibrary>java8</dateLibrary>              <!-- java.time.* -->
</configOptions>
```

### Package-Struktur

- **API Package**: `io.pruefung.api.spi.client`
- **Model Package**: `io.pruefung.api.spi.client.model`
- **Invoker Package**: `io.pruefung.api.spi.client.invoker`

## 📚 Dokumentation

| Dokument                                                                         | Beschreibung                       |
| -------------------------------------------------------------------------------- | ---------------------------------- |
| `README.md`                                                                      | Vollständige Projekt-Dokumentation |
| `QUICKSTART.md`                                                                  | 5-Minuten Setup-Guide              |
| `../../api-definitions/src/main/resources/openapi/ihk-content-provider-spi.yaml` | OpenAPI-Spezifikation              |
| `../../docs/`                                                                    | API-Dokumentation                  |

## 🔄 Entwicklungs-Workflow

1. **OpenAPI ändern**: Spezifikation in YAML editieren
2. **Neu generieren**: `mvn generate-sources`
3. **Prüfen**: Generierte Interfaces in `target/generated-sources/`
4. **Build**: `mvn clean install`
5. **Verwenden**: Als Dependency in eigenem Projekt einbinden

## ⚠️ Wichtige Hinweise

1. **Nie generierte Klassen editieren** - Alle Änderungen werden überschrieben
2. **OpenAPI ist Single Source of Truth** - Änderungen nur in YAML
3. **Interface-Only** - Keine Implementierungen, nur Contracts
4. **Spring Boot 3 + Jakarta EE** - Modern Stack

## 📦 Build-Artefakte

Nach `mvn install`:

- **Main JAR**: Interfaces + Models
- **Sources JAR**: Quellcode
- **Javadoc JAR**: API-Dokumentation

Installiert in lokales Maven Repository:

```
~/.m2/repository/io/pruefung/api/spi/ihk-content-provider-java-client/1.0.0-SNAPSHOT/
```

## 🔗 Verwandte Projekte

- **IHK GfI UForm API**: `../../ihk-gfi-uform-api/` - Ähnliche Projektstruktur
- **API Definitions**: `../../api-definitions/` - OpenAPI Spezifikationen
- **C# Client**: `../c#/` - C#-Version (geplant)

---

**Version**: 1.0.0-SNAPSHOT  
**OpenAPI Generator**: 7.10.0  
**Java**: 21  
**Spring Boot**: 3.4.1
