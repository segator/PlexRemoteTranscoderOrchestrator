package org.segator.plex.cloud.transcoding;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.segator.plex.cloud.transcoding.entity.ApplicationParameters;

public class Main {

    public static void main(String... anArgs) throws Exception {

        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(getOptions(), anArgs);
        ApplicationParameters appParams = new ApplicationParameters();
        if (cmd.hasOption("digitalOceanToken")) {
            appParams.setDOToken(cmd.getOptionValue("digitalOceanToken"));
        }

        if (cmd.hasOption("digitalOceanRegion")) {
            appParams.setDORegion(cmd.getOptionValue("digitalOceanRegion"));
        }
        if (cmd.hasOption("sshkey")) {
            appParams.setSshkey(cmd.getOptionValue("sshkey"));
        }
        if (cmd.hasOption("webServerDomainName")) {
            appParams.setWebServerIP(cmd.getOptionValue("webServerDomainName"));
        }
        if (cmd.hasOption("webServerPort")) {
            appParams.setWebServerPort(Integer.valueOf(cmd.getOptionValue("webServerPort")));
        }else{
             appParams.setWebServerPort(80);
        }
        if (cmd.hasOption("transcodeFileServerDomainName")) {
            appParams.setTranscodeShareDirectoryIP(cmd.getOptionValue("transcodeFileServerDomainName"));
        }else{
            appParams.setTranscodeShareDirectoryIP(appParams.getWebServerIP());
        }
        if (cmd.hasOption("transcodeFileServerPort")) {
            appParams.setTranscodeShareDirectoryPort(Integer.valueOf(cmd.getOptionValue("transcodeFileServerPort")));
        }else{
             appParams.setTranscodeShareDirectoryPort(445);
        }
        appParams.setToProperties();

        new Main().start();
    }

    private static Options getOptions() {
        // create Options object
        Options options = new Options();

        Option opt = new Option("digitalOceanToken", true, "DigitalOcean Token");
        opt.setRequired(true);
        options.addOption(opt);
        opt = new Option("digitalOceanRegion", true, "digitalOcean region where to deploy");
        opt.setRequired(true);
        options.addOption(opt);
        opt = new Option("sshkey", true, "ssh id assigned to digitalOcean keys sections");
        opt.setRequired(true);
        options.addOption(opt);
        opt = new Option("webServerDomainName", true, "Web Server Domain Name or public ip");
        opt.setRequired(true);
        options.addOption(opt);
        options.addOption(new Option("webServerPort", true, "Web Server public port"));        
        options.addOption(new Option("transcodeFileServerDomainName", true, "(SMB) File Server Domain Name or public ip"));
        
        options.addOption(new Option("transcodeFileServerPort", true, "(SMB) File Server public port"));
        return options;
    }
    private WebServer server;

    public Main() {
        server = new WebServer(8800);
    }

    public void start() throws Exception {
        server.start();
        server.join();
    }
}
