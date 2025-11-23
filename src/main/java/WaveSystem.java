import godot.annotation.Export;
import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.annotation.RegisterProperty;
import godot.api.Node;
import godot.api.Node3D;
import godot.core.VariantArray;
import godot.global.GD;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

@RegisterClass
public class WaveSystem extends Node3D implements Runnable, Subject {

    public ArrayList<Enemy> currentEnemies = new ArrayList<>();
    @RegisterProperty
    public VariantArray<Node> waves = new VariantArray<>(Object.class);
    public int waveIndex = 0;

    public Thread t;
    public Semaphore semaphore = new Semaphore(0);

    @RegisterFunction
    @Override
    public void _ready(){
        waves = getChildren();
        for (Node w : waves){
            ((Wave) w).system = this;
        }
        t = new Thread(this);
        t.start();
    }


    @Override
    public void run() {

        while(waveIndex < waves.size()){
            callDeferred("start_wave");
            try {
                semaphore.acquire();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            waveIndex += 1;
        }

    }

    @RegisterFunction
    public void startWave(){
        ((Wave)waves.get(waveIndex)).begin();
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
