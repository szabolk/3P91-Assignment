package GameComponents;

/**
 * Defines behaviour for attackable things
 */
public interface IAttackable {
    public void takeDamage(int damage);
    public boolean isDestroyed();
    public int getHP();
}
