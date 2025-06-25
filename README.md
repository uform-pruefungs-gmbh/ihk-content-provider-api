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
