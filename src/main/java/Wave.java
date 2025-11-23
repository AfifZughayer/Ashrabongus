import godot.annotation.Export;
import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.annotation.RegisterProperty;
import godot.api.Node;
import godot.api.PackedScene;
import godot.core.Vector3;

@RegisterClass
public abstract class Wave extends Node {

    @Export
    @RegisterProperty
    public PackedScene enemy;
    @RegisterProperty
    public WaveSystem system;

    @RegisterFunction
    public void createEnemy(Vector3 pos){
        Enemy enemy_instance = (Enemy) enemy.instantiate();
        enemy_instance.system = system;
        enemy_instance.setPosition(pos);
        system.registerObserver(enemy_instance);
        getTree().getCurrentScene().addChild(enemy_instance);
    }

    @RegisterFunction
    public abstract void begin();

}
