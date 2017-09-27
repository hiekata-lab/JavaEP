#!/bin/bash
cd `dirname $0`

which mvn > /dev/null 2>&1
if [ $? -eq 0 ]; then
  mvn --version
else
  echo "Maven is not installed"
  exit 0
fi

which mvn > /dev/null 2>&1
if [ $? -eq 0 ]; then
  echo 'Checking update ....'
  git fetch
  current_rev=`git rev-parse HEAD`
  remote_rev=`git rev-parse origin/HEAD` 
  if [ ${current_rev} = ${remote_rev} ]; then
    echo 'Your repository is the latest'
  else
    echo -e '\033[31m[WARNING]:Your repository is already out of date. Please git pull and update.\033[m'
  fi
else
  echo "Git is not installed"
  exit 0
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
