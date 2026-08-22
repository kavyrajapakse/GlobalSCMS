@REM ----------------------------------------------------------------------------
@REM Maven Wrapper startup batch script for Windows
@REM ----------------------------------------------------------------------------

@if "%DEBUG%" == "" @echo off
@classprocessor -version >nul 2>&1

set ERROR_CODE=0
set MAVEN_PROJECTBASEDIR=%~dp0

if not "%MAVEN_PROJECTBASEDIR%" == "" goto OkBaseDir
set MAVEN_PROJECTBASEDIR=.
:OkBaseDir

set MAVEN_CONFIG=.mvn

@REM Execute Maven
mvn %*

if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
@exit /B %ERROR_CODE%
