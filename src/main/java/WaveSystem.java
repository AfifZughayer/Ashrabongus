import godot.annotation.Export;
import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.annotation.RegisterProperty;
import godot.api.Node3D;
import godot.api.PackedScene;
import godot.core.Vector3;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

@RegisterClass
public class WaveSystem extends Node3D implements Runnable, Subject {

    public ArrayList<Enemy> currentEnemies = new ArrayList<>();

    @Export
    @RegisterProperty
    public PackedScene enemy;
    public Thread t;
    public Semaphore semaphore = new Semaphore(0);

    @RegisterFunction
    @Override
    public void _ready(){
        t = new Thread(this);
        t.start();
    }


    @Override
    public void run() {

        while(true){
            callDeferred("create_enemy");
            callDeferred("create_enemy2");
            try {
                semaphore.acquire();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @RegisterFunction
    public void createEnemy(){
        Enemy enemy_instance = (Enemy) enemy.instantiate();
        enemy_instance.system = this;
        enemy_instance.setPosition(new Vector3(0, 0, 75));
        registerObserver(enemy_instance);
        getTree().getCurrentScene().addChild(enemy_instance);
    }
    @RegisterFunction
    public void createEnemy2(){
        Enemy enemy_instance = (Enemy) enemy.instantiate();
        enemy_instance.system = this;
        enemy_instance.setPosition(new Vector3(30, 0, 75));
        registerObserver(enemy_instance);
        getTree().getCurrentScene().addChild(enemy_instance);
    }

    @Override
    public void registerObserver(Observer o) {
        currentEnemies.add((Enemy) o);
    }

    @Override
    public void removeObsever(Observer o) {
        currentEnemies.remove(o);
        if (currentEnemies.isEmpty())
            semaphore.release();
    }
}
