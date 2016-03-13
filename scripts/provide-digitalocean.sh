#!/bin/bash

apt-get -y update
apt-get -y  install curl screen apt-transport-https ca-certificates socat nfs-common
#apt-get -y install unzip
#Get Variables
export MACHINE_ID=$(curl -s http://169.254.169.254/metadata/v1/id)
export MAIN_HOST='##mainhost##'
export WEB_PORT='##webport##'
#export GID_PLEX='500'
#export UID_PLEX='500'

wget http://$MAIN_HOST:$WEB_PORT/download/$MACHINE_ID/L2V0Yy90cmFuc2NvZGVyX29yY2hlc3RyYXRvci9tYWNoaW5lUmVmcmVzaC5weQ==?absolute=1 -O /usr/bin/machineRefresh.py
chmod 777 /usr/bin/machineRefresh.py

mkdir /transcode
chmod 777 /transcode
mkdir /usr/lib/plexmediaserver
#wget http://$MAIN_HOST:$WEB_PORT/download/$MACHINE_ID/L3Vzci9saWIvcGxleG1lZGlhc2VydmVy?absolute=1 -O /tmp/plexServer.zip
#unzip /tmp/plexServer.zip -d /usr/lib/plexmediaserver
#rm /tmp/plexServer.zip
#chmod -R 777 /usr/lib/plexmediaserver

#remove ssh start on boot
update-rc.d -f ssh remove
##Notify Server that we finish the build
wget http://$MAIN_HOST:$WEB_PORT/finishTranscoderBuilderProcess/$MACHINE_ID