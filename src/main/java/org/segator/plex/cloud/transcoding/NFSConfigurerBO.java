/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.segator.plex.cloud.transcoding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.segator.plex.cloud.transcoding.entity.ApplicationParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author isaac_000
 */
@Service
public class NFSConfigurerBO {

    @Autowired
    private ApplicationParameters applicationParameters;
    private List<String> ipList = new ArrayList();

    public synchronized void addIP(String ip) throws IOException, InterruptedException {
        if (ip != null && !ipList.contains(ip)) {
            ipList.add(ip);
            refreshExport();
        }
    }

    public synchronized void removeIP(String ip) throws IOException, InterruptedException {
        if (ipList.contains(ip)) {
            ipList.remove(ip);
            refreshExport();
        }
    }

    private void refreshExport() throws IOException, InterruptedException {        
        String exportPermission = "";
        for (String ip : ipList) {
            exportPermission += ip + "(rw,sync,secure,no_subtree_check,root_squash) ";
        }
        String exportFileString = "/transcode " + exportPermission + "\n/usr/lib/plexmediaserver " + exportPermission + "\n"+
                "\"/config/Library/Application Support\" " + exportPermission + "\n" +
                applicationParameters.getMediaDirectory() + " " + exportPermission;
        File exportsFile = new File("/etc/exports");
        if (exportsFile.exists()) {
            exportsFile.delete();
        }
        FileUtils.writeStringToFile(exportsFile, exportFileString, "UTF-8");
        Process process = new ProcessBuilder(
                "exportfs", "-r").start();
        process.waitFor();
    }
}
