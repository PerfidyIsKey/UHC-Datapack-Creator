@echo off
cd..
cd Server
start java -Xmx8000M -Xms8000M -jar paper-1.21.1-15.jar nogui

pause

cd..
cd src