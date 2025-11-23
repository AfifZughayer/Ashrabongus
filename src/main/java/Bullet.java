import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.core.Vector3;

@RegisterClass
public class Bullet extends Projectile{

    @Override
    public void move(double delta) {
        Vector3 pos = getPosition().plus(getBasis().getZ().times(speed * delta));
        setPosition(pos);
    }
}
