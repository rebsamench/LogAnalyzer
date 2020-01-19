# LogAnalyzer
Projektarbeit für “MAS I 13 - Programmiersprachen - Vertiefung Java” von Christoph Rebsamen und Simon Rizzi

## Umfeld der Aufgabenstellung
Das System der Gebäudeautomation wird in unterschiedliche Ebenen gegliedert. Man spricht hier auch von der sogenannten Automationspyramide. Die vorliegende Arbeit befasst sich mit Geräten und Kommunikationsflüssen auf der Feldebene.

Die Geräte der Feldebene werden untereinander und mit den übergeordneten Steuerungen (DDC, SPS o.Ä.) zunehmend mit digitalen Bussystemen vernetzt. Feldgeräte können z.B. Antriebe von Lüftungsklappen, Wasserventilen, oder auch Brandschutzklappen sein. Die Digitalisierung stellt die Baubranche vor grosse Herausforderungen. So stellt die Fehlersuche und Diagnose heute andere Anforderungen wie bei den rein analogen Verdrahtungen der Vergangenheit.
Topologie der Feldebene
Ein Gateway stellt die Verbindung zwischen einem Busstrang und dem Feldgerät, in diesem Fall einem Lüftungsklappenantrieb her. Es ermöglicht unter anderem die Einbindung eines Geräts mit analoger Ansteuerung an einen digitalen Gebäudebus.


Über dieses Gateway läuft die Kommunikation zum Beispiel in Form von Steuerbefehlen. Zusätzlich erzeugt es Status und Fehlermeldungen und stellt diese zur Verfügung.

## Use Case
Die im Rahmen dieser Arbeit entwickelte Software soll die Auswertung von Daten, Zuständen und Befehlen, die über ein solches Gateway laufen, ermöglichen und vereinfachen. In kurzer Zeit können grosse Datenmengen anfallen, die effizient ausgewertet und verwaltet werden sollen. Die Auswertung soll Rückschlüsse auf Probleme und Fehler ermöglichen.

## Scope
LogAnalyzer ist als Proof of Concept umgesetzt. Mit LogAnalyzer will Belimo prüfen, ob die programmunterstützte Analyse der Logdaten einen Mehrwert bringt. LogAnalyzer wird dann entweder weiterentwickelt oder es wird die Anschaffung einer auf dem Markt erhältlichen Analyselösung geprüft. Folgende Features wurden in LogAnalyzer vorbereitet, aber nicht implementiert:
* Benutzerlogin: Die Benutzerdaten (eindeutiger Benutzername, Passwort und Berechtigung) können verwaltet werden und die Logdaten mit vorhandenen Benutzern importiert werden. Es findet aber kein Login statt und die Passwörter sind als plain Text in der Datenbank hinterlegt.
* Analyse: Die Analyse der Logdaten kann mit detaillierten Abfragen gezielt erfolgen. Analyseabfragen (z. B. bekannte Fehlermuster) können aber noch nicht abgespeichert werden.

## Features
* Erfassung und Änderung von Basisdaten User (Benutzer), Site (Anlage) und Bus line (Buslinie):
  * Neuerfassung mit Beschränkung der Anzahl Zeichen auf max. Feldlänge der Datenbank
  * Übersicht über vorhandene Basisdaten in Tabelle und direkte Änderung in editierbarer Tabelle. Änderung kann mit ENTER in der Tabelle committed und anschliessend in die Datenbank gespeichert werden.
* Relationale Datenbank stellt konsistente Daten sicher
* Import:
  * Import von mehreren Log-Dateien zu eine Buslinie
  * Automatische Ermittlung der Busadresse aus dem Logfile und Ergänzung des Logrecords mit dieser Adresse
  * Bildung eines eindeutigen Schlüssels für jeden Record verhindert Duplikate in der Datenbank.
  * Automatische Ergänzung jedes Logrecords mit User, Site und Busline.
* Einstellungen (Properties-Datei oder in LogAnalyzer auf dem Panel ‘Settings’):
  * Einstellung und Test der Datenbankverbindung (JDBC-URL, Benutzername, Passwort)
  * Wahl des Panels, das beim Start von LogAnalyzer angezeigt wird
  * Einstellung, ob die Meldungen in der globalen Meldungsbox beim Wechsel zwischen Panels gelöscht werden sollen.
* Auswertung der in der Datenbank gespeicherten Logdaten:
  * Dynamisches Suchformular mit zahlreichen Suchkriterien
  * Wahl von mehreren beliebigen Logrecords aus der Resultattabelle, die in einer Vergleichsansicht weiter analysiert werden sollen. Wahl des Zeitraums (x Sekunden vor/nach dem gewählten Ereignis).
  * Export der Suchresultate nach Microsoft Excel
* Vorbereitung für Benutzerverwaltung (Datenmodell) und Prüfung der Admin-Berechtigung für einzelne Panels
* Maven-Build mit automatischer Fat-Jar- und Javadoc-Erstellung und Export von Testdaten, Propertiesdatei und Datenbankskripts

## Systemvoraussetzungen
* MySQL Server ab Version 8.0.
* Java-Laufzeitumgebung ab Version 11
* Github-Account ist eingerichtet und Git Connector sind installiert.

## Installation
Folgende Installationsschritte sind nötig, um LogAnalyzer in Betrieb zu nehmen:
1. Datenbank aufsetzen. Skript createdatabase.sql in MySQL 8 Command Line Client ausführen. Das Skript ist in src/main/resources/scripts oder nach Ausführung von mvn package im Unterverzeichnis scripts des target-Verzeichnisses enthalten.
2. Projekt https://github.com/rebsamench/LogAnalyzer in IDE auschecken.
3. Maven clean und install (Befehl mvn clean install) ausführen.
   - LogAnalyzer starten.
   - Aus IDE: Startkonfiguration für LogAnalyzer in IDE einrichten. Als Main-Methode die Methode main in der Klasse Launcher wählen. Über erstellte Startkonfiguration ausführen.
   - Über generierte JAR-Datei: java -DCONFIG_DIR=config -jar LogAnalyzer-1.0-jar-with-dependencies.jar in Konsole oder aus Batchskript aufrufen. Beispielaufruf für Windows:

Der Startparameter CONFIG_DIR bestimmt, in welchem Verzeichnis die Datei loganalyzer.properties liegt. Standardmässig ist es das Verzeichnis config im Verzeichnis target, das beim Paketieren über maven erstellt wird.

## Anwendung (Workflow)
1. Basisdaten auf dem Panel Base Data erfassen. Die Basisdaten sind Voraussetzung für einen Datenimport.
2. Logrecords auf dem Panel Import importieren. Testdaten stehen nach mvn install im Verzeichnis testdata zur Verfügung.
3. Logrecords auf dem Panel Reports auswerten.
