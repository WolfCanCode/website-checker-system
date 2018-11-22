@ECHO OFF
REM ############################################################################
REM #
REM #   Title       : Website Checker System Batch
REM #   Bat Name    : rotationDB.bat
REM #   Create      : TruongLN
REM #   Create date : 22/11/2018
REM #
REM ############################################################################


SETLOCAL ENABLEDELAYEDEXPANSION

REM ########################################################
REM # SET PARAMETER
REM ########################################################

SET PASSWORD=12345678
SET USER=root
SET SCRIPT_BASE=C:\Users\ngoct\Documents\capstone\wcs\website-checker-system

ECHO ---------------------
ECHO  CONNECTING TO DB
ECHO ---------------------
mysql -u %USER% -p%PASSWORD% -h 127.0.0.1 wcsdb < %SCRIPT_BASE%\script.sql
ECHO ---------------------
ECHO  ROTATION COMPLETED
ECHO ---------------------

pause
