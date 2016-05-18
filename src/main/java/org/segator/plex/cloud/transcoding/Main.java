package org.segator.plex.cloud.transcoding;

import java.util.Arrays;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.segator.plex.cloud.transcoding.constants.TranscoderConstants;
import org.segator.plex.cloud.transcoding.entity.ApplicationParameters;

public class Main {

    public static void main(String... anArgs) throws Exception {
        if (Arrays.asList(anArgs).contains("--help")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar PlexCloudTranscoding <arguments>", getOptions());
            return;
        }

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
        } else {
            appParams.setWebServerPort(80);
        }
        if (cmd.hasOption("mediaDirectory")) {
            appParams.setMediaDirectory(cmd.getOptionValue("mediaDirectory"));
        }
        if (cmd.hasOption("VMTimeout")) {
            appParams.setVMTimeout(Integer.valueOf(cmd.getOptionValue("VMTimeout")));
        } else {
            appParams.setVMTimeout(TranscoderConstants.UNUSED_TIMEOUT_MACHINE);
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
        opt = new Option("mediaDirectory", true, "where plex get media files");
        opt.setRequired(true);
        options.addOption(opt);
        options.addOption(new Option("webServerPort", true, "Web Server public port"));
        options.addOption(new Option("VMTimeout", true, "When the Transcoder VM reach the unused time will be destroy"));
        options.addOption(new Option("h", "help", false, "Help information"));
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
