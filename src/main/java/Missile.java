import godot.annotation.Export;
import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.annotation.RegisterProperty;
import godot.core.Vector3;
import godot.global.GD;

@RegisterClass
public class Missile extends Projectile implements HealthComponent{

    @Export
    @RegisterProperty
    public int maxHealth;
    @RegisterProperty
    public int currentHealth = maxHealth;

    @RegisterProperty
    public Player player;

    @RegisterFunction
    @Override
    public void _ready(){
        player = (Player) getTree().getFirstNodeInGroup("player");
    }

    @Override
    public void move(double delta) {
        lookAt(player.getGlobalPosition(), Vector3.Companion.getUP(), true);
        Vector3 pos = getPosition().plus(getBasis().getZ().times(speed * delta));
        setPosition(pos);
    }

    @Override
    public void takeDamage(int amount) {
        currentHealth -= amount;
        if (currentHealth <= 0){
            onDeath();
        }
    }

    @Override
    public void onDeath() {
        callDeferred("queue_free");
    }
}
