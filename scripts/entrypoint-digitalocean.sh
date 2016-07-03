#!/bin/bash

#Get Variables
export MACHINE_ID=$(curl -s http://169.254.169.254/metadata/v1/id)
export MAIN_HOST='##mainhost##'
export WEB_PORT='##webport##'

mkdir /var/lib/plexmediaserver
addgroup --gid 911 abc
adduser --disabled-password --gecos "" --uid 911 --gid 911 abc --home /var/lib/plexmediaserver --no-create-home
mkdir /var/lib/plexmediaserver/.ssh
cp /root/.ssh/authorized_keys /var/lib/plexmediaserver/.ssh/
chown -R abc:abc /var/lib/plexmediaserver
chown -R abc:abc /var/lib/plexmediaserver/.ssh

service sshd start

#mount transcode ouput
mount -t nfs -o proto=tcp,port=2049 $MAIN_HOST:/transcode /transcode
chmod 777 /transcode

#mount plexbinaries
mount -t nfs -o proto=tcp,port=2049 $MAIN_HOST:/usr/lib/plexmediaserver /usr/lib/plexmediaserver
chmod +x /usr/lib/plexmediaserver/Resources/plex_transcoder

#Mount Application Support
mount -t nfs -o proto=tcp,port=2049 $MAIN_HOST:/config/Library/Application\ Support /config/Library/Application\ Support



#run plex data redirector
screen -S plexRedirector -A -m -d socat TCP-LISTEN:32400,fork TCP:$MAIN_HOST:$WEB_PORT

#run machineRefresh Script
screen -S machineNotifier -A -m -d python /usr/bin/machineRefresh.py $MAIN_HOST:$WEB_PORT