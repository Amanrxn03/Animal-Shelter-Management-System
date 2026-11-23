@echo off
setlocal
cd /d "%~dp0"

if "%JAVA_HOME%"=="" (
    if exist "C:\Users\amand\.jdks\jbr-21.0.11" (
        set "JAVA_HOME=C:\Users\amand\.jdks\jbr-21.0.11"
    ) else if exist "C:\Program Files\Java\jdk-24" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-24"
    )
)

if exist "%~dp0sbt-cli\bin\sbt.bat" (
    set "PATH=%JAVA_HOME%\bin;%~dp0sbt-cli\bin;%PATH%"
    call "%~dp0sbt-cli\bin\sbt.bat" test
) else (
    sbt test
)
endlocal

