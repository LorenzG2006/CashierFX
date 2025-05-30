@echo off
setlocal EnableDelayedExpansion

set "directory=%1"
set "extensions=%2"

if "%directory%" == "" (
  set "directory=."
)

if "%extensions%" == "" (
  set "extensions=*"
)

set "logs=logs"

set "logFile=%logs%\%date:/=-%_%time::=-%.log%"
set "logFile=%logFile: =0%"

if not exist "%logs%" (
  mkdir "%logs%"
)

set /a "fileCount=0"
set /a "lineCount=0"

for /r "%directory%"  %%i in ("%extensions%") do (
  set "filePath=%%i"
  
  for /f %%j in ('find /v /c "" ^< "!filePath!"') do (
    set /a "fileCount+=1"
    set /a "lineCount+=%%j"
    echo !fileCount!. %%~nxi - %%j lines >> "%logFile%"
  )
)

echo -------------------------------- >> "%logFile%"

echo Total data: %fileCount% files, %lineCount% lines >> "%logFile%"

type "%logFile%"

pause