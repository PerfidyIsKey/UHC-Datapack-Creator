@echo off
cd..
cd Server
start java -Xmx8000M -Xms8000M -jar paper-1.21.3-81.jar nogui

pause

cd..
cd src