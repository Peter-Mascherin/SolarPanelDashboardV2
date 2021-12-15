
@echo OFF
set /A puttyinstance=0
start javaw --module-path "C:\Users\David Bradley\Desktop\openjfx-17.0.0.1_windows-x64_bin-sdk\javafx-sdk-17.0.0.1\lib" --add-modules javafx.fxml,javafx.controls -jar "C:\Users\David Bradley\Desktop\WorkStudySolarTech2021\SolarPanelDashboardV2Export\SolarPanelDashboardV2.jar"
set /p boardid="Please enter a board id (Currently either 2 , 4 , 5 , 8 , or 7): "
IF %boardid%==8 (CALL :boardeight)
IF %boardid%==2 (CALL :boardtwo)
IF %boardid%==4 (CALL :boardfour)
IF %boardid%==5 (CALL :boardfive)
IF %boardid%==7 (CALL :boardseven) ELSE (CALL :wrongid)
EXIT /B %ERRORCODE%

:wrongid
ECHO "Either you entered something wrong or something else went wrong , please restart or read the documentation"
PAUSE
EXIT /B 0

:boardeight
FOR %%x IN (1) DO CALL :boardeightconnect
goto boardeight
EXIT /B 0

:boardtwo
FOR %%x IN (1) DO CALL :boardtwoconnect
goto boardtwo
EXIT /B 0

:boardfour
FOR %%x IN (1) DO CALL :boardfourconnect
goto boardfour
EXIT /B 0

:boardfive
FOR %%x IN (1) DO CALL :boardfiveconnect
goto boardfive
EXIT /B 0

:boardseven
FOR %%x IN (1) DO CALL :boardsevenconnect
goto boardseven
EXIT /B 0

:boardeightconnect
for /F "tokens=2 delims==; " %%I in ('wmic process call create "putty.exe -load "COM9CONNECTION"" ^| find "ProcessId"') do set /A puttyinstance=%%I
TIMEOUT /t 30 /NOBREAK
taskkill /F /PID %puttyinstance%
TIMEOUT /t 3 /NOBREAK
EXIT /B 0

:boardtwoconnect
for /F "tokens=2 delims==; " %%I in ('wmic process call create "putty.exe -load "COM3CONNECTION"" ^| find "ProcessId"') do set /A puttyinstance=%%I
TIMEOUT /t 30 /NOBREAK
taskkill /F /PID %puttyinstance%
TIMEOUT /t 3 /NOBREAK
EXIT /B 0

:boardfourconnect
for /F "tokens=2 delims==; " %%I in ('wmic process call create "putty.exe -load "COM5CONNECTION"" ^| find "ProcessId"') do set /A puttyinstance=%%I
TIMEOUT /t 30 /NOBREAK
taskkill /F /PID %puttyinstance%
TIMEOUT /t 3 /NOBREAK
EXIT /B 0

:boardfiveconnect
for /F "tokens=2 delims==; " %%I in ('wmic process call create "putty.exe -load "COM8CONNECTION"" ^| find "ProcessId"') do set /A puttyinstance=%%I
TIMEOUT /t 30 /NOBREAK
taskkill /F /PID %puttyinstance%
TIMEOUT /t 3 /NOBREAK
EXIT /B 0

:boardsevenconnect
for /F "tokens=2 delims==; " %%I in ('wmic process call create "putty.exe -load "COM13CONNECTION"" ^| find "ProcessId"') do set /A puttyinstance=%%I
TIMEOUT /t 30 /NOBREAK
taskkill /F /PID %puttyinstance%
TIMEOUT /t 3 /NOBREAK
EXIT /B 0

