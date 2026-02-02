package GameComponents;

public interface IMoveable {
    public boolean move(int dx, int dy); //Movement successful = true, failed movement = false
    public int getX();
    public int getY();
}
