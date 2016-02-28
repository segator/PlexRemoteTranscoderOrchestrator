#!/bin/bash

apt-get -y update
apt-get -y  install curl screen apt-transport-https ca-certificates samba-client cifs-utils unzip
#Get Variables
export MACHINE_ID=$(curl -s http://169.254.169.254/metadata/v1/id)
export MAIN_HOST='##mainhost##'
export SMB_HOST='##smbhost##'
export SMB_PORT='##smbport##'
export WEB_PORT='##webport##'
#export GID_PLEX='500'
#export UID_PLEX='500'

wget http://$MAIN_HOST:$WEB_PORT/download/$MACHINE_ID/bWFjaGluZVJlZnJlc2gucHk= -O /usr/bin/machineRefresh.py
chmod 777 /usr/bin/machineRefresh.py

mkdir /transcode
chmod 777 /transcode
mkdir /usr/lib/plexmediaserver
wget http://$MAIN_HOST:$WEB_PORT/download/$MACHINE_ID/cGxleG1lZGlhc2VydmVy -O /tmp/plexServer.zip
unzip /tmp/plexServer.zip -d /usr/lib/plexmediaserver
rm /tmp/plexServer.zip
chmod -R 777 /usr/lib/plexmediaserver

##Notify Server that we finish the build
wget http://$MAIN_HOST:$WEB_PORT/finishTranscoderBuilderProcess/$MACHINE_ID