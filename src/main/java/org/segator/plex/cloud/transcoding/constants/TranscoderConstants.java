/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.segator.plex.cloud.transcoding.constants;

/**
 *
 * @author isaac_000
 */
public class TranscoderConstants {

    public static final String DROPLET_BASE_NAME = "Transcoder-";
    public static final int UNUSED_TIMEOUT_MACHINE = 10*60;
    public static final String DROPLET_IMAGE_PLEX_REMOTE_TRANSCODING = "PlexRemoteTranscoder";
    public static final String DROPLET_BASE_BUILD_SLUG = "debian-8-x64";
    public static final String DROPLET_PROVISION_FILE_NAME = "/etc/transcoder_orchestrator/provide-digitalocean.sh";
    public static final String DROPLET_ENTRY_POINT_FILE_NAME = "/etc/transcoder_orchestrator/entrypoint-digitalocean.sh";
}
