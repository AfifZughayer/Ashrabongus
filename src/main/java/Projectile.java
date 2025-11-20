import godot.annotation.Export;
import godot.annotation.RegisterFunction;
import godot.annotation.RegisterProperty;
import godot.annotation.RegisterClass;
import godot.api.Area3D;
import godot.core.*;

@RegisterClass
public class Projectile extends Area3D {

    @Export
    @RegisterProperty
    public float dmg;

    @Export
    @RegisterProperty
    public float speed = 5;

    @RegisterProperty
    public float timer = 0;

    @RegisterFunction
    public void dealDamage(Area3D area) {
        if (area.isInGroup("damageable")){
            area.queueFree();
        }
    }

    @RegisterFunction
    public void _process(double delta){
        if (timer >= 5){
            queueFree();
        }
        timer += delta;

        Vector3 pos = getPosition().plus(getBasis().getZ().times(speed * delta));
        setPosition(pos);
    }

}
