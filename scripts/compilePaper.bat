cwd=%cd%

cd libs\Paper

gradlew.bat applyPatches
gradlew.bat reobfJar

cd %cwd%
