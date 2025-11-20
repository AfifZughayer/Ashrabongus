import godot.annotation.Export;
import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.annotation.RegisterProperty;
import godot.api.*;
import godot.core.Vector3;
import godot.global.GD;

import java.util.Random;

@RegisterClass
public class Player extends CharacterBody3D implements ShootComponent {

    @Export
    @RegisterProperty
    public float speed = 5f;
    @Export
    @RegisterProperty
    public float fireRate = 1;
    @RegisterProperty
    public float timer = 0;

    @RegisterProperty
    public float roll;
    @RegisterProperty
    public float pitch;
    @RegisterProperty
    public Vector3 rot;

    @Export
    @RegisterProperty
    public PackedScene projectile;
    @Export
    @RegisterProperty
    public Marker3D gunPos;
    @Export
    @RegisterProperty
    public Node3D jet;

    @RegisterFunction
    public void _ready(){
        Input.setMouseMode(Input.MouseMode.CAPTURED);
    }

    @RegisterFunction
    public void _input(InputEvent event){
        assert event != null;
    }

    @RegisterFunction
    public void _process(double delta) {
        roll = Input.getAxis("left", "right");
        pitch = Input.getAxis("up", "down");
        rot = new Vector3(1, 1,45 * roll);
        jet.setRotationDegrees(jet.getRotationDegrees().lerp(rot, 0.05));

        shoot(delta);
    }

    @RegisterFunction
    public void _physicsProcess(double delta){
//        applyCentralForce(getBasis().getZ().times(throttle));
//        applyTorque(getBasis().getY().times(-yaw * sens * getMass()));
//        applyTorque(getBasis().getX().times(pitch * sens * getMass()));
//        applyTorque(getBasis().getZ().times(roll * sens * getMass()));
        setVelocity(getBasis().getZ().times(speed));
        moveAndSlide();
        jet.rotateZ((float)Math.toRadians(roll));
        rotateY(-(float)Math.toRadians(roll));
        globalRotate(getBasis().getX(), (float)Math.toRadians(pitch));
    }

    @RegisterFunction
    @Override
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
