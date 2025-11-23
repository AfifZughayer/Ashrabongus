import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.api.Area3D;
import godot.api.Node3D;

@RegisterClass
public class Wall extends Area3D implements DamageComponent {

    @RegisterFunction
    public void dealDamage(Node3D body) {
        if (body.isInGroup("player")){
            getTree().reloadCurrentScene();
        }
    }

    @Override
    public void areaDealDamage(Area3D area) {
        if (area.isInGroup("player")){
            getTree().reloadCurrentScene();
        }
    }

}
