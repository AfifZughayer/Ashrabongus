import godot.annotation.Export;
import godot.annotation.RegisterFunction;
import godot.annotation.RegisterProperty;
import godot.annotation.RegisterClass;
import godot.api.Area3D;
import godot.api.Node3D;
import godot.core.*;

@RegisterClass
public abstract class Projectile extends Area3D {

    @Export
    @RegisterProperty
    public int dmg;

    @RegisterProperty
    public String tag = "";

    @Export
    @RegisterProperty
    public float speed = 5;

    @RegisterProperty
    public float timer = 0;

    @RegisterFunction
    public void dealDamage(Node3D body) {
        if (body instanceof HealthComponent && body.isInGroup(tag)){
            ((HealthComponent) body).takeDamage(dmg);
        }
    }

    public abstract void move(double delta);

}
