package org.segator.plex.cloud.transcoding.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.http.HttpHeaders;
import org.segator.plex.cloud.transcoding.TranscoderBO;
import org.segator.plex.cloud.transcoding.entity.TranscoderMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author isaac_000
 */
@Controller
public class Downloader {

    @Autowired
    private TranscoderBO transcoderBO;

    public String decode(String s) {
        return StringUtils.newStringUtf8(Base64.decodeBase64(s));
    }

    @RequestMapping(value = "/download/{id}/{path64}")
    public void downloadFile(@PathVariable("id") Integer id, @PathVariable("path64") String path64, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TranscoderMachine transcoderMachine = transcoderBO.getTranscoderMachine(id);

        if (transcoderMachine == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } else if (!transcoderMachine.getIp().equals(request.getRemoteAddr())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
 
        //File file = new File(decode(path64));
        File file = new File(System.getProperty("user.dir")+"/"+decode(path64));
        if(request.getParameter("absolute")!=null && request.getParameter("absolute").equals("1")){
            file = new File(decode(path64));
        }
        
        //Request Headers
        String range = request.getHeader("range");
        String conection = request.getHeader("Connection");
        String responseDesc = "Not Found";
        Integer responseCode = HttpServletResponse.SC_NOT_FOUND;
        if (file.exists()) {
            if (file.isDirectory()) {
               OutputStream  ZipedOutputStream = zipOutputStream(file, response.getOutputStream());
               ZipedOutputStream.close();
            } else {
            OutputStream outputStream = response.getOutputStream();

                String contentRange = null;

                Long rangeInit = 0l;
                Long rangeFinish = file.length();
                Long contentLength = file.length();
                //Accept partial download
                if (range != null) {
                    if (range.contains("bytes")) {
                        String rangebytes = range.split("=")[1];
                        String[] rangebytesSplited = rangebytes.split("-");
                        rangeInit = Long.valueOf(rangebytesSplited[0].trim());
                        contentRange = rangeInit.toString();
                        contentLength -= rangeInit;
                        if (rangebytesSplited.length > 1) {
                            rangeFinish = Long.valueOf(rangebytesSplited[1].trim());
                            contentRange += "-" + rangeFinish.toString();
                            contentLength -= (file.length() - rangeFinish);
                        } else {
                            contentRange += "-" + (file.length() - 1l);
                        }
                    } else {
                        responseCode = HttpServletResponse.SC_BAD_REQUEST;
                        responseDesc = "Bad Request";
                    }
                }

                if (range != null) {
                    responseCode = HttpServletResponse.SC_PARTIAL_CONTENT;
                    responseDesc = "Partial Content";
                } else {
                    responseCode = HttpServletResponse.SC_OK;
                    responseDesc = "OK";
                }

                response.setHeader(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
                String fileName = file.getName();
                if (file.isDirectory()) {
                    fileName += ".zip";
                }
                response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
                response.setHeader("Content-Transfer-Encoding", "binary");

                if (contentLength > 0) {
                    response.setHeader("Accept-Ranges", "bytes");
                    response.setHeader(HttpHeaders.CONTENT_LENGTH, contentLength.toString());
                }

                if (contentRange != null) {
                    response.setHeader("Content-Range", "bytes " + contentRange + "/" + file.length());
                }

                //Define connection type            
                if (conection != null && conection.equals("keep-alive")) {
                    response.setHeader("Connection", "keep-alive");
                    response.setHeader("Keep-Alive", "timeout=1500, max=98");
                } else {
                    response.setHeader("Connection", "close");
                }
                response.setHeader("Cache-Control", "no-cache");
                response.setStatus(responseCode, responseDesc);
                response.flushBuffer();
                FileInputStream fis = new FileInputStream(file);
                fis.skip(rangeInit);
                downloadFile(fis, outputStream, true, rangeFinish);
            }

        } else {
            response.setStatus(responseCode, responseDesc);
        }
    }

    public static final int DEFAULT_BUFFER_SIZE = 32 * 1024;

    public static void downloadFile(InputStream senderStream, OutputStream receiverStream, boolean closeOutput, long bytesToCopy) throws IOException, Exception {

        if (senderStream != null) {
//        Long totalBytesReaded=0l;
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

            int sizeRead = DEFAULT_BUFFER_SIZE;
            int readedBytes = 0;
            try {
                while (bytesToCopy > 0 && -1 != (readedBytes = senderStream.read(buffer, 0, sizeRead))) {
                    //download when something to read            

                    receiverStream.write(buffer, 0, readedBytes);
                    bytesToCopy -= readedBytes;
                    if (bytesToCopy < sizeRead) {
                        sizeRead = (int) (bytesToCopy);
                    }
                }
            } catch (Exception ex) {
            } finally {
                try {
                    receiverStream.flush();
                } catch (IOException ex) {

                }

                try {
                    if (closeOutput) {
                        receiverStream.close();
                    }
                } catch (Exception ex) {

                }

                try {
                    senderStream.close();
                } catch (IOException ex) {

                }

            }
        } else {
            try {
                if (closeOutput) {
                    receiverStream.close();
                }

            } catch (IOException ex) {

            }
        }
    }

    public static String getDirectoryFromUrl(String url, String type) {
        if (url.contains("/")) {
            url = url.substring(0, url.lastIndexOf("/") + 1);
        } else if (url.contains("\\")) {
            url = url.substring(0, url.lastIndexOf("\\") + 1);
        } else {
            return type;
        }
        if (!url.startsWith(type)) {
            return type + url;
        }
        return url;

    }

    private OutputStream zipOutputStream(File directory, OutputStream outputStream) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            zipOutputStream.setLevel(ZipEntry.STORED);
            for (File file : directory.listFiles()) {
                //Creating Zip File
                zipFileConstructionRecursive(file, zipOutputStream, file.getParent()+File.separator);

            }
            return zipOutputStream;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void zipFileConstructionRecursive(File file, ZipOutputStream zipOutputStream, String cutURL) throws IOException, Exception {
        if (file.isDirectory()) {

            if (file.listFiles().length > 0) {
                zipOutputStream.putNextEntry(new ZipEntry(file.getAbsolutePath().replaceFirst(Pattern.quote(cutURL), "") + File.separator));
                for (File fileChild : file.listFiles()) {
                    zipFileConstructionRecursive(fileChild, zipOutputStream, cutURL);
                }
            }
        } else {
            
            zipOutputStream.putNextEntry(new ZipEntry(file.getAbsolutePath().replaceFirst(Pattern.quote(cutURL), "")));
            downloadFile(new FileInputStream(file), zipOutputStream, false, file.length());
        }
    }

}
