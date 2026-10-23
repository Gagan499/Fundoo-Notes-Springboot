package com.fundoo.notes.history;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component 
public class Console_History {
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private final Path logFile = Paths.get("console-history.log");

    @PostConstruct
    public void captureConsole() {
        PrintStream original = System.out;

        OutputStream tee = new OutputStream() {
            @Override
            public void write(int b) {
                original.write(b);
                synchronized (buffer) { buffer.write(b); }
            }
            @Override
            public void write(byte[] b, int off, int len) {
                original.write(b, off, len);
                synchronized (buffer) { buffer.write(b, off, len); }
            }
            @Override
            public void flush() { original.flush(); }
        };

        System.setOut(new PrintStream(tee, true));
    }

    @Scheduled(cron = "0 */5 * * * *")   // every 5 minutes
    public void saveLogHistory() throws IOException {
        String chunk;
        synchronized (buffer) {
            chunk = buffer.toString(StandardCharsets.UTF_8);
            buffer.reset();               // start fresh for the next 5 minutes
        }
        if (chunk.isEmpty()) return;

        String entry = "=== " + LocalDateTime.now() + " ===\n" + chunk + "\n";
        Files.writeString(logFile, entry, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
