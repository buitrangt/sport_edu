@echo off
echo ===== FINAL FIX FOR TOURNAMENT API =====
cd /d "C:\Users\ACER\Desktop\be"

echo Stopping any running process...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":8080" ^| find "LISTENING"') do taskkill /f /pid %%a 2>nul

echo Building project...
call gradlew clean build -x test
if %ERRORLEVEL% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)

echo ===== BUILD SUCCESSFUL =====
echo Starting application...
echo.
echo Backend will be ready at: http://localhost:8080
echo Tournament API endpoint: http://localhost:8080/api/tournaments/with-image
echo.
start cmd /k "call gradlew bootRun"

echo Waiting 15 seconds for application to start...
timeout /t 15 /nobreak

echo Testing basic endpoint...
curl -X GET "http://localhost:8080/api/tournaments" -H "Accept: application/json"
echo.
echo.
echo ===== READY FOR FRONTEND TESTING =====
echo 1. Backend is running on port 8080
echo 2. Frontend can now call /api/tournaments/with-image
echo 3. Check backend console for detailed logs
pause
