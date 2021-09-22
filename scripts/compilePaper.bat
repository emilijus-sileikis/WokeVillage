@echo off
set cwd=%cd%

cd libs\Paper

gradlew.bat --no-daemon applyPatches
gradlew.bat --no-daemon reobfJar

cd %cwd%
