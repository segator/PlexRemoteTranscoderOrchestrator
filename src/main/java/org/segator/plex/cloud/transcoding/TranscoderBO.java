/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.segator.plex.cloud.transcoding;

import com.myjeeva.digitalocean.exception.DigitalOceanException;
import com.myjeeva.digitalocean.exception.RequestUnsuccessfulException;
import com.myjeeva.digitalocean.pojo.Droplet;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import org.segator.plex.cloud.transcoding.constants.TranscoderConstants;
import org.segator.plex.cloud.transcoding.entity.ApplicationParameters;
import org.segator.plex.cloud.transcoding.entity.PlexUserSession;
import org.segator.plex.cloud.transcoding.entity.TranscoderMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author isaac_000
 */
@Service
public class TranscoderBO {

    @Autowired
    private MachineStorer storerMachine;

    @Autowired
    private ApplicationParameters applicationParams;

    public synchronized TranscoderMachine getFreeTranscoderMachine(PlexUserSession plexUserSession) throws DigitalOceanException, RequestUnsuccessfulException, InterruptedException, UnsupportedEncodingException, IOException {
        Thread.sleep(3000);
        long now = new Date().getTime();
        for (TranscoderMachine transcoderMachine : new ArrayList<TranscoderMachine>(storerMachine.getTranscoderMachines())) {
            //If the user have same session as the VM seems that he are seeking to the same movie so he have preferency to take this VM
            if (plexUserSession.getSession().equals(transcoderMachine.getPlexUserSession().getSession())) {
                return transcoderMachine;
            }
            
            //In case the VM is in idle state it seems nobody is using it so we assign to the user.
            if (transcoderMachine.getStatusTranscoding().equals("idle")) {
                transcoderMachine.setStatusTranscoding("assigned");
                transcoderMachine.setPlexUserSession(plexUserSession);
                transcoderMachine.setLastUsage(now);
                return transcoderMachine;
            }

            //In case the VM was assigned but not used in 2min we automatically assign to the new session
            long diference = new Date().getTime() - transcoderMachine.getLastUsage();
            if (transcoderMachine.getStatusTranscoding().equals("assigned") && diference > 120 * 1000) {
                System.out.println("The VM" + transcoderMachine.getName() + " was assigned but not used, reasigning to " + plexUserSession.getSession());
                transcoderMachine.setPlexUserSession(plexUserSession);
                transcoderMachine.setLastUsage(now);
                return transcoderMachine;
            }
        }
        //No Empty Machines let's create new one, but only if exist an image
        if (applicationParams.getDOImageID() == null) {
            return null;
        }
        String VMName = TranscoderConstants.DROPLET_BASE_NAME + System.currentTimeMillis();
        System.out.println("We are going to create a droplet " + VMName);
        Droplet newMachine = applicationParams.getBasicDroplet(VMName);
        File entryPointDigitalOcean = new File(TranscoderConstants.DROPLET_ENTRY_POINT_FILE_NAME);
        String provisionTemplate = new String(Files.readAllBytes(Paths.get(entryPointDigitalOcean.getAbsolutePath())), "UTF-8");
        String provisionFile = applicationParams.parseScriptTemplate(provisionTemplate);
        provisionFile = provisionFile.replace(":", "\\:");
        newMachine.setUserData(provisionFile);
        newMachine = applicationParams.getDOClient().createDroplet(newMachine);

        TranscoderMachine transcoderMachine = new TranscoderMachine(newMachine.getId(), newMachine.getName());
        transcoderMachine.setDroplet(newMachine);
        transcoderMachine.setPlexUserSession(plexUserSession);
        storerMachine.getTranscoderMachines().add(transcoderMachine);
        return transcoderMachine;
    }

    public TranscoderMachine getTranscoderMachine(Integer id) {
        for (TranscoderMachine transcoderMachine : new ArrayList<TranscoderMachine>(storerMachine.getTranscoderMachines())) {
            if (transcoderMachine.getMachineID().equals(id)) {
                return transcoderMachine;
            }
        }
        return null;
    }
}
