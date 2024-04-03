#!/bin/bash
# 定义应用组名
group_name='java'
# 定义应用名称
app_name='alipay-demo'
echo ${app_name}
echo '--------copy app_name ----'
docker stop ${app_name}
docker rm ${app_name}
docker rmi ${group_name}/${app_name}
echo '----rmi ${group_name}/${app_name}----'
# 打包编译docker镜像
docker build -t ${group_name}/${app_name} .

#构建docker应用
docker run -p 30001:30001 --name ${app_name} \
-e TZ="Asia/Shanghai" \
-d ${group_name}/${app_name}
