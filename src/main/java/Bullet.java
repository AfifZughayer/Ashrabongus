import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.core.Vector3;

@RegisterClass
public class Bullet extends Projectile{


    @RegisterFunction
    public void _process(double delta){
        move(delta);
    }

    @Override
    public void move(double delta) {
        if (timer >= 5){
            queueFree();
        }
        timer += delta;

        Vector3 pos = getPosition().plus(getBasis().getZ().times(speed * delta));
        setPosition(pos);
    }
}
