# PlexRemoteTranscoderOrchestrator
Application that let us to execute Plex Transcoding in DigitalOcean Droplets.

Inspired in https://github.com/wnielson/Plex-Remote-Transcoder

This application is part of a group of dockers that allow us to setup very easy a Plex Server with Remote Transcoding automatically configured.

This is the Web Server Orchestrator that control what virtual machines is running and provide to Plex Main server the differents machines.

# What we need?
-Plex Server
-Web Orchestrator(this application)
-SMB server(maybe we could change it and use something better)

The Web Orchestrator require have access to media files and need to be visible from Internet.

