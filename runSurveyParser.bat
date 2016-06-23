@ECHO OFF

set CLASSPATH=%CLASSPATH%;./lib/commons-csv-1.3.jar;./lib/commons-io-2.5.jar;./lib/log4j-1.2.17.jar;./bin

java com.survey.fileparser.SurveyParser
