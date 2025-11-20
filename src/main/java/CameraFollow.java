import godot.annotation.Export;
import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.annotation.RegisterProperty;
import godot.api.Camera3D;
import godot.api.Node3D;
import godot.core.Basis;
import godot.core.Vector3;

@RegisterClass
public class CameraFollow extends Camera3D {

    @Export
    @RegisterProperty
    public Node3D player;

    @RegisterFunction
    public void _process(double delta){
        Vector3 playerPos = player.getGlobalPosition();
        setGlobalPosition(playerPos.plus(player.getGlobalBasis().getZ().times(-9)).plus(player.getGlobalBasis().getY().times(2)));
        lookAt(playerPos);
    }

}
