/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.segator.plex.cloud.transcoding.entity;

import com.myjeeva.digitalocean.DigitalOcean;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.myjeeva.digitalocean.pojo.Droplet;
import com.myjeeva.digitalocean.pojo.Image;
import com.myjeeva.digitalocean.pojo.Key;
import com.myjeeva.digitalocean.pojo.Region;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 *
 * @author isaac_000
 */
@Service
public class ApplicationParameters implements InitializingBean {

    private String DOToken;
    private String DOImageID;
    private String sshkey;
    private String webServerIP;
    private String transcodeShareDirectoryIP;
    private Integer transcodeShareDirectoryPort;
    private Integer webServerPort;
    private String DORegion;
    private DigitalOcean DOClient;
    private Key sshKeyObj;

    public String getDOToken() {
        return DOToken;
    }

    public void setDOToken(String DOToken) {
        this.DOToken = DOToken;
    }

    public String getDOImageID() {
        return DOImageID;
    }

    public Integer getWebServerPort() {
        return webServerPort;
    }

    public void setWebServerPort(Integer webServerPort) {
        this.webServerPort = webServerPort;
    }

    public void setDOImageID(String DOImageID) {
        this.DOImageID = DOImageID;
    }

    public String getDORegion() {
        return DORegion;
    }

    public String getTranscodeShareDirectoryIP() {
        return transcodeShareDirectoryIP;
    }

    public void setTranscodeShareDirectoryIP(String transcodeShareDirectoryIP) {
        this.transcodeShareDirectoryIP = transcodeShareDirectoryIP;
    }

    public void setDORegion(String DORegion) {
        this.DORegion = DORegion;
    }

    public String getSshkey() {
        return sshkey;
    }

    public Integer getTranscodeShareDirectoryPort() {
        return transcodeShareDirectoryPort;
    }

    public void setTranscodeShareDirectoryPort(Integer transcodeShareDirectoryPort) {
        this.transcodeShareDirectoryPort = transcodeShareDirectoryPort;
    }

    public void setSshkey(String sshkey) {
        this.sshkey = sshkey;
    }

    public String getWebServerIP() {
        return webServerIP;
    }

    public void setWebServerIP(String webServerIP) {
        this.webServerIP = webServerIP;
    }

    public void setToProperties() {
        System.setProperty("application.DOToken", DOToken);
        System.setProperty("application.DORegion", DORegion);
        System.setProperty("application.sshkey", sshkey);
        System.setProperty("application.webServerIP", webServerIP);
        System.setProperty("application.webServerPort", webServerPort.toString());
        System.setProperty("application.transcodeShareDirectoryIP", transcodeShareDirectoryIP);
        System.setProperty("application.transcodeShareDirectoryPort", transcodeShareDirectoryPort.toString());
    }

    public static ApplicationParameters createFromProperties() {
        ApplicationParameters appParams = new ApplicationParameters();
        setFromProperties(appParams);
        return appParams;
    }

    public static void setFromProperties(ApplicationParameters appParams) {
        appParams.setDOToken(System.getProperty("application.DOToken"));
        appParams.setDORegion(System.getProperty("application.DORegion"));
        appParams.setSshkey(System.getProperty("application.sshkey"));
        appParams.setWebServerIP(System.getProperty("application.webServerIP"));
        appParams.setWebServerPort(Integer.valueOf(System.getProperty("application.webServerPort")));
        appParams.setTranscodeShareDirectoryIP(System.getProperty("application.transcodeShareDirectoryIP"));
        appParams.setTranscodeShareDirectoryPort(Integer.valueOf(System.getProperty("application.transcodeShareDirectoryPort")));
    }

    public DigitalOcean getDOClient() {
        return DOClient;
    }

    public void setDOClient(DigitalOcean DOClient) {
        this.DOClient = DOClient;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setFromProperties(this);
        DOClient = new DigitalOceanClient("v2", DOToken);
        String plexUserSshKeyPub = new String(Files.readAllBytes(Paths.get(this.sshkey)));

        //Load SSH key
        for (Key sshKeyObjDO : getDOClient().getAvailableKeys(0).getKeys()) {

            if (sshKeyObjDO.getPublicKey().equals(plexUserSshKeyPub)) {
                this.sshKeyObj = sshKeyObjDO;
            }
        }
        //If no exist we register the key
        if (sshKeyObj == null) {
            sshKeyObj = new Key("PlexMain-" + ((int) (System.currentTimeMillis() / 1000)), plexUserSshKeyPub);
            sshKeyObj = getDOClient().createKey(sshKeyObj);
        }
    }

    public Droplet getBasicDroplet(String name) {
        Droplet newDroplet = new Droplet();
        newDroplet.setName(name);
        newDroplet.setSize("2gb"); // setting size by slug value
        newDroplet.setRegion(new Region(DORegion)); // setting region by slug value; sgp1 => Singapore 1 Data center
        if (DOImageID != null) {
            newDroplet.setImage(new Image(DOImageID)); // setting by Image Id 1601 => centos-5-8-x64 also available in image slug value
        }
        newDroplet.setEnableBackup(Boolean.FALSE);
        newDroplet.setEnableIpv6(Boolean.FALSE);
        newDroplet.setEnablePrivateNetworking(Boolean.FALSE);

        // Adding SSH key info
        List<Key> keys = new ArrayList<Key>();
        keys.add(getSshKey());
        newDroplet.setKeys(keys);
        return newDroplet;
    }

    public Key getSshKey() {
        return sshKeyObj;
    }

    public String parseScriptTemplate(String provisionTemplate) {
        String provisionFile = provisionTemplate.replace("\"", "\\\"");
        provisionFile = provisionFile.replaceAll(Pattern.quote("##mainhost##"), getWebServerIP()).
                replaceAll(Pattern.quote("##smbhost##"), getTranscodeShareDirectoryIP()).
                replaceAll(Pattern.quote("##smbport##"), getTranscodeShareDirectoryPort().toString()).
                replaceAll(Pattern.quote("##webport##"), getWebServerPort().toString());

        return provisionFile;
    }

}
