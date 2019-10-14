/**
 * 
 */
package com.matrix.capstone.stt;

import com.matrix.capstone.stt.utils.AudioUtils;
import com.matrix.capstone.stt.utils.BackendUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import javax.sound.sampled.UnsupportedAudioFileException;
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
 * Created on : 2019-10-02, 1:46:46 a.m.
 *
 * @author Parteek Dheri
 */
public class S2TWorker implements Runnable {
    private Environment environment;

	private  String deepSpeechDir;
	private   String audioPrefix = "rx_audio";
	private   String textPrefix = "tr_txt";

	private   String deepSpeechExePath;
	
	private InputStream dataStream;
	private String fileIdentifier;

	private static final Logger log = LoggerFactory.getLogger(HomeController.class);

	@Override
	public void run() {
		String audioFileName = audioPrefix + fileIdentifier;
		Path trxDirPath = FileSystems.getDefault().getPath(deepSpeechDir, fileIdentifier).toAbsolutePath();
		Path audioFilePath = trxDirPath.resolve(audioFileName);
		try {

			Files.createDirectories(trxDirPath);

			log.info(" {} dir created.", trxDirPath.toString());
			int waveFileSize = AudioUtils.mp3ToWav(dataStream, audioFilePath.toString());
			log.info("rxAudio{}, {} bytes copied.", fileIdentifier, waveFileSize);

			List<String> commands = new ArrayList<String>();
			commands.add("sudo");
			commands.add(deepSpeechExePath);
			commands.add(audioFilePath.toString());


			ProcessBuilder pb = new ProcessBuilder();
			pb.command(commands);
			pb.directory(new File(deepSpeechExePath).getParentFile());
			File outputText = new File(trxDirPath.toFile(), textPrefix + fileIdentifier);
			log.info("process builder dir {}", String.join(" ", commands));

			pb.redirectErrorStream(false);
			pb.redirectError(new File(trxDirPath.toFile(), "errs" + fileIdentifier));
			pb.redirectOutput(Redirect.appendTo(outputText));
			Process p = pb.start();
			p.waitFor();
            log.debug("translation ended");
            new File(trxDirPath.resolve(audioFileName.concat("_txt")).toString());

            new BackendUtils(environment).callback(null,fileIdentifier.replace("_",""));
			log.debug("callback ended");
			boolean wavFileDeleted = new File(audioFilePath.toString()).delete();
            log.debug("wavFileDeleted ? ".concat(String.valueOf(wavFileDeleted)));


        } catch (IOException | InterruptedException | UnsupportedAudioFileException e) {
			e.printStackTrace();
			log.error("IOException  -> {}", e.toString());
		}
	}

	public S2TWorker(InputStream dataStream, String fileCount, Environment environment) {
		super();
		this.dataStream = dataStream;
		this.fileIdentifier = "_" + fileCount;
		this.environment = environment;
        this.deepSpeechDir = environment.getProperty("spring.application.deepSpeech-dir");
        this.deepSpeechExePath = environment.getProperty("spring.application.deepSpeech-exe-path");

    }

}
