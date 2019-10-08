/**
 * 
 */
package com.matrix.capstone.stt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on : 2019-04-02, 1:46:46 a.m.
 *
 * @author Parteek Dheri
 */
public class S2TWorker implements Runnable {

	private static String deepspeechDir = "/deepspeech";
	private static String audioPrefix = "rx_audio";
	private static String textPrefix = "tr_txt";

	private static String deepspeechExePath = "/home/ubuntu/capstone/runtime/startup.sh";

	private InputStream dataStream;
	private String fileIdentifier;

	private static final Logger log = LoggerFactory.getLogger(HomeController.class);

	@Override
	public void run() {
		String audioFileName = audioPrefix + fileIdentifier;
		Path trxDirPath = FileSystems.getDefault().getPath(deepspeechDir, fileIdentifier).toAbsolutePath();
		Path audioFilePath = trxDirPath.resolve(audioFileName);
		long bytes;
		try {
			Files.createDirectories(trxDirPath);
			log.info(" {} dir created.", trxDirPath.toString());
			bytes = Files.copy(dataStream, audioFilePath);
			log.info("rxAudio{}, {} bytes copied.", fileIdentifier, bytes);

			List<String> commands = new ArrayList<String>();
			commands.add("sudo");
			commands.add(deepspeechExePath);
			commands.add(audioFilePath.toString());


			ProcessBuilder pb = new ProcessBuilder();
			pb.command(commands);
			pb.directory(new File(deepspeechExePath).getParentFile());
			File outputText = new File(trxDirPath.toFile(), textPrefix + fileIdentifier);
			log.info("process builder dir {}", String.join(" ", commands));

			pb.redirectErrorStream(false);
			pb.redirectError(new File(trxDirPath.toFile(), "errs" + fileIdentifier));
			pb.redirectOutput(Redirect.appendTo(outputText));
			Process p = pb.start();
			p.waitFor();
			log.debug("stt ended");



		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			log.error("IOException  -> {}", e.toString());
		}
	}

	/**
	 * @param dataStream
	 */
	public S2TWorker(InputStream dataStream, int fileCount) {
		super();
		this.dataStream = dataStream;
		this.fileIdentifier = "_" + fileCount;
	}

}
