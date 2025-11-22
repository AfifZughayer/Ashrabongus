import godot.annotation.Export;
import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.annotation.RegisterProperty;
import godot.api.*;
import godot.core.*;

import java.util.Random;

@RegisterClass
public abstract class Enemy extends CharacterBody3D implements HealthComponent, ShootComponent, Observer{

    @RegisterProperty
    @Export
    public int maxHealth;
    @RegisterProperty
    public int currentHealth = maxHealth;

    @RegisterProperty
    @Export
    public Vector3 startPos = Vector3.Companion.getZERO();
    @RegisterProperty
    @Export
    public Vector3 startRot = Vector3.Companion.getZERO();;

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
    public Boolean exit = false;

    public WaveSystem system;

    @RegisterFunction
    @Override
    public void _ready() {
        model.setPosition(startPos);
        model.setRotationDegrees(startRot);
        player = (Player) getTree().getFirstNodeInGroup("player");
        enter();
    }

    @RegisterFunction
    public abstract void enter();

    public abstract void exit();

    @RegisterFunction
    @Override
    public void _physicsProcess(double delta) {
        if (exit) return;
        lookAt(player.getPosition(), Vector3.Companion.getUP(), true);
        setVelocity(getBasis().getZ().times(10));
        moveAndSlide();
    }

    @RegisterFunction
    @Override
    public void takeDamage(int amount) {
        currentHealth -= amount;
        if (currentHealth <= 0)
            onDeath();
    }

    @RegisterFunction
    @Override
    public void onDeath() {
        update();
        queueFree();
    }

    @Override
    public void update() {
        system.removeObsever(this);
    }

    @RegisterFunction
    public void tempDelete(){
        queueFree();
    }
}
