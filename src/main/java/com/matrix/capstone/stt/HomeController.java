package com.matrix.capstone.stt;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on : 2019-10-01, 11:28:15 p.m.
 *
 * @author Parteek Dheri
 */
@RestController
public class HomeController {
    private static final Logger log = LogManager.getLogger(HomeController.class);
    private static int fileCount = 0;

    @RequestMapping("/")
    public Map<String, String> goHome(Model model) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("time", new Date().toString());
        log.info("The system time is {}", map.get("time"));
        return map;
    }

    @PostMapping("/validateNarration")
    public Map<String,String> handleFileUpload(@RequestParam("file") MultipartFile file,  @RequestParam("narrationId") String narrationId ) throws IOException {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("status", "success");
        map.put("time", new Date().toString());
        log.info("processed at {}", map.get("time"));

        synchronized (this) {
            new S2TWorker(file.getInputStream(), Integer.parseInt(narrationId)).run();
        }
        return map;

    }
//
//    @RequestMapping(value = "/audioFile", method = RequestMethod.POST)
//    public Map<String, String> getAudio(@RequestParam("file") MultipartFile file, @RequestParam("narrationID") int narrationID ) throws Exception {
//        HashMap<String, String> map = new HashMap<String, String>();
//        map.put("status", "success");
//        map.put("time", new Date().toString());
//        log.info("processed at {}", map.get("time"));
//
//        synchronized (this) {
//            new S2TWorker(file.getInputStream(), narrationID).run();
//        }
//
//        return map;
//    }


}
