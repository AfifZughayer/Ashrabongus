import godot.annotation.Export;
import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.annotation.RegisterProperty;
import godot.api.*;
import godot.core.Vector3;

import java.util.Random;

@RegisterClass
public class Enemy extends CharacterBody3D implements HealthComponent, ShootComponent{

    @RegisterProperty
    @Export
    public int maxHealth;
    @RegisterProperty
    public int currentHealth;

    @RegisterProperty
    @Export
    public Node3D model;
    @RegisterProperty
    public Player player;
    @RegisterProperty
    @Export
    public Marker3D gunPos;
    @RegisterProperty
    @Export
    public PackedScene projectile;

    @RegisterProperty
    public Tween tween;
    @RegisterProperty
    public Vector3 dir;
    @RegisterProperty
    public float timer;
    @RegisterProperty
    @Export
    public float fireRate;
    @RegisterProperty
    public Boolean exit = false;

    @RegisterFunction
    @Override
    public void _ready() {
        currentHealth = maxHealth;
        model.setPosition(new Vector3(0, 50, 0));
        model.setRotationDegrees(new Vector3(90, 0, 0));
        player = (Player) getTree().getFirstNodeInGroup("player");
        lookAt(player.getPosition());
        setRotation(new Vector3(getRotation().getX(), getRotation().getY() + Math.PI, getRotation().getZ()));
        enter();
    }

    @RegisterFunction
    @Override
    public void _process(double delta){
        int distance = (int) Math.floor(getGlobalPosition().distanceTo(player.getGlobalPosition()));
        if (distance <= 10) exit();
        if (distance <= 50 && distance > 10) shoot(delta);
    }

    @RegisterFunction
    @Override
    public void _physicsProcess(double delta) {
        if (exit) return;
        lookAt(player.getPosition(), Vector3.Companion.getUP(), true);
        setVelocity(getBasis().getZ().times(10));
        moveAndSlide();
    }

    @RegisterFunction
    public void enter(){
        if (tween != null)
            tween.kill();
        tween = createTween();
        tween.setParallel(true);
        tween.setTrans(Tween.TransitionType.QUART);
        tween.setEase(Tween.EaseType.OUT);
        tween.tweenProperty(model, "position", new Vector3(0, 0, 0), 1.5);
        tween.tweenProperty(model, "rotation_degrees", new Vector3(0, 0, 0), 1.5);
    }

    public void exit(){
        exit = true;
        if (tween != null)
            tween.kill();
        tween = createTween();
        tween.setParallel(true);
        tween.setTrans(Tween.TransitionType.QUART);
        tween.setEase(Tween.EaseType.OUT);
        tween.tweenProperty(model, "position", new Vector3(0, 50, 0), 2);
        tween.tweenProperty(model, "rotation_degrees", new Vector3(-90, 0, 0), 0.5);
    }

    @RegisterFunction
    @Override
    public void takeDamage(int amount) {
        queueFree();
    }

    @RegisterFunction
    @Override
    public void onDeath() {
        queueFree();
    }

    @Override
    public void shoot(double delta) {
        if (timer > 0){
            timer -= delta;
        }
        if (timer <= 0) {
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
