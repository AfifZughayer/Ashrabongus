import godot.annotation.Export;
import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.annotation.RegisterProperty;
import godot.api.*;
import godot.core.Vector3;
import godot.global.GD;

import java.util.Random;

@RegisterClass
public class Player extends CharacterBody3D implements ShootComponent, HealthComponent {

    @Export
    @RegisterProperty
    public int maxHealth;
    @RegisterProperty
    public int currentHealth;
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
    @Export
    @RegisterProperty
    public Camera3D cam;
    @RegisterProperty
    public int camIndex = 0;

    SerialPortHandle sph = new SerialPortHandle("COM30");

    @RegisterFunction
    public void _ready(){
        currentHealth = maxHealth;
        Input.setMouseMode(Input.MouseMode.CAPTURED);
    }

    @RegisterFunction
    public void _process(double delta) {
//      keyboard();
        wireless();
        rot = new Vector3(10 * pitch, 1,45 * roll);
        jet.setRotationDegrees(jet.getRotationDegrees().lerp(rot, 0.025));

        shoot(delta);
    }

    @RegisterFunction
    public void _physicsProcess(double delta){
        setVelocity(getBasis().getZ().times(speed)); // apply velocity in the forward direction
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
            Projectile proj_instance = (Projectile) projectile.instantiate();
            proj_instance.tag = "enemy";
            getTree().getCurrentScene().addChild(proj_instance);
            Random rand = new Random();
            float mix = ((rand.nextFloat()*2)-1) * 0.05f;
            Vector3 pos = new Vector3(gunPos.getGlobalPosition().getX() + mix, gunPos.getGlobalPosition().getY() + mix, gunPos.getGlobalPosition().getZ() + mix);
            proj_instance.setGlobalPosition(pos);
            proj_instance.setGlobalRotation(getGlobalRotation());
            timer = 1.0f / fireRate;
        }
    }

    // keyboard control for testing
    public void keyboard(){
        roll = Input.getAxis("left", "right");
        pitch = Input.getAxis("up", "down");
    }
    public void wireless(){
        String msg = sph.readLine();
        if (msg.isEmpty())
            return;
        String cleanMsg = msg.trim();
        String[] inputs = cleanMsg.split("\\s+");

        try {
            float rollInput = Integer.parseInt(inputs[0]) - 230;
            rollInput /= 620;
            rollInput *= 2;
            rollInput -= 1;
            roll = rollInput;
        } catch (NumberFormatException ignored){

        }

        try {
            float pitchInput = Integer.parseInt(inputs[1]) - 350;
            pitchInput /= 330;
            pitchInput *= 2;
            pitchInput -= 1;
            pitch = pitchInput;
        } catch (NumberFormatException ignored) {
        }
    }

    @Override
    public void takeDamage(int amount) {
        currentHealth -= amount;
        if (currentHealth <= 0)
            onDeath();
    }

    @Override
    public void onDeath() {
        getTree().reloadCurrentScene();
    }

}
