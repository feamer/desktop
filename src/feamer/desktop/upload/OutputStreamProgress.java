package feamer.desktop.upload;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;

public class OutputStreamProgress extends OutputStream {

    private final OutputStream outstream;
    private volatile long bytesWritten=0;
    Consumer<Integer> callback;

    public OutputStreamProgress(OutputStream outstream, Consumer<Integer> callback) {
        this.outstream = outstream;
        this.callback = callback;
    }

    @Override
    public void write(int b) throws IOException {
        outstream.write(b);
        bytesWritten++;
    }

    @Override
    public void write(byte[] b) throws IOException {
        outstream.write(b);
        bytesWritten += b.length;
        this.callback.accept((int)bytesWritten);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        outstream.write(b, off, len);
        bytesWritten += len;
        this.callback.accept((int)bytesWritten);
    }

    @Override
    public void flush() throws IOException {
        outstream.flush();
    }

    @Override
    public void close() throws IOException {
        outstream.close();
    }

    public long getWrittenLength() {
        return bytesWritten;
    }
}