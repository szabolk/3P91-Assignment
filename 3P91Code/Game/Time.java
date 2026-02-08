package Game;

//Keeps the time of the game/upgrades/building
public class Time {
    private int time;

    public Time() {

    }

    /**
     * Gets the current time (real time maybe? seems like the easiest to implement)
     * @return int - the current time
     */
    public int getTime() {
        return 0;
    }

    /**
     * Checks if an event, like if a building is finished building
     *
     * @param time - the finish time of the specific event
     * @return boolean - true if event is finished, false if not
     */
    public boolean eventFinished(int time) {
        return true;
    }
}
