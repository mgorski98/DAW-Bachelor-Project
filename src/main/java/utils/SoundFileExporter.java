package utils;

import lombok.AllArgsConstructor;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@AllArgsConstructor
public class SoundFileExporter {

    private SoundClip clip;

    public void export(String path) throws IOException {
        File f = new File(path);
        float[] samples = clip.getSamples();
        var sampleSize = clip.getAudioFormat().getSampleSizeInBits() / 8;
        byte[] bytes = new byte[samples.length * sampleSize];
        int currentIndex = 0;
        byte[] sampleBytes;
        ByteOrder order = clip.getAudioFormat().isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
        switch (sampleSize) {
            case 1:
                for (int i = 0; i < samples.length; ++i) {
                    bytes[i] = (byte)(samples[i] * Byte.MAX_VALUE);
                }
                break;
            case 2:
                for (float sample : samples) {
                    ByteBuffer bb = ByteBuffer.allocate(sampleSize).order(order);
                    bb.putShort((short) (sample * Short.MAX_VALUE));
                    sampleBytes = bb.array();
                    for (int j = 0; j < sampleSize; ++j) {
                        bytes[currentIndex++] = sampleBytes[j];
                    }
                }
                break;
            case 4:
                for (float sample : samples) {
                    ByteBuffer _bb = ByteBuffer.allocate(sampleSize).order(order);
                    _bb.putFloat(sample);
                    sampleBytes = _bb.array();
                    for (int j = 0; j < sampleSize; ++j) {
                        bytes[currentIndex++] = sampleBytes[j];
                    }
                }
                break;
        }
        try (var bais = new ByteArrayInputStream(bytes);
                var stream = new AudioInputStream(bais, clip.getAudioFormat(), bytes.length)) {
            AudioSystem.write(stream, AudioFileFormat.Type.WAVE, f);
        }
    }
}
