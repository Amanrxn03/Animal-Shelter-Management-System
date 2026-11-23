@echo off
setlocal
cd /d "%~dp0"
echo Pushing Animal Shelter Management System to GitHub...
git push -u origin main
if %ERRORLEVEL% equ 0 (
    echo Successfully pushed to GitHub!
) else (
    echo Push failed. Please verify that the repository exists on GitHub and that you are authenticated.
)
endlocal
