#！/bin/bash
# 需要yum环境 一键安装docker-ce和docker-compose,本脚本适用于CentOS7和8

function os7_install_docker () {

rpm -qa | grep docker &> /etc/null
 if [ $? -ne 0 ]
 then
    echo "开始安装docker..."
    # step 1: 安装必要的一些系统工具
    yum install -y yum-utils device-mapper-persistent-data lvm2
    # Step 2: 添加软件源信息
    filename='/etc/yum.repos.d/docker-ce.repo'
    if [ ! -f ${filename} ]
    then
        echo "开始配置${filename} 文件"
        yum-config-manager --add-repo https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
        yum makecache fast
    else
        echo "${filename} 已存在"
    fi
    # Step 3: 安装Docker-CE   docker-compose
    yum -y install docker-ce  docker-compose
    # Step 4: 开启Docker服务
    systemctl enable docker
    systemctl start docker

    echo -e "docker installed  [\033[32m OK \033[0m]"
    sleep 3

 else
    echo "docker 已存在"
 fi


}

#------------------------centos8安装docker------------------------
function os8_install_docker () {

rpm -qa | grep docker &> /etc/null
 if [ $? -ne 0 ]
 then
    echo "开始安装docker..."
    # step 1: 安装必要的一些系统工具
    yum install -y yum-utils device-mapper-persistent-data lvm2
    # Step 2: 添加软件源信息
    filename='/etc/yum.repos.d/docker-ce.repo'
    if [ ! -f ${filename} ]
    then
        echo "开始配置${filename} 文件"
        yum-config-manager --add-repo https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
        yum makecache fast
    else
        echo "${filename} 已存在"
    fi
    # Step 3: 安装Docker-CE
    yum --allowerasing -y install docker-ce
	# Step 4: 安装Docker-Compose服务
	curl -L https://github.com/docker/compose/releases/download/v2.23.0/docker-compose-linux-x86_64 -o /usr/local/bin/docker-compose
    # Step 5: 开启Docker服务
    systemctl enable docker
    systemctl start docker

    echo -e "docker installed  [\033[32m OK \033[0m]"
    sleep 3

 else
    echo "docker 已存在"
 fi


}

#------------------------配置镜像加速服务------------------------
function setup_fast_mirros(){
echo "正在配置镜像加速服务....."
filename='/etc/docker/daemon.json'

if [ ! -f ${filename} ]
then

cat > ${filename} <<EOF
{
  "registry-mirrors": [
    "https://docker.mirrors.ustc.edu.cn",
    "http://hub-mirror.c.163.com",
    "https://registry.docker-cn.com"
    ]
}
EOF
sleep 3

if [ -f ${filename} ]
then
    echo -e "文件配置成功  [\033[32m OK \033[0m]"
    systemctl daemon-reload
    systemctl restart docker
    docker info
    which docker
    sleep 3
else
    echo -e "文件配置失败[\033[31m FAILED  \033[0m]"
    exit
fi

else
    echo "${filename} 文件已存在，未做修改"
fi

}


#------------------------拉取常用镜像------------------------
function docker_pull_images(){

# 系统镜像
os_images=("centos:7.9.2009")
# web服务镜像
web_images=("nginx" "tomcat" "httpd" )
# 数据库镜像
database_images=("mysql:5.7.32" "mysql:8")
# 开发工具镜像
develop_images=("python" "busybox" )
# docker镜像
docker_image=("docker")

# 拉取镜像时只需调用对应的变量
download_images=(
# "${os_images[*]}"
"${web_images[*]}"
"${database_images[*]}"
# "${develop_images[*]}"
# "${docker_image[*]}"
)

rpm -qa | grep docker-ce  &> /etc/null
if [ $? -eq 0 ]
then
    echo "拉取镜像开始..."
    sleep 1
    for image in ${download_images[*]}
    do
        echo "正在拉取镜像---${image}---"
        image_name=`echo ${image} | awk -F: '{print $1}'`
        docker images ${image} | grep -o ${image_name}  &> /etc/null

        if [ $? -ne 0 ]
        then
            docker pull ${image}
        else
            echo -e "${image}镜像已存在 [\033[32m OK \033[0m]"
        fi
        sleep 1
    done

    echo -e "镜像拉取完毕  [\033[32m OK \033[0m] 详情如下："
    docker images

else
    echo -e "docker未安装，请安装docker后再拉取镜像    [\033[31m ERROR \033[0m]"
    exit
fi

}

#------------------------开启功能------------------------
os=`cat /etc/redhat-release | grep -o '[0-9]'|sed -n '1p'`
if [ $os == 7 ];then
	os7_install_docker
	setup_fast_mirros
elif [ $os == 8 ];then
    os8_install_docker
	setup_fast_mirros
else
    echo "此脚本不支持该系统安装！"
    exit
fi


# 1 安装docker
#install_docker

# 2 配置加速服务
#0setup_fast_mirros

# 3 拉取常用镜像
# docker_pull_images