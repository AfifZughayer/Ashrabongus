import godot.annotation.Export;
import godot.annotation.RegisterClass;
import godot.annotation.RegisterProperty;
import godot.api.PackedScene;
import godot.core.Vector3;

@RegisterClass
public class CityWave1 extends Wave{

    @Export
    @RegisterProperty
    public PackedScene enemy;

    @Override
    public void begin() {
        createEnemy(enemy, new Vector3(25, 0, 75));
        createEnemy(enemy, new Vector3(-25, 0, 75));
    }

}
