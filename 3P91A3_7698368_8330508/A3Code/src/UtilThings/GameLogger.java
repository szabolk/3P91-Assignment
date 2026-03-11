package UtilThings;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * This is used for logging different events for when the game is running. It tracks things like resources
 * added to players, attacks that happens to player villages, player attacks on explored villages, build/train/upgrade scheduling,
 * build/train/upgrade completion, etc.
 */
public class GameLogger {
    private static final String LOG_FILE = "game_log.txt"; //swap
    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Used as a simple logging tool, using a timestamp and the event as the content written to the file
     * @param event - event to be written to the file
     */
    public static void log(String event) {
        String entry = "[" + LocalDateTime.now().format(format) + "] " + event;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(entry);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Warning: could not write to game log: " + e.getMessage());
        }
    }
}
