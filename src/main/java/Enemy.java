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
    public int currentHealth;
    @Export
    @RegisterProperty
    public int speed = 20;

    @RegisterProperty
    @Export
    public Vector3 startModelPos = Vector3.Companion.getZERO();
    @RegisterProperty
    @Export
    public Vector3 startModelRot = Vector3.Companion.getZERO();;
    @RegisterProperty
    public Vector3 startPos = Vector3.Companion.getZERO();

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
        currentHealth = maxHealth;
        startPos = getPosition();
        model.setPosition(startModelPos);
        model.setRotationDegrees(startModelRot);
        player = (Player) getTree().getFirstNodeInGroup("player");
        lookAt(player.getGlobalPosition(), Vector3.Companion.getUP(), true);
        enter();
    }

    @RegisterFunction
    public abstract void enter();

    public abstract void exit();

    public abstract void onExitFinish();

    @RegisterFunction
    @Override
    public void _physicsProcess(double delta) {
        if (exit) return;
        lookAt(player.getGlobalPosition(), Vector3.Companion.getUP(), true);
        setVelocity(getBasis().getZ().times(speed));
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
        if (system == null) return;
        system.removeObsever(this);
    }

}
