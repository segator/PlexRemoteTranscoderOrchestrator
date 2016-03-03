#!/bin/bash

#Get Variables
export MACHINE_ID=$(curl -s http://169.254.169.254/metadata/v1/id)
export MAIN_HOST='##mainhost##'
export SMB_HOST='##smbhost##'
export SMB_PORT='##smbport##'
export SMB_USER='##smbuser##'
export SMB_PASS='##smbpass##'
export WEB_PORT='##webport##'

#mount transcode ouput
mount -t cifs -o user=$SMB_USER,pass=$SMB_PASS,port=$SMB_PORT //$SMB_HOST/transcode /transcode
#run machineRefresh Script
screen -S machineNotifier -A -m -d python /usr/bin/machineRefresh.py $MAIN_HOST:$WEB_PORT