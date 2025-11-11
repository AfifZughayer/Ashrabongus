import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.api.Area3D;
import godot.api.Node3D;
import godot.api.StaticBody3D;

@RegisterClass
public class Wall extends Area3D {

    @RegisterFunction
    public void dealDamage(Node3D body) {
        if (body.isInGroup("entity")){
            getTree().reloadCurrentScene();
        }
    }

}
