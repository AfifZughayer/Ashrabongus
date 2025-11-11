import godot.annotation.Export;
import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.annotation.RegisterProperty;
import godot.api.Node;

@RegisterClass
public class HealthComponent extends Node implements HealthSystem {

    @Export
    @RegisterProperty
    public int maxHealth = 100;

    private int health;

    @RegisterFunction
    public void _ready() {
        health = maxHealth;
        addToGroup("damageable");
    }

    @Override
    public int getHealth() {
        return health;
    }


    @RegisterFunction
    @Override
    public void takeDamage(int amount) {
        if (amount <= 0) return;
        if (health == 0) return;
        health = health-amount;
        if (health <= 0) {
            onDeath();
        }
    }

    @RegisterFunction
    @Override
    public void onDeath() {
        queueFree();
    };
}
