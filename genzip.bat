set ZIP="C:\Program Files\7-Zip\7z.exe"
@echo off
del WowSync.7z
%ZIP% a WowSync.7z lib WowSync.jar WowSync_config.bat WowSync_launch.bat
@echo on
