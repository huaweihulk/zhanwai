#!/bin/sh

function usage(){

	echo "USAGE: $0 start|stop [options]" 
	echo " e.g.: $0 start -L" 

}

if [ $# = 0 ] ; then 
	usage
exit 1; 
fi 

FINDNAME=$0
while [ -h $FINDNAME ] ; do FINDNAME=`ls -ld $FINDNAME | awk '{print $NF}'` ; done
SERVER_HOME=`echo $FINDNAME | sed -e 's@/[^/]*$@@'`
unset FINDNAME

if [ "$SERVER_HOME" = '.' ]; then
   SERVER_HOME=$(echo `pwd` | sed 's/\/bin//')
else
   SERVER_HOME=$(echo $SERVER_HOME | sed 's/\/bin//')
fi

#默认的JVM配置
MODULE=$(basename $SERVER_HOME)

HEAP_MEMORY=256m

PERM_MEMORY=64m

JAVA_OPTS="-server -Djava.nio.channels.spi.SelectorProvider=sun.nio.ch.EPollSelectorProvider -XX:+HeapDumpOnOutOfMemoryError"
    
case $1 in
start)

    shift
    ARGS=($*)
    for ((i=0; i<${#ARGS[@]}; i++)); do
        case "${ARGS[$i]}" in
        -Opts*) JAVA_OPTS="${JAVA_OPTS} ${ARGS[$i+1]}" ;;
        -Heap*) HEAP_MEMORY="${ARGS[$i+1]}" ;;
        -Perm*) PERM_MEMORY="${ARGS[$i+1]}" ;;
        -JmxPort*)  JMX_PORT="${ARGS[$i+1]}" ;;
	-L*) LOG_NOHUP="ON";;
          *) parameters="${parameters} ${ARGS[$i]}" ;;
        esac
    done
    
    JAVA_OPTS="${JAVA_OPTS} -Xms$HEAP_MEMORY -Xmx$HEAP_MEMORY -XX:PermSize=$PERM_MEMORY -XX:MaxPermSize=$PERM_MEMORY -Duser.dir=$SERVER_HOME"
    echo "Java options: ${JAVA_OPTS}"
	echo "Java params: ${parameters}"
    if [ "$JMX_PORT" != '' ]; then
        JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.port=${JMX_PORT}"
		echo "JMX is configured, Port: ${JMX_PORT}"
    fi
    
    echo  "Starting ${MODULE} ... "
    if [ "$LOG_NOHUP" = '' ]; then
        nohup java $JAVA_OPTS -jar ${SERVER_HOME}/dist/${MODULE}.jar ${parameters} > /dev/null 2>&1 &
    else
        nohup java $JAVA_OPTS -jar ${SERVER_HOME}/dist/${MODULE}.jar ${parameters}> ./nohup.out 2>&1 &
    fi
	
    echo "${MODULE} STARTED"
	
	if [ "$LOG_NOHUP" = 'ON' ]; then
		sleep 1
		echo "tailf ./nohup.out"
		tailf ./nohup.out
	fi
    ;;

stop)
    echo "Stopping ${MODULE} ... "
    PROID=`ps -ef|grep "${SERVER_HOME}"|grep -v grep|awk '{print $2}'`
	if [ -n "$PROID" ]; then
  		echo "Kill process id - ${PROID}"
  		kill -9 ${PROID}
  		echo "${MODULE} STOPPED"
	else
  		echo "No process running."
	fi
    ;;
*)
    usage

esac

exit 0
