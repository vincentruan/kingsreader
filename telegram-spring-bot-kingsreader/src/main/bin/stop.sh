#!/bin/sh
#set JAVA_HOME
#JAVA_HOME=/usr/alibaba/java

#check JAVA_HOME & java
noJavaHome=false
if [ -z "$JAVA_HOME" ] ; then
    noJavaHome=true
fi
if [ ! -e "$JAVA_HOME/bin/java" ] ; then
    noJavaHome=true
fi
if $noJavaHome ; then
    echo
    echo "Error: JAVA_HOME environment variable is not set."
    echo
    exit 1
fi
#==============================================================================

#set JAVA_OPTS
JAVA_OPTS="-Xss256k"
#==============================================================================

DIR=`pwd`
cd `dirname $0`
cd ../lib
LIB_DIR=`pwd`

SERVER_NAME='com.github.telegram.bot.KingsReaderApp'

PIDS=`ps -ef | grep java | grep "$LIB_DIR" | grep ${SERVER_NAME} | awk '{print $2}'`
if [ -z "$PIDS" ]; then
    echo "stop failed, the $SERVER_NAME not start!"
    exit 1
fi

for PID in ${PIDS};
do
    kill ${PID} > /dev/null 2>&1
done

echo "stop success! pid:"${PIDS}
cd ${DIR}