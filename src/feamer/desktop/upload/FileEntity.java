package feamer.desktop.upload;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;

public class FileEntity extends org.apache.http.entity.FileEntity {

    private OutputStreamProgress outstream;
    Consumer<Integer> callback;

    public FileEntity(File file, Consumer<Integer> callback) {
        super(file);
        this.callback = callback;
    }

    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        this.outstream = new OutputStreamProgress(outstream, value -> {
        	this.callback.accept(this.getProgress());
        });
        super.writeTo(this.outstream);
    }

    /**
     * Progress: 0-100
     */
    public int getProgress() {
        if (outstream == null) {
            return 0;
        }
        long contentLength = getContentLength();
        if (contentLength <= 0) { // Prevent division by zero and negative values
            return 0;
        }
        long writtenLength = outstream.getWrittenLength();
        return (int) (100*writtenLength/contentLength);
    }
}