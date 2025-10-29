import godot.annotation.Export;
import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.annotation.RegisterProperty;
import godot.api.*;
import godot.core.Vector3;

import java.util.Random;

@RegisterClass
public class Player extends CharacterBody3D {

    @Export
    @RegisterProperty
    public float speed = 5f;
    @Export
    @RegisterProperty
    public float sens = 0.005f;
    @Export
    @RegisterProperty
    public float fireRate = 1;
    @RegisterProperty
    public float timer = 0;

    @RegisterProperty
    public Camera3D cam;
    @Export
    @RegisterProperty
    public PackedScene projectile;
    @Export
    @RegisterProperty
    public Marker3D gunPos;

    @RegisterFunction
    public void _ready(){
        Input.setMouseMode(Input.MouseMode.CAPTURED);
        cam = (Camera3D) getNode("Camera3D");
    }

    @RegisterFunction
    public void _input(InputEvent event){
        assert event != null;
        if (event.isActionPressed("Space")){
            speed = 0f;
        }
        if (event.isActionReleased("Space")){
            speed = 5f;
        }
        if (event instanceof InputEventMouseMotion mouseEvent){
            rotateY((float) -mouseEvent.getRelative().getX() * sens);
            cam.rotateX((float) mouseEvent.getRelative().getY() * sens);
        }
    }

    @RegisterFunction
    public void _process(double delta) {
        if (timer > 0){
            timer -= delta;
        }
        if (Input.isActionPressed("Shoot") && timer <= 0) {
            Node3D proj_instance = (Node3D) projectile.instantiate();
            getTree().getCurrentScene().addChild(proj_instance);
            Random rand = new Random();
            float mix = ((rand.nextFloat()*2)-1) * 0.05f;
            Vector3 pos = new Vector3(gunPos.getGlobalPosition().getX() + mix, gunPos.getGlobalPosition().getY() + mix, gunPos.getGlobalPosition().getZ() + mix);
            proj_instance.setGlobalPosition(pos);
            proj_instance.setGlobalRotation(cam.getGlobalRotation());
            timer = 1.0f / fireRate;
        }
    }

    @RegisterFunction
    public void _physics_process(double delta){
        setVelocity(cam.getGlobalBasis().getZ().times(-speed));
        moveAndSlide();
    }

}
