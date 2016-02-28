/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.segator.plex.cloud.transcoding.entity;

/**
 *
 * @author isaac_000
 */
public class PlexTranscoderRequest {
    private PlexUserSession session;

    public PlexUserSession getSession() {
        return session;
    }

    public void setSession(PlexUserSession session) {
        this.session = session;
    }
    
    
}
