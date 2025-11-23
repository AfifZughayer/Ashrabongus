import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.annotation.RegisterProperty;
import godot.core.Vector3;

@RegisterClass
public class Missile extends Projectile{

    @RegisterProperty
    public Player player;

    @RegisterFunction
    @Override
    public void _ready(){
        player = (Player) getTree().getFirstNodeInGroup("player");
    }

    @Override
    public void move(double delta) {
        lookAt(player.getGlobalPosition());
        Vector3 pos = getPosition().plus(getBasis().getZ().times(speed * delta));
        setPosition(pos);
    }

}
