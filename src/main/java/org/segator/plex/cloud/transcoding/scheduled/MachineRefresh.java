/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.segator.plex.cloud.transcoding.scheduled;

import com.myjeeva.digitalocean.pojo.Delete;
import java.util.ArrayList;
import java.util.Date;
import org.segator.plex.cloud.transcoding.MachineStorer;
import org.segator.plex.cloud.transcoding.constants.TranscoderConstants;
import org.segator.plex.cloud.transcoding.entity.ApplicationParameters;
import org.segator.plex.cloud.transcoding.entity.TranscoderMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author isaac_000
 */
@Service
public class MachineRefresh {

    @Autowired
    private MachineStorer storerMachine;
    @Autowired
    private ApplicationParameters applicationParameters;

    @Scheduled(fixedDelay = 5000)
    public void checkMachines() {
        try {
            for (TranscoderMachine transcoderMachine : new ArrayList<TranscoderMachine>(storerMachine.getTranscoderMachines())) {
                long now = new Date().getTime();
                //if the droplet is new we need to refresh to get new data(like ipaddres etc..)
                if (transcoderMachine.getMachineStatus().equals("new")) {
                    transcoderMachine.setDroplet(applicationParameters.getDOClient().getDropletInfo(transcoderMachine.getMachineID()));
                }

                //If the machine is unused during X time we destroy the machine(Only if is not a builder VM)
                if (!transcoderMachine.isBuilder()) {
                    long diference = now - transcoderMachine.getLastUsage();
                    if (diference > TranscoderConstants.UNUSED_TIMEOUT_MACHINE) {
                        Delete delete = applicationParameters.getDOClient().deleteDroplet(transcoderMachine.getMachineID());
                        if (delete.getIsRequestSuccess()) {
                            storerMachine.removeMachine(transcoderMachine);
                            System.out.println("Delete Transcoding Machine:" + transcoderMachine.getMachineID());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
