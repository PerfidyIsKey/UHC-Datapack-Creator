@echo off
setlocal enabledelayedexpansion

REM Set your server and JAR file variables
set SERVER_IP=192.168.0.115
set SERVER_PORT=25565
set JAR=mc-bots-1.2.14.jar

REM Construct the base command using the variables
set BASE_CMD=java -jar ..\Server\plugins\%JAR% -s %SERVER_IP%:%SERVER_PORT%

REM Create a temporary file for nicknames
set NICKNAMES_FILE=..\Files\DIORITE\nicknames.txt

REM Initialize the bot count
set BOT_COUNT=0

REM Loop through each line in the players file
for /f "tokens=1,2,3,4,5 delims=," %%A in (..\Files\DIORITE\players.txt) do (
    if "%%E"=="true" (
        set "USERNAME=%%B"
        REM Write the username to the nicknames file
        echo %%B >> "%NICKNAMES_FILE%"

        REM Increment the bot count
        set /a BOT_COUNT+=1
    )
)

REM Construct the final command to launch the bots with the nickname file
set FINAL_CMD=!BASE_CMD! --nicks "%NICKNAMES_FILE%" -c !BOT_COUNT!

REM Start the bots
start cmd /k "!FINAL_CMD!"