#!/bin/sh
# 检查参数数量
if [ "$#" -ne 4 ]; then
    echo "Usage: $0 <ip> <port> <java-jar-command> <java-jar-args>"
    exit 1
fi
# 获取参数
IP=$1
PORT=$2
JAVA_JAR_COMMAND=$3
JAVA_JAR_ARGS=$4
# 等待端口变为可用
while ! nc -z -w 5 "$IP" "$PORT"; do
    echo "Waiting for $IP:$PORT to become available..."
    sleep 1
done
# 端口变为可用后，运行 java -jar 命令
echo "Port $IP:$PORT is available. Starting the Java application..."
$JAVA_JAR_COMMAND $JAVA_JAR_ARGS