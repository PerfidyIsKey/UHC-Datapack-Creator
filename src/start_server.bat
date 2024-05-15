@echo off
cd..
cd Server
start java -Xmx4000M -Xms4000M -jar paper-1.20.4-401.jar nogui

pause

cd..
cd src