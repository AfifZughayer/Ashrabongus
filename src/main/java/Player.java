import godot.annotation.Export;
import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.annotation.RegisterProperty;
import godot.api.*;
import godot.core.Vector3;

import java.util.Random;

@RegisterClass
public class Player extends RigidBody3D implements MechanicShoot{

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

    @Export
    @RegisterProperty
    public float throttle;
    @RegisterProperty
    public float roll;
    @RegisterProperty
    public float pitch;
    @RegisterProperty
    public float yaw;
    @Export
    @RegisterProperty
    public float response = 0.5f;

    @Export
    @RegisterProperty
    public PackedScene projectile;
    @Export
    @RegisterProperty
    public Marker3D gunPos;

    @RegisterFunction
    public void _ready(){
        Input.setMouseMode(Input.MouseMode.CAPTURED);
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

    }

    @RegisterFunction
    public void _process(double delta) {
        roll = Input.getAxis("left", "right");
        pitch = Input.getAxis("up", "down");
        yaw = Input.getAxis("yawLeft", "yawRight");

        shoot(delta);
    }

    @RegisterFunction
    public void _physics_process(double delta){
        applyCentralForce(getBasis().getZ().times(throttle));
        applyTorque(getBasis().getY().times(yaw * response));
        applyTorque(getBasis().getX().times(pitch * response));
        applyTorque(getBasis().getZ().times(roll * response));
    }

    @RegisterFunction
    public void shoot(double delta){
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
            proj_instance.setGlobalRotation(getGlobalRotation());
            timer = 1.0f / fireRate;
        }
    }

}
