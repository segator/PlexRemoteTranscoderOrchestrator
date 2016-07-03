/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.segator.plex.cloud.transcoding.scheduled;

import com.myjeeva.digitalocean.common.ActionStatus;
import com.myjeeva.digitalocean.exception.DigitalOceanException;
import com.myjeeva.digitalocean.exception.RequestUnsuccessfulException;
import com.myjeeva.digitalocean.pojo.Action;
import com.myjeeva.digitalocean.pojo.Delete;
import com.myjeeva.digitalocean.pojo.Droplet;
import com.myjeeva.digitalocean.pojo.Image;
import com.myjeeva.digitalocean.pojo.Images;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.regex.Pattern;
import org.segator.plex.cloud.transcoding.MachineStorer;
import org.segator.plex.cloud.transcoding.constants.TranscoderConstants;
import org.segator.plex.cloud.transcoding.entity.ApplicationParameters;
import org.segator.plex.cloud.transcoding.entity.TranscoderMachine;
import org.segator.plex.cloud.transcoding.utils.VersionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author isaac_000
 */
@Service
public class MachineImageBuilder {
    
    @Autowired
    private ApplicationParameters applicationParameters;
    
    @Autowired
    private MachineStorer storerMachine;
    
    private TranscoderMachine machineBuild = null;
    private boolean existImage = false;
    
    @Scheduled(fixedDelay = 5000)
    public synchronized void imageChecking() throws UnsupportedEncodingException, DigitalOceanException, RequestUnsuccessfulException, IOException, RemoteException, InterruptedException {
        if (!existImage) {
            //Exist the required Image?
            Images images = applicationParameters.getDOClient().getAvailableImages(0, 1000);
            Image baseImage = null;
            for (Image image : images.getImages()) {
                if (image.getRegions().contains(applicationParameters.getDORegion())) {
                    if (TranscoderConstants.DROPLET_IMAGE_PLEX_REMOTE_TRANSCODING.equals(image.getName())) {
                        applicationParameters.setDOImageID(image.getId().toString());
                        existImage = true;
                        machineBuild = null;
                        return;
                    } else if (TranscoderConstants.DROPLET_BASE_BUILD_SLUG.equals(image.getSlug())) {
                        baseImage = image;
                    }
                }
            }
            if (machineBuild == null) {
                Droplet buildDroplet = applicationParameters.getBasicDroplet(TranscoderConstants.DROPLET_IMAGE_PLEX_REMOTE_TRANSCODING+"Build");
                buildDroplet.setImage(baseImage);
                File provisioningDigitalOcean = new File(TranscoderConstants.DROPLET_PROVISION_FILE_NAME);
                String provisionTemplate = new String(Files.readAllBytes(Paths.get(provisioningDigitalOcean.getAbsolutePath())), "UTF-8");
                String provisionFile = applicationParameters.parseScriptTemplate(provisionTemplate);
                buildDroplet.setUserData(provisionFile);
                Droplet builderDropletImage = applicationParameters.getDOClient().createDroplet(buildDroplet);
                machineBuild = new TranscoderMachine(builderDropletImage.getId(), builderDropletImage.getName());
                machineBuild.setDroplet(builderDropletImage);
                storerMachine.getTranscoderMachines().add(machineBuild);
            } else if (machineBuild.isReadyToSnapshot()) {                
                //Power Of builder machine
                waitForActionComplete(applicationParameters.getDOClient().powerOffDroplet(machineBuild.getMachineID()));                
                //Take Snapshot
                waitForActionComplete(applicationParameters.getDOClient().takeDropletSnapshot(machineBuild.getMachineID(), TranscoderConstants.DROPLET_IMAGE_PLEX_REMOTE_TRANSCODING));
                //Destroy machine
                Delete delete = applicationParameters.getDOClient().deleteDroplet(machineBuild.getMachineID());
            }
            
        }
    }    
   
    
    private void waitForActionComplete(Action actionTrigger) throws DigitalOceanException, RequestUnsuccessfulException, RemoteException, InterruptedException {
        boolean actionCompleted = false;
        while (!actionCompleted) {
            Thread.sleep(2000);
            Action action = applicationParameters.getDOClient().getActionInfo(actionTrigger.getId());
            if (action.getStatus() == ActionStatus.COMPLETED) {
                actionCompleted=true;
            } else if (action.getStatus() == ActionStatus.ERRORED) {
                throw new RemoteException("Error on do Action ID:" + action.getId());
            }
        }
    }
}
