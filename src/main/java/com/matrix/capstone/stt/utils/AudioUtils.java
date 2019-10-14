package com.matrix.capstone.stt.utils;

import javax.sound.sampled.*;
import java.io.*;

public class AudioUtils {


    public static int mp3ToWav(InputStream mp3Data, String fileName) throws UnsupportedAudioFileException, IOException {
        // open stream
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
