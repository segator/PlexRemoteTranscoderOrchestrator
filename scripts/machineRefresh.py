#!/usr/bin/env python
import requests
import sys, getopt
import time
import os, subprocess
import time
machineID = os.environ['MACHINE_ID']
lastStatus=''
lastUpdate=time.time()
def start(notifyHost):
    global lastStatus
    global lastUpdate
    while True:
          now = time.time()
          dif = int(now-lastUpdate)
          transcoding = subprocess.check_output('ps ax | grep transcode | grep -v grep | awk \'{print $1}\'',shell=True)
          if transcoding:
             if (lastStatus == 'transcoding') & (dif > 60):
                print "machine %s keep alive transcoding:%s" % (machineID,getRequest(notifyHost,machineID,'transcoding'))
                lastUpdate=now
             elif lastStatus != 'transcoding':
                print "machine %s changed status to %s:%s" %  (machineID,'transcoding',getRequest(notifyHost,machineID,'transcoding'))
                lastStatus = 'transcoding'
                lastUpdate=now
          elif lastStatus != 'idle':
              print "machine %s changed status to %s:%s" % (machineID,'idle',getRequest(notifyHost,machineID,'idle'))
              lastStatus = 'idle'
              lastUpdate=now
          time.sleep(1)


def getRequest(notifyHost,machineID,status):
    response = requests.get('http://'+notifyHost+'/setTranscoderStatus/'+machineID+'/'+status)
    return response.status_code





def main(argv):
    start(argv[0])

if __name__ == "__main__":
   main(sys.argv[1:])
