# Build Script for Java Client

# Dieser Script führt den Maven Build für das Java Client Modul durch
# Voraussetzung: Maven muss installiert sein

param(
    [switch]$Clean,
    [switch]$SkipTests,
    [switch]$GenerateOnly
)

# Farben für Output
$ErrorColor = "Red"
$SuccessColor = "Green"
$InfoColor = "Cyan"

# Projektverzeichnis
$ProjectRoot = $PSScriptRoot
Set-Location $ProjectRoot

Write-Host "`n=== IHK Content Provider Java Client Build ===" -ForegroundColor $InfoColor
Write-Host "Projektverzeichnis: $ProjectRoot`n" -ForegroundColor $InfoColor

# Prüfe ob Maven Wrapper verfügbar ist (bevorzugt)
$mvnWrapper = Join-Path $ProjectRoot ".\mvnw.cmd"
$mvnCommand = $null

if (Test-Path $mvnWrapper) {
    $mvnCommand = $mvnWrapper
    Write-Host "Maven Wrapper gefunden: $mvnWrapper" -ForegroundColor $SuccessColor
}
else {
    # Fallback: Prüfe ob Maven im PATH verfügbar ist
    $mvnCmd = Get-Command mvn -ErrorAction SilentlyContinue
    if ($mvnCmd) {
        $mvnCommand = "mvn"
        Write-Host "Maven gefunden: $($mvnCmd.Source)" -ForegroundColor $SuccessColor
    }
    else {
        Write-Host "FEHLER: Weder Maven Wrapper noch Maven (mvn) gefunden!" -ForegroundColor $ErrorColor
        Write-Host "`nBitte eine der folgenden Optionen:" -ForegroundColor $InfoColor
        Write-Host "  1. Maven Wrapper sollte in '../' verfügbar sein" -ForegroundColor $InfoColor
        Write-Host "  2. Oder installieren Sie Maven: https://maven.apache.org/download.cgi" -ForegroundColor $InfoColor
        exit 1
    }
}

# Prüfe ob OpenAPI-Datei existiert
$openApiFile = Join-Path $ProjectRoot "..\..\api-definitions\src\main\resources\openapi\ihk-content-provider-spi.yaml"
if (-not (Test-Path $openApiFile)) {
    Write-Host "FEHLER: OpenAPI-Spezifikation nicht gefunden!" -ForegroundColor $ErrorColor
    Write-Host "Erwartet: $openApiFile" -ForegroundColor $InfoColor
    exit 1
}

Write-Host "OpenAPI-Spezifikation gefunden: $openApiFile`n" -ForegroundColor $SuccessColor

# Build-Befehl zusammenstellen
$buildArgs = @()

if ($Clean) {
    $buildArgs += "clean"
}

if ($GenerateOnly) {
    $buildArgs += "generate-sources"
    Write-Host "Führe Code-Generierung aus..." -ForegroundColor $InfoColor
}
else {
    $buildArgs += "install"
    Write-Host "Führe vollständigen Build aus..." -ForegroundColor $InfoColor
}

if ($SkipTests) {
    $buildArgs += "-DskipTests"
}

# Maven Build ausführen
Write-Host "`nMaven-Befehl: $mvnCommand $($buildArgs -join ' ')`n" -ForegroundColor $InfoColor

try {
    & $mvnCommand $buildArgs
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "`n=== Build erfolgreich ===" -ForegroundColor $SuccessColor
        
        if ($GenerateOnly) {
            $generatedSourcesPath = Join-Path $ProjectRoot "target\generated-sources\openapi\src\main\java"
            if (Test-Path $generatedSourcesPath) {
                Write-Host "`nGenerierte Interfaces:" -ForegroundColor $InfoColor
                Get-ChildItem -Path $generatedSourcesPath -Recurse -Filter "*.java" | 
                Select-Object -First 10 | 
                ForEach-Object { Write-Host "  - $($_.FullName.Replace($generatedSourcesPath, ''))" }
            }
        }
        else {
            $jarFile = Get-ChildItem -Path (Join-Path $ProjectRoot "target") -Filter "*.jar" | 
            Where-Object { $_.Name -notmatch "(sources|javadoc)" } | 
            Select-Object -First 1
            
            if ($jarFile) {
                Write-Host "`nErzeugte Artefakte:" -ForegroundColor $InfoColor
                Write-Host "  - $($jarFile.FullName)" -ForegroundColor $SuccessColor
                Write-Host "  - Größe: $([math]::Round($jarFile.Length / 1KB, 2)) KB" -ForegroundColor $InfoColor
            }
        }
    }
    else {
        Write-Host "`n=== Build fehlgeschlagen ===" -ForegroundColor $ErrorColor
        exit $LASTEXITCODE
    }
}
catch {
    Write-Host "`nFEHLER beim Build: $_" -ForegroundColor $ErrorColor
    exit 1
}

Write-Host ""
