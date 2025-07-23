@echo off
setlocal enabledelayedexpansion

REM Set server and JAR info
set SERVER_IP=uhc.mr9madness.com
set SERVER_PORT=25565
set JAR=mc-bots-1.2.14.jar
set BASE_CMD=java -jar ..\plugins\%JAR% -s %SERVER_IP%:%SERVER_PORT%
set NICKNAMES_FILE=..\Files\DIORITE\nicknames.txt

REM Clean up old nicknames file
if exist "%NICKNAMES_FILE%" del "%NICKNAMES_FILE%"

REM Initialize bot count
set BOT_COUNT=0

REM Create the nicknames file
for /f "tokens=1,2,3,4,5 delims=," %%A in (..\Files\DIORITE\players.txt) do (
    if "%%E"=="true" (
        set "USERNAME=%%B"
        echo !USERNAME!>>"%NICKNAMES_FILE%"
        set /a BOT_COUNT+=1
    )
)

REM Build the final command
set FINAL_CMD=%BASE_CMD% --nicks "%NICKNAMES_FILE%" -c %BOT_COUNT%

REM Start the bot process in a new console window (no cleanup)
start "" cmd /k "%FINAL_CMD%"

endlocal