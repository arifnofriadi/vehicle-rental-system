# Script otomatis compile & run untuk PowerShell
$ErrorActionPreference = "Stop"

Write-Host "Compiling Java files..." -ForegroundColor Cyan
if (-not (Test-Path "bin")) {
    New-Item -ItemType Directory -Path "bin" | Out-Null
}

$files = Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -cp "lib/*;src" -d bin $files

if ($LASTEXITCODE -eq 0) {
    Write-Host "Menjalankan aplikasi..." -ForegroundColor Green
    java -cp "bin;lib/*" Main
} else {
    Write-Host "Kompilasi gagal!" -ForegroundColor Red
}

