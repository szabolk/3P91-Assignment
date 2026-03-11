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
    public long getTime() {
        return (System.currentTimeMillis() - startTime);
    }

    /**
     * Checks if an event, like if a building is finished building
     *
     * @param time - the finish time of the specific event
     * @return boolean - true if event is finished, false if not
     */
    public boolean eventFinished(long time) {
        return getTime() >= time;
    }
}
