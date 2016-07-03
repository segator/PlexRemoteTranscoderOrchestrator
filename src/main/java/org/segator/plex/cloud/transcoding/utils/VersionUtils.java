/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.segator.plex.cloud.transcoding.utils;

import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author isaac_000
 */
public class VersionUtils {
    public  synchronized String getVersion() {
    String version = null;

    // try to load from maven properties first
    try {
        Properties p = new Properties();
        InputStream is = this.getClass().getResourceAsStream("/META-INF/maven/org.segator/PlexCloudTranscoding/pom.properties");
        if (is != null) {
            p.load(is);
            version = p.getProperty("version", "");
        }
    } catch (Exception e) {
        // ignore
    }

    // fallback to using Java API
    if (version == null) {
        Package aPackage = VersionUtils.class.getPackage();
        if (aPackage != null) {
            version = aPackage.getImplementationVersion();
            if (version == null) {
                version = aPackage.getSpecificationVersion();
            }
        }
    }

    if (version == null) {
        // we could not compute the version so use a blank
        version = "";
    }
    version= version.replace(".", "v");

    return version;
} 
}
