import godot.annotation.Export;
import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.annotation.RegisterProperty;
import godot.api.*;

@RegisterClass
public class Player extends CharacterBody3D {

    @Export
    @RegisterProperty
    public int speed = 5;
    @Export
    @RegisterProperty
    public float sens = 0.005f;
    @Export
    @RegisterProperty
    public Camera3D cam;

    @RegisterFunction
    public void _ready(){
        Input.setMouseMode(Input.MouseMode.CAPTURED);
        cam = (Camera3D) getNode("Camera3D");
    }

    @RegisterFunction
    public void _input(InputEvent event){
        if (event instanceof InputEventMouseMotion mouseEvent){
            rotateY((float) -mouseEvent.getRelative().getX() * sens);
            cam.rotateX((float) mouseEvent.getRelative().getY() * sens);
        }
    }

    @RegisterFunction
    public void _physics_process(double delta){
        setVelocity(cam.getGlobalBasis().getZ().times(-speed));
        moveAndSlide();
    }

}
