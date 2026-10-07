@echo off
setlocal

rem Change to the project directory if not already there
cd "%~dp0"

rem Call the Maven wrapper JAR
call .mvn\wrapper\maven-wrapper.jar %*

exit /b %errorlevel%