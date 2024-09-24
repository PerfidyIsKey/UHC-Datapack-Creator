@echo off
cd..
cd Server
start java -Xmx8000M -Xms8000M -jar paper-1.21.1-40.jar nogui

pause

cd..
cd src