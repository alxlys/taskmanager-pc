package taskmanager.command;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class DownloadCommand implements Command {

    private final String url;
    private final BlockingQueue<Path> queue;
    private final AtomicLong counter;

    DownloadCommand(String url, BlockingQueue<Path> queue, AtomicLong counter) {
        this.url = url;
        this.queue = queue;
        this.counter = counter;
    }

    @Override
    public void execute() throws Exception {
        String[] parts = url.split(/*File.separator*/"/");
        Path outFile = Paths.get(parts[parts.length - 1]);
        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, outFile, StandardCopyOption.REPLACE_EXISTING);
            queue.put(outFile);
            counter.incrementAndGet();
        }
    }
}
