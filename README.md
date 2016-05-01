# PlexRemoteTranscoderOrchestrator
Application that let us to execute Plex Transcoding in DigitalOcean Droplets.

Inspired by [wnielson/Plex-Remote-Transcoder](https://github.com/wnielson/Plex-Remote-Transcoder)

This application is part of a group of dockers that allow us to setup very easy a Plex Server with Remote Transcoding automatically configured.

This is the Web Server Orchestrator that control what virtual machines is running and provide to Plex Main server the differents machines.

## What we need?
- Plex Server
- Web Orchestrator(this application)
- SMB server(maybe we could change it and use something better)

The Web Orchestrator require have access to media files and need to be visible from Internet.

If you don't want to compile you don't need because I will create a docker with all preinstalled.

## Compile
The project use Maven

First we need java to compile & run
```
echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee /etc/apt/sources.list.d/webupd8team-java.list
echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list
apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886
apt-get update
apt-get install oracle-java8-installer
```

1. Install tools: `apt-get install maven git`
2. Download Project: `git clone https://github.com/segator/PlexRemoteTranscoderOrchestrator.git`
3. Enter directory: `cd PlexRemoteTranscoderOrchestrator`
4. Compile Project: `mvn clean install`

The Jar file will be inside target directory

## Execute Arguments

`java -jar PlexCloudTranscoding-1.0-SNAPSHOT.jar arguments`


| Argument   | Example Value | Description
| ------------- | ------------- | ------------ |
| digitalOceanToken | DOToken | Do Token needed to create Droplets |
| digitalOceanRegion | am2 | Region where we deploy Droplets |
| sshkey | /root/.ssh/id_rsa.pub | Public key we install to the deployed Droplet |
| webServerDomainName | orchestrator.plexserver.com  | Domain/ip (with access from internet) where we deploy this application |
| webServerPort | 8800 | port of the webServerDomainName (with access from internet) |
| transcodeFileServerDomainName | drop.transcode.plexserver.com | Domain/Ip(with access from internet) where DO will write data |
| transcodeFileServerPort | 445 | Port of the transcodeFileServerPort (with access from internet) |




## TODO
- [x] Remote Transcoding Working
- [ ] Do the application more parametrizable
- [x] Prepare Plex Wrapper Transcoder for Docker use
- [x] Create Docker
- [x] Extend Plex Docker with Plex Transcoder Wrapper
- [x] Create Docker Compose to do an automatic Plex Remote Transcoding with few parameters
- [ ] Refactor Application to allow more microserver providers(Amazon,Google Cloud)


