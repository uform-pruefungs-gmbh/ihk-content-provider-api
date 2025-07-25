# Define the path to the schema viewer HTML file
$htmlPath = Join-Path $PSScriptRoot "..\api-definitions\src\main\resources\xsdviewer\schemaviewer.html"

# Verify if the file exists
if (-not (Test-Path $htmlPath)) {
    Write-Error "Schema viewer HTML file not found at: $htmlPath"
    exit 1
}

# Get the directory containing the HTML file
$serverRoot = Join-Path $PSScriptRoot "..\tmp"

# Start the http-server using npx
Write-Host "Starting web server in directory: $serverRoot"
Write-Host "Press Ctrl+C to stop the server"

try {

    # Create temp directory if it doesn't exist
    New-Item -ItemType Directory -Force -Path ".\tmp" | Out-Null

    $schemaViewerOutputPath = Join-Path $serverRoot "schemaviewer.html"
    $cmi5ContentPath = Join-Path $PSScriptRoot "..\api-definitions\src\main\resources\xsd\ihk-level1.xsd"

    # Read the content of both files
    $schemaViewerContent = Get-Content -Path $htmlPath -Raw

    $cmi5Content = Get-Content -Path $cmi5ContentPath -Raw

    # Replace the placeholder with cmi5 content
    $newContent = $schemaViewerContent -replace "ThIs-Is-ASampleSchema", $cmi5Content

    # Write the modified content to the new file
    Set-Content -Path $schemaViewerOutputPath -Value $newContent

    Write-Host "Server started successfully. You can access the schema viewer at: http://localhost:8080/schemaviewer.html"
    npx http-server $serverRoot
}
catch {
    Write-Error "Failed to start http-server: $_"
    exit 1
}