package org.segator.plex.cloud.transcoding.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 *
 * @author isaac_000
 */
 public class PlexUserSession {

    @JsonProperty("Accept-Language")
    private String acceptLanguage;
    private String subtitles;
    private Integer includeCodecs;
    @JsonProperty("X-Plex-Chunked")
    private Integer plexChunked;
    @JsonProperty("X-Plex-Account")
    private String plexAccount;
    @JsonProperty("X-Plex-Username")
    private String plexUsername;
    @JsonProperty("X-Plex-Device-Name")
    private String plexDeviceName;
    @JsonProperty("X-Plex-Platform-Version")
    private String plexPlatformVersion;
    @JsonProperty("X-Plex-Session-Identifier")
    private String plexSessionIdentifier;
    @JsonProperty("X-Plex-Device-Screen-Resolution")
    private String plexDeviceScreenResolution;
    @JsonProperty("X-Plex-Client-Profile-Extra")
    private String plexClientProfileExtra;
    @JsonProperty("session")
    private String session;
    @JsonProperty("X-Plex-Product")
    private String plexProduct;
    private Integer mediaIndex;
    private Double maxVideoBitrate;
    private Double videoQuality;
    private String videoResolution;
    private Integer fastSeek;
    private Integer directPlay;
    @JsonProperty("X-Plex-Device")
    private String plexDevice;
    private String protocol;
    private Integer copyts;
    private Integer partIndex;
    private Integer audioBoost;
    private String path;
    @JsonProperty("X-Plex-Client-Identifier")
    private String plexClientIdentifier;
    private Double offset;
    @JsonProperty("X-Plex-Platform")
    private String plexPlatform;
    @JsonProperty("X-Plex-Version")
    private String plexVersion;
    @JsonProperty("X-Plex-Token")
    private String plexToken;
    private Integer directStream;
    private Integer subtitleSize;

    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    public void setAcceptLanguage(String acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
    }

    public String getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(String subtitles) {
        this.subtitles = subtitles;
    }

    public Integer getPlexChunked() {
        return plexChunked;
    }

    public void setPlexChunked(Integer plexChunked) {
        this.plexChunked = plexChunked;
    }

    public String getPlexClientProfileExtra() {
        return plexClientProfileExtra;
    }

    public void setPlexClientProfileExtra(String plexClientProfileExtra) {
        this.plexClientProfileExtra = plexClientProfileExtra;
    }

    public String getPlexDeviceName() {
        return plexDeviceName;
    }

    public void setPlexDeviceName(String plexDeviceName) {
        this.plexDeviceName = plexDeviceName;
    }

    public String getPlexPlatformVersion() {
        return plexPlatformVersion;
    }

    public void setPlexPlatformVersion(String plexPlatformVersion) {
        this.plexPlatformVersion = plexPlatformVersion;
    }

    public String getPlexSessionIdentifier() {
        return plexSessionIdentifier;
    }

    public void setPlexSessionIdentifier(String plexSessionIdentifier) {
        this.plexSessionIdentifier = plexSessionIdentifier;
    }

    public String getPlexDeviceScreenResolution() {
        return plexDeviceScreenResolution;
    }

    public void setPlexDeviceScreenResolution(String plexDeviceScreenResolution) {
        this.plexDeviceScreenResolution = plexDeviceScreenResolution;
    }
    
    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getPlexProduct() {
        return plexProduct;
    }

    public void setPlexProduct(String plexProduct) {
        this.plexProduct = plexProduct;
    }

    public Double getMaxVideoBitrate() {
        return maxVideoBitrate;
    }

    public void setMaxVideoBitrate(Double maxVideoBitrate) {
        this.maxVideoBitrate = maxVideoBitrate;
    }

    public Integer getIncludeCodecs() {
        return includeCodecs;
    }

    public void setIncludeCodecs(Integer includeCodecs) {
        this.includeCodecs = includeCodecs;
    }

    public String getPlexAccount() {
        return plexAccount;
    }

    public void setPlexAccount(String plexAccount) {
        this.plexAccount = plexAccount;
    }

    public String getPlexUsername() {
        return plexUsername;
    }

    public void setPlexUsername(String plexUsername) {
        this.plexUsername = plexUsername;
    }

    public Double getVideoQuality() {
        return videoQuality;
    }

    public void setVideoQuality(Double videoQuality) {
        this.videoQuality = videoQuality;
    }

    public String getVideoResolution() {
        return videoResolution;
    }

    public void setVideoResolution(String videoResolution) {
        this.videoResolution = videoResolution;
    }

    public Integer getMediaIndex() {
        return mediaIndex;
    }

    public void setMediaIndex(Integer mediaIndex) {
        this.mediaIndex = mediaIndex;
    }

    public Integer getFastSeek() {
        return fastSeek;
    }

    public void setFastSeek(Integer fastSeek) {
        this.fastSeek = fastSeek;
    }

    public Integer getDirectPlay() {
        return directPlay;
    }

    public void setDirectPlay(Integer directPlay) {
        this.directPlay = directPlay;
    }

    public String getPlexDevice() {
        return plexDevice;
    }

    public void setPlexDevice(String plexDevice) {
        this.plexDevice = plexDevice;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Integer getCopyts() {
        return copyts;
    }

    public void setCopyts(Integer copyts) {
        this.copyts = copyts;
    }

    public Integer getPartIndex() {
        return partIndex;
    }

    public void setPartIndex(Integer partIndex) {
        this.partIndex = partIndex;
    }

    public Integer getAudioBoost() {
        return audioBoost;
    }

    public void setAudioBoost(Integer audioBoost) {
        this.audioBoost = audioBoost;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPlexClientIdentifier() {
        return plexClientIdentifier;
    }

    public void setPlexClientIdentifier(String plexClientIdentifier) {
        this.plexClientIdentifier = plexClientIdentifier;
    }

    public Double getOffset() {
        return offset;
    }

    public void setOffset(Double offset) {
        this.offset = offset;
    }

    public String getPlexPlatform() {
        return plexPlatform;
    }

    public void setPlexPlatform(String plexPlatform) {
        this.plexPlatform = plexPlatform;
    }

    public String getPlexVersion() {
        return plexVersion;
    }

    public void setPlexVersion(String plexVersion) {
        this.plexVersion = plexVersion;
    }

    public String getPlexToken() {
        return plexToken;
    }

    public void setPlexToken(String plexToken) {
        this.plexToken = plexToken;
    }

    public Integer getDirectStream() {
        return directStream;
    }

    public void setDirectStream(Integer directStream) {
        this.directStream = directStream;
    }

    public Integer getSubtitleSize() {
        return subtitleSize;
    }

    public void setSubtitleSize(Integer subtitleSize) {
        this.subtitleSize = subtitleSize;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.plexToken);
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
        final PlexUserSession other = (PlexUserSession) obj;
        if (!Objects.equals(this.plexToken, other.plexToken)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PlexUserSession{" + "acceptLanguage=" + acceptLanguage + ", subtitles=" + subtitles + ", plexChunked=" + plexChunked + ", plexDeviceName=" + plexDeviceName + ", plexPlatformVersion=" + plexPlatformVersion + ", session=" + session + ", plexProduct=" + plexProduct + ", mediaIndex=" + mediaIndex + ", fastSeek=" + fastSeek + ", directPlay=" + directPlay + ", plexDevice=" + plexDevice + ", protocol=" + protocol + ", copyts=" + copyts + ", partIndex=" + partIndex + ", audioBoost=" + audioBoost + ", path=" + path + ", plexClientIdentifier=" + plexClientIdentifier + ", offset=" + offset + ", plexPlatform=" + plexPlatform + ", plexVersion=" + plexVersion + ", plexToken=" + plexToken + ", directStream=" + directStream + ", subtitleSize=" + subtitleSize + '}';
    }

}
