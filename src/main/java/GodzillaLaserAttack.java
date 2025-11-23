import godot.annotation.Export;
import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.annotation.RegisterProperty;
import godot.api.Marker3D;
import godot.api.Node3D;
import godot.api.PackedScene;
import godot.core.Callable;
import godot.core.StringNames;
import godot.core.Vector3;

@RegisterClass
public class GodzillaLaserAttack extends Attack{

    @RegisterProperty
    @Export
    public PackedScene laser;
    @Export
    @RegisterProperty
    public Marker3D laserPos;
    public Player player;

    @RegisterFunction
    @Override
    public void _ready() {
        player = (Player) getTree().getFirstNodeInGroup("player");
    }

    @Override
    public void attack() {
        Node3D laser_instance = (Node3D) laser.instantiate();
        laser_instance.setGlobalPosition(laserPos.getGlobalPosition());
        ((Laser) laser_instance).finished.connect(Callable.create(this, StringNames.toGodotName("attackFinish")), 0);
        getTree().getCurrentScene().addChild(laser_instance);
        laser_instance.lookAt(player.getGlobalPosition(), Vector3.Companion.getUP(), true);
    }

    @Override
    public void attackFinish(){
        update();
    }

}
