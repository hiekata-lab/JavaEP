#!/bin/bash -ue

usage_exit() {
  echo "Usage: $0 [-a] [-t username] [-r regexp?] ..." 1>&2
  exit 1
}
usage_help(){
  echo "Usage: ./delete_user.sh [OPTIONS] [ARGS]"
  echo "  This script is for deleting user information to initialize database of JavaEP."
  echo
  echo "Options:"
  echo "  -h, help"
  echo "  -a, don't need any arg. delete all users without admin."
  echo "  -r [regexp?], ex) -r '^[0-9]+$', delete students using regular expression."
  echo "  -t [username], ex) -t wanaka, delete the user."
  echo
  exit 1
}

while getopts ":t:ar:h" OPT
do
  case $OPT in
    a) OPT_FLAG_a=1;;
    t) OPT_FLAG_t=1;OPT_VALUE_t=$OPTARG ;;
    r) OPT_FLAG_r=1;OPT_VALUE_r=$OPTARG ;;
    h) usage_help;;
    :) echo  "[ERROR] Option argument is undefined.";;   
    \?) echo "[ERROR] Undefined options.";usage_exit;;
  esac
done

shift $(($OPTIND - 1))

if [ -n "${OPT_FLAG_a+UNDEF}" ];then
  cd ./db
  sed -e "s/targetuser/admin/g" delete_user.sql > tmp.sql
  sed -e "3,9s/=/!=/g" tmp.sql > delete_user_exec.sql
  mysql -u javaep -p < delete_user_exec.sql
  rm delete_user_exec.sql
  rm tmp.sql
  cd ../
fi

if [ -n "${OPT_FLAG_r+UNDEF}" ];then
  echo ${OPT_VALUE_r}
  cd ./db
  sed -e "s/targetuser/${OPT_VALUE_r}/g" delete_user.sql > tmp.sql
  sed -e "3,9s/=/regexp/g" tmp.sql > delete_user_exec.sql
  mysql -u javaep -p < delete_user_exec.sql
  rm delete_user_exec.sql
  rm tmp.sql
  cd ../
fi

if [ -n "${OPT_FLAG_t+UNDEF}" ];then
  cd ./db
  sed -e "s/targetuser/${OPT_VALUE_t}/g" delete_user.sql > delete_user_exec.sql
  mysql -u javaep -p < delete_user_exec.sql
  rm delete_user_exec.sql
  cd ../
fi
