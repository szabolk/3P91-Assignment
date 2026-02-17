package GameComponents;

public interface IAttacker {
    public int getDamage();

    default void attack(IAttackable target) {
        if (target == null) { throw new IllegalAttackException("Attack Failed: Entity Does Not Exist (Null)"); }
        if (target.isDestroyed()) { throw new IllegalAttackException("Attack Failed: Entity is Destroyed"); }
        target.takeDamage(this.getDamage());
    }
    /**
     * Should an attack happen on a unit that is already destroyed, this
     * exception will be thrown
     */
    public class IllegalAttackException extends RuntimeException {
        public IllegalAttackException(String message) {
            super(message);
        }
    }
}
