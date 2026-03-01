package Game;

/**
 * This class keeps track of the game time used in GameEngine
 */
public class Time {
    private long startTime;

    public Time() {
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Gets the current time (real time maybe? seems like the easiest to implement)
     * @return int - the current time in milliseconds since game start
     */
    public int getTime() {
        return (int)(System.currentTimeMillis() - startTime);
    }
}
