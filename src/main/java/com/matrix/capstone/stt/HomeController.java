package  com.matrix.capstone.stt;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on : 2019-04-01, 11:28:15 p.m.
 *
 * @author Parteek Dheri
 */
@RestController
public class HomeController {
	private static final Logger log = LogManager.getLogger(HomeController.class);
	private static int fileCount = 0;

	@RequestMapping("/")
	public String goHome(Model model) {
		return "home";
	}

	@RequestMapping(value = "/audioFile", method = RequestMethod.POST)
	public Map<String, String> getAudio(Model model, InputStream dataStream) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("author", "dheri");
		map.put("time", new Date().toString());
		log.info("processed at {}", map.get("time"));

		synchronized (this) {
			new S2TWorker(dataStream, ++fileCount).run();
		}

		return map;
	}


}
