package org.segator.plex.cloud.transcoding.entity;

import com.myjeeva.digitalocean.pojo.Droplet;
import com.myjeeva.digitalocean.pojo.Network;
import java.util.Date;

/**
 *
 * @author isaac_000
 */
public class TranscoderMachine extends IJSON {

    private final Integer machineID;
    private final String name;
    private String ip;
    private String statusTranscoding = "pending";
    private String machineStatus;
    private long lastUsage;
    private Droplet droplet;
    private boolean builder = false;
    private boolean readyToSnapshot = false;
    private PlexUserSession plexUserSession;

    public Droplet getDroplet() {
        return droplet;
    }

    public TranscoderMachine(Integer machineID, String name) {
        this.machineID = machineID;
        this.name = name;
        this.setResult("OK");
        this.lastUsage = new Date().getTime();
        plexUserSession = new PlexUserSession();
    }

    public PlexUserSession getPlexUserSession() {
        return plexUserSession;
    }

    public void setPlexUserSession(PlexUserSession plexUserSession) {
        this.plexUserSession = plexUserSession;
    }

    public TranscoderMachine(Integer machineID, String name, boolean builder) {
        this(machineID, name);
        this.builder = builder;
    }

    public boolean isBuilder() {
        return builder;
    }

    public boolean isReadyToSnapshot() {
        return readyToSnapshot;
    }

    public void setReadyToSnapshot(boolean readyToSnapshot) {
        this.readyToSnapshot = readyToSnapshot;
    }

    public void setDroplet(Droplet droplet) {
        this.droplet = droplet;
        if (droplet.getNetworks() != null && droplet.getNetworks().getVersion4Networks() != null) {
            for (Network net : droplet.getNetworks().getVersion4Networks()) {
                ip = net.getIpAddress();
            }
        }
        machineStatus = droplet.getStatus().toString();

    }

    public String getName() {
        return name;
    }

    public void setLastUsage(long lastUsage) {
        this.lastUsage = lastUsage;
    }

    public long getLastUsage() {
        return lastUsage;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getMachineID() {
        return machineID;
    }

    public String getStatusTranscoding() {
        return statusTranscoding;
    }

    public void setStatusTranscoding(String statusTranscoding) {
        this.statusTranscoding = statusTranscoding;
    }

    public String getMachineStatus() {
        return machineStatus;
    }

    public void setMachineStatus(String machineStatus) {
        this.machineStatus = machineStatus;
    }

    public boolean isActive() {
        return "active".equals(machineStatus);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.machineID != null ? this.machineID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TranscoderMachine other = (TranscoderMachine) obj;
        if (this.machineID != other.machineID && (this.machineID == null || !this.machineID.equals(other.machineID))) {
            return false;
        }
        return true;
    }

}
