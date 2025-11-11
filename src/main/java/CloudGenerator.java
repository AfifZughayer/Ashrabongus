import godot.annotation.Export;
import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.annotation.RegisterProperty;
import godot.api.Node3D;
import godot.api.PackedScene;
import godot.core.Vector3;

@RegisterClass
public class CloudGenerator extends Node3D {

    @Export
    @RegisterProperty
    public PackedScene cloud;

    @Export
    @RegisterProperty
    public int range = 10;

    @Export
    @RegisterProperty
    public int count = 50;

    Boolean flag = true;

    @RegisterFunction
    public void _process(double delta){
        if (flag == true) {
            createClouds();
            flag = false;
        }
    }

    @RegisterFunction
    public void createClouds(){
        for (int i = 0; i < count; i++){
            Node3D cloudInstance = (Node3D) cloud.instantiate();
            int randX = (int)(Math.random() * range);
            int randZ = (int)(Math.random() * range);
            cloudInstance.setPosition(new Vector3(randX, 0, randZ));
            getTree().getCurrentScene().addChild(cloudInstance);
        }
    }

}
