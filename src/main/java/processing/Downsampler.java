package processing;

public class Downsampler implements Processing {
    @Override
    public float[] apply(float[] buffer) {
        return new float[0];
    }

    @Override
    public float[] apply(float[] buffer, int offset, int len) {
        return new float[0];
    }
}