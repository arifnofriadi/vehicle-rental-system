@echo off
chcp 65001 > nul
echo Compiling Java files...
if not exist "bin" mkdir bin

powershell -NoProfile -ExecutionPolicy Bypass -Command "$files = Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName }; javac -encoding UTF-8 -cp 'lib/*;src' -d bin $files"

if %ERRORLEVEL% EQU 0 (
    echo Menjalankan aplikasi...
    java -cp "bin;lib/*" Main
) else (
    echo Kompilasi gagal!
)
pause

