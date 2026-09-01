@REM ----------------------------------------------------------------------------
@REM Maven Wrapper startup batch script for Windows
@REM ----------------------------------------------------------------------------

@if "%DEBUG%" == "" @echo off
setlocal enabledelayedexpansion

set ERROR_CODE=0
set MAVEN_CMD=mvn

@REM Check if mvn is available in system PATH
where mvn >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    @REM Search for IntelliJ IDEA bundled Maven
    for /d %%D in ("C:\Program Files\JetBrains\IntelliJ IDEA*") do (
        if exist "%%D\plugins\maven\lib\maven3\bin\mvn.cmd" (
            set "MAVEN_CMD=%%D\plugins\maven\lib\maven3\bin\mvn.cmd"
        )
    )
)

"%MAVEN_CMD%" %*

if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
@exit /B %ERROR_CODE%
