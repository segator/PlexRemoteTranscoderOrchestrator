/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.segator.plex.cloud.transcoding;

import org.segator.plex.cloud.transcoding.constants.TranscoderConstants;
import com.myjeeva.digitalocean.pojo.Droplet;
import com.myjeeva.digitalocean.pojo.Droplets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.segator.plex.cloud.transcoding.entity.ApplicationParameters;
import org.segator.plex.cloud.transcoding.entity.TranscoderMachine;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author isaac_000
 */
@Service
public class MachineStorer implements InitializingBean {

    @Autowired
    private ApplicationParameters applicationParams;



    private List<TranscoderMachine> transcoderMachines;

    public synchronized List<TranscoderMachine> getTranscoderMachines() {
        return transcoderMachines;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        transcoderMachines = new ArrayList();
        //See in digitalOcean if exist machines
        Droplets droplets = applicationParams.getDOClient().getAvailableDroplets(1, 200);
        for (Droplet droplet : droplets.getDroplets()) {
            if (droplet.isActive() && droplet.getName().startsWith(TranscoderConstants.DROPLET_BASE_NAME)) {
                TranscoderMachine transcoderMachine = new TranscoderMachine(droplet.getId(), droplet.getName());
                transcoderMachine.setDroplet(droplet);
                transcoderMachines.add(transcoderMachine);
            }
        }

    }

    public synchronized void removeMachine(TranscoderMachine transcoderMachine) throws IOException, InterruptedException {
        transcoderMachines.remove(transcoderMachine);
    }

}
