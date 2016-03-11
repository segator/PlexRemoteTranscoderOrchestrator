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
import org.springframework.stereotype.Service;

/**
 *
 * @author isaac_000
 */
@Service
public class NFSConfigurerBO {

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
        String a = "/exports 192.168.0.0/24(rw,sync,insecure,fsid=0,no_subtree_check,no_root_squash)";
        String exportPermission = "";
        for (String ip : ipList) {
            exportPermission += ip + " (rw,sync,insecure,fsid=0,no_subtree_check,no_root_squash) ";
        }
        String exportFileString = "/transcode " + exportPermission + "\n/usr/lib/plexmediaserver " + exportPermission;
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
