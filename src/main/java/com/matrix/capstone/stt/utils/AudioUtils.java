package com.matrix.capstone.stt.utils;

import com.matrix.capstone.stt.HomeController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.*;
import java.io.*;

public class AudioUtils {

    private static final Logger log = LogManager.getLogger(AudioUtils.class);

    public static int mp3ToWav(InputStream mp3Data, String fileName) throws UnsupportedAudioFileException, IOException {

        log.info(AudioSystem.getAudioFileFormat(mp3Data));

        AudioInputStream mp3Stream = AudioSystem.getAudioInputStream(mp3Data);
        AudioFormat sourceFormat = mp3Stream.getFormat();
        // create audio format object for the desired stream/audio format
        // this is *not* the same as the file format (wav)
        AudioFormat convertFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                sourceFormat.getSampleRate(), 16,
                sourceFormat.getChannels(),
                sourceFormat.getChannels() * 2,
                sourceFormat.getSampleRate(),
                false);
        // create stream that delivers the desired format
        AudioInputStream converted = AudioSystem.getAudioInputStream(convertFormat, mp3Stream);
        // write stream into a file with file format wav
        int fileSize = AudioSystem.write(converted, AudioFileFormat.Type.WAVE, new File(fileName));
        return fileSize;
    }

}
