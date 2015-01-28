set JAR="C:\Program Files\Java\jdk1.8.0_05\bin\jar.exe"
@echo off
del WowSync.jar
cd classes
%JAR% -cvfm ../WowSync.jar ../Manifest.txt wowsync
cd ..
%JAR% -uvf WowSync.jar images
pause
@echo on
