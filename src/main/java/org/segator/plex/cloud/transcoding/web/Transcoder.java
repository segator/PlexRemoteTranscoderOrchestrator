package org.segator.plex.cloud.transcoding.web;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.segator.plex.cloud.transcoding.TranscoderBO;
import org.segator.plex.cloud.transcoding.entity.ExceptionError;
import org.segator.plex.cloud.transcoding.entity.IJSON;
import org.segator.plex.cloud.transcoding.entity.PlexUserSession;
import org.segator.plex.cloud.transcoding.entity.TranscoderMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class Transcoder {

    @Autowired
    private TranscoderBO transcoderBO;

    @RequestMapping(value = "/getTranscoder", produces = MediaType.APPLICATION_JSON_VALUE, consumes = "application/json",
            headers = "Accept=application/json", method = RequestMethod.POST)
    public @ResponseBody
    IJSON getTranscoder(@RequestBody final PlexUserSession plexUserSession, HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            if(plexUserSession.getPlexToken()==null){
                throw new RemoteException("NO plexToken defined");
            }
            return transcoderBO.getFreeTranscoderMachine(plexUserSession);
        } catch (Exception ex) {
            res.setStatus(500);
            return new ExceptionError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getClass().getCanonicalName() + " - " + ex.getMessage());
        }
    }

//    @RequestMapping(value = "/getTranscoder/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody
//    IJSON getTranscoderByID(@PathVariable("id") Integer id, HttpServletResponse res) {
//        try {
//            TranscoderMachine transcoderMachine = transcoderBO.getTranscoderMachine(id);
//            if (transcoderMachine == null) {
//                res.setStatus(404);
//                return new ExceptionError(HttpServletResponse.SC_NOT_FOUND, "not found");
//            } else {
//                return transcoderMachine;
//            }
//
//        } catch (Exception ex) {
//            res.setStatus(500);
//            return new ExceptionError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getClass().getCanonicalName() + " - " + ex.getMessage());
//        }
//    }

    @RequestMapping(value = "/setTranscoderStatus/{id}/{transStatus}")
    public @ResponseBody
    IJSON setMachineStatus(@PathVariable("id") Integer id, @PathVariable("transStatus") String transStatus, HttpServletRequest req, HttpServletResponse res) {
        TranscoderMachine transcoderMachine = transcoderBO.getTranscoderMachine(id);

        if (transcoderMachine == null) {
            res.setStatus(404);
            return new ExceptionError(HttpServletResponse.SC_NOT_FOUND, "not found");
        } else if (transcoderMachine.getIp().equals(req.getRemoteAddr())) {
            transcoderMachine.setStatusTranscoding(transStatus);
            transcoderMachine.setLastUsage(new Date().getTime());
            return transcoderMachine;
        } else {
            res.setStatus(401);
            return new ExceptionError(HttpServletResponse.SC_UNAUTHORIZED, "Unknow client");
        }
    }

    @RequestMapping(value = "/finishTranscoderBuilderProcess/{id}")
    public @ResponseBody
    IJSON setMachineStatus(@PathVariable("id") Integer id, HttpServletRequest req, HttpServletResponse res) {
        TranscoderMachine transcoderMachine = transcoderBO.getTranscoderMachine(id);

        if (transcoderMachine == null) {
            res.setStatus(404);
            return new ExceptionError(HttpServletResponse.SC_NOT_FOUND, "not found");
        } else if (transcoderMachine.getIp().equals(req.getRemoteAddr())) {
            transcoderMachine.setReadyToSnapshot(true);
            return transcoderMachine;
        } else {
            res.setStatus(401);
            return new ExceptionError(HttpServletResponse.SC_UNAUTHORIZED, "Unknow client");
        }
    }

    @RequestMapping(value = "/video/:/transcode/session/{sessionID}/progress")
    public void getProgress(@PathVariable("sessionID") String sessionID, HttpServletRequest req, HttpServletResponse res) throws IOException {
        Map<String, String[]> parameters = req.getParameterMap();

        for (Entry<String, String[]> entry : parameters.entrySet()) {
            String joinedString = StringUtils.join(entry.getValue(), ",");
            System.out.println(entry.getKey() + ":{" + joinedString + "}");
        }

        System.out.println("InputStream");
        System.out.println(convertStreamToString(req.getInputStream()));

    }

    @RequestMapping(value = "/mock/progress/{sessionID}")
    public void getMockProgress(@PathVariable("sessionID") String sessionID, HttpServletRequest req, HttpServletResponse res) throws IOException {

        URL realProgress = new URL("http://192.168.0.155:32400/video/:/transcode/session/" + sessionID + "/progress?width=1920&height=1080");
        HttpURLConnection httpCon = (HttpURLConnection) realProgress.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        httpCon.connect();
        httpCon.getOutputStream().close();
        httpCon.disconnect();
    }

    private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}