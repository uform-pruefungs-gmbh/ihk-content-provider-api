# IHK Content Provider API

API und SPI-Definition für den Prüfungsmanager - einer Management-Shell für IHK Zwischen- und Abschlussprüfungen

## Was ist der Prüfungsmanager

Der u-form Prüfungsmanager ist eine Managementplattform für die Durchführung von Prüfungen. Der Schwerpunkt liegt in der Durchführung von digitalen Zwischen- und Abschlussprüfungen der IHK-Organisationen.

Die Plattform besteht aus einem Managementanteil, die das Handling von Teilnehmenden und Teilnahmen managt. Eine Teilnahmen bedeutet: Die Teilnahme einer Person an einer Prüfung zu einem Zeitpunkt für ein Prüfungsfach.

Die zu prüfenden Inhalte stellen "content provider" (Inhaltsanbieter) zur Verfügung. Um die Inhalte zum richtigen Zeitpunkt mit Teilnahmen zu verbunden, den Ablauf der Prüfung zu steuern und zu überwachen sowie nach Abschluss der Prüfung die Ergebnisse zur Auswertung an die [IHK-GfI GmbH](https://www.ihk-gfi.de/) zu übermitteln, implementieren die Inhaltsanbieter eine Serviceschnittstelle (SPI). Die Registrierung eines Inhaltsanbieters erfolgt wiederum über eine API im Prüfungsmanager.

Kurz dargestellt:

- **API:** Registrierung und Überarbeitung von Inhaltsanbieterdaten (insbesondere Aufruflinks)
- **SPI:** Durch den Inhaltsanbieter zur realisierende Funktionalitäten

Beide Schnittstellen sind in diesem Repository als OpenAPI 3.0.x-Definition beschrieben.

## Konzept des SPI

Die Grundfunktionen des SPI sind im Wesentlichen:

- Auflistung der freigegebenen Inhalte
- Erzeugung von Zugangsdaten für Teilnahmen (One Time Password, TAN)
- Reaktion auf Statusänderungen durch die Managementshell (Angelegt, Freigegeben, Gesperrt,...)
- Subscribe/Unsubscribe von Monitoringdaten während der Prüfung (der Inhaltsanbieter liefert während einer Subscription Statusänderungen an die Shell)
- Abruf von Eingabeedaten nach Beendigung der Durchführung zur Weitergabe an die GfI

Alle Aufrufe erfolgen durch den Prüfungsmanager an den/die Inhaltsanbieter. Ausnahme ist hier eine laufende Subscription. In diesem Fall werden die Daten über SSE oder Websockets an den Prüfungsmanager gesendet.

## Inhaltsanbieter (content provider)

Inhaltsanbieter sind für die Bereitstellung der Prüfungsinhalte verantwortlich. Sie implementieren die SPI, um mit dem Prüfungsmanager zu kommunizieren und die erforderlichen Funktionen bereitzustellen.

## Konzept des Datenaustauschmodells

Das Datenaustauschmodell beschreibt die Struktur der Daten, die zwischen dem Prüfungsmanager und den Inhaltsanbietern sowie integrierten Systemen ausgetauscht werden.

Das Datenmodel ist in mehrere Level unterteilt, die jeweils unterschiedliche Aspekte der Prüfungsinhalte und -strukturen abbilden. Die Level sind:

- **Level 0:** Basisinformationen zu Prüfungen inkl. Metadaten. Basierend auf dem Standard xAPI 2.0 und CMI5.
- **Level 1:** Grundlegende Struktur der Prüfungsfragen. Insbesondere eine Liste der Prüfungsfragen.
- **Level 2:** Detaillierte Informationen zu Prüfungsfragen und -antworten. Diese Inhalte sind von Anwendungsfall und den Kommunikationspartnern abhängig.

![Überblick über das Datenaustauschmodell](./docs/images/bild-datenaustausch-ueberblick.png)

Die Definitionen sind in JSON Schema und OpenAPI 3.0.x-Format verfügbar.

## Technische Schnittstellendefinitionen

### Beschreibung der API

Die technischen Definitionen beschreiben die Schnittstellen im OpenAPI-Format V3

[Technische Definition der Content Provider API](api.html)

[Technische Definition der Content Provider SPI](spi.html)

### JSON Schema-Definitionen für Prüfungsdatenstrukturen

Die JSON Schema-Definitionen beschreiben die Datenstrukturen für die verschiedenen Level des Datenaustauschmodells:

**Wiederverwendbare Typdefinitionen:**

- [CMI5 Types](./types/cmi5.json) - CMI5-konforme Kursstrukturen und Assignable Units
- [IHK Common Types](./types/ihk-common.json) - Grundlegende Datentypen für das IHK-System
- [IHK Level 1 Types](./types/ihk-level1.json) - Strukturen für Prüfungsfragen und Metadaten
- [IHK Level 2 Types](./types/ihk-level2-example.json) - Erweiterte Beispieltypen

## JSON-Beispielstruktur für CMI5-konforme Prüfungsinhalte

Nachfolgend ein Beispiel für eine CMI5-konforme JSON-Struktur mit einer Assignable Unit (AU) für "Medienkaufleute Digital und Print":

```json
{
  "courseStructure": {
    "course": {
      "id": "https://example.pruefung.io/courses/zp/f25/5598/medienkaufleute-2025",
      "title": {
        "langstring": [
          {
            "lang": "de",
            "value": "ZP F25 Medienkaufleute Digital und Print"
          }
        ]
      },
      "description": {
        "langstring": [
          {
            "lang": "de",
            "value": "IHK-Zwischenprüfung Frühjahr 2025 für Medienkaufleute Digital und Print"
          }
        ]
      }
    },
    "au": {
      "id": "https://example.pruefung.io/au/zp/f25/5598/wiso/medienkaufleute-2025-wiso",
      "moveOn": "Completed",
      "masteryScore": 1,
      "launchMethod": "AnyWindow",
      "activityType": "examination",
      "title": {
        "langstring": [
          {
            "lang": "de",
            "value": "ZP F25 Medienkaufleute Digital und Print Wiso"
          }
        ]
      },
      "description": {
        "langstring": [
          {
            "lang": "de",
            "value": "IHK Zwischenprüfung Frühjahr 2025 für Medienkaufleute Digital und Print. Prüfungsbereich Wiso"
          }
        ]
      },
      "url": "https://zp.pruefung.io",
      "pmdata": {
        "metadata": {
          "berufsnummer": "5598",
          "pruefungszeitpunkt": "F25",
          "pruefungsbereich": "Wiso",
          "erstellungseinrichtung": "ZPA"
        },
        "fragen": [
          {
            "id": "1",
            "titel": "Was ist der Hauptzweck des Marketings?",
            "interactiontypes": "fill-in"
          }
        ]
      }
    }
  }
}
```

Diese JSON-Struktur enthält:

**Course-Definition:**

- Eindeutige ID für den Kurs
- Titel und Beschreibung in mehreren Sprachen (hier Deutsch)

**Assignable Unit (AU) für "Medienkaufleute Digital und Print":**

- Eindeutige ID für die AU
- CMI5-konforme Attribute:
  - `moveOn`: "Completed" - Kriterium zum Fortschritt
  - `masteryScore`: 1 - Erforderlicher Score für Bestehen
  - `launchMethod`: "AnyWindow" - Startmethode
  - `activityType`: "examination" - Aktivitätstyp
- Launch-URL für die Prüfungsumgebung
- IHK-spezifische Prüfungsmetadaten (Level 1):
  - Berufsnummer "5598"
  - Prüfungszeitpunkt "F25" (Frühjahr 2025)
  - Prüfungsbereich "Wiso"
  - Erstellungseinrichtung "ZPA"
- Prüfungsfragen mit ID, Titel und Interaktionstyp

Die JSON-basierte Struktur bietet gegenüber XML folgende Vorteile:

- Einfachere Verarbeitung und Parsing
- Bessere Unterstützung durch moderne Web-Frameworks
- Kompaktere Datenübertragung
- Native Unterstützung in JavaScript und anderen modernen Programmiersprachen
- Umfangreiche Validierungsmöglichkeiten durch JSON Schema
