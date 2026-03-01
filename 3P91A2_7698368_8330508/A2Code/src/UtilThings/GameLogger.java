package UtilThings;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GameLogger {
    private static final String LOG_FILE = "game_log.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(String event) {
        String entry = "[" + LocalDateTime.now().format(FORMATTER) + "] " + event;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(entry);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Warning: could not write to game log: " + e.getMessage());
        }
    }
}
