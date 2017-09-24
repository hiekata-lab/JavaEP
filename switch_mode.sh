#!/bin/bash
cd `dirname $0`

which mvn > /dev/null 2>&1
if [ $? -eq 0 ]; then
  mvn --version
else
  echo "Maven is not installed"
  exit 0
fi

readonly CLASS_FILE="./src/main/resources/javaep.properties_class"
readonly EXAM_FILE="./src/main/resources/javaep.properties_exam"
readonly SETTING_FILE="./src/main/resources/javaep.properties"
readonly SECURITY_FILE="./src/main/webapp/WEB-INF/spring/security.xml"

if diff -q $CLASS_FILE $SETTING_FILE >/dev/null ; then
  echo "MODE SWITCH Exam mode"
  cp $EXAM_FILE $SETTING_FILE
  cp ${SECURITY_FILE}_exam $SECURITY_FILE
else
  echo "MODE SWITCH Class mode"
  cp $CLASS_FILE $SETTING_FILE
  cp ${SECURITY_FILE}_class $SECURITY_FILE
fi

mvn package
readonly TOMCAT_BIN="/opt/tomcat/apache-tomcat-8.5.20/bin"
readonly WEBAPPS_DIR="/opt/tomcat/apache-tomcat-8.5.20/webapps"
export PATH="${TOMCAT_BIN}:$PATH"

sudo service httpd stop
sudo ${TOMCAT_BIN}/shutdown.sh
sudo cp ./target/javaep-1.0.0-BUILD-SNAPSHOT.war "${WEBAPPS_DIR}/JavaEP.war"
sudo ${TOMCAT_BIN}/startup.sh
sudo service httpd start
