import godot.annotation.Export;
import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.annotation.RegisterProperty;
import godot.api.Node;
import godot.api.Node3D;
import godot.core.VariantArray;

import java.util.concurrent.Semaphore;

@RegisterClass
public abstract class Boss extends Node3D implements HealthComponent, Runnable, Subject{

    @Export
    @RegisterProperty
    public VariantArray<Node> attacks = new VariantArray<>(Object.class);
    @RegisterProperty
    public Attack currentAttack;

    @Export
    @RegisterProperty
    public int maxHealth;
    @RegisterProperty
    public int currentHealth;

    public Thread t;
    public Semaphore semaphore = new Semaphore(0);

    @Override
    public void _ready() {
        currentHealth = maxHealth;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        while (true){
            callDeferred("begin_attack");
            try {
                semaphore.acquire();
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @RegisterFunction
    public void beginAttack(){
        setAttack();
        currentAttack.attack();
    }

    @RegisterFunction
    public void setAttack(){
        int randIndex = (int)Math.floor(Math.random() * attacks.size());
        currentAttack = (Attack)attacks.get(randIndex);
        registerObserver((Observer) currentAttack);
    }

    @Override
    public void takeDamage(int amount){
        currentHealth -= amount;
        if (currentHealth <= 0){
            onDeath();
        }
    }

    @Override
    public void registerObserver(Observer o) {
        ((Attack) o).boss = this;
    }

    @Override
    public void removeObsever(Observer o) {
        semaphore.release();
    }
}
