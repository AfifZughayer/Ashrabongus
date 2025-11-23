import godot.annotation.Export;
import godot.annotation.RegisterClass;
import godot.annotation.RegisterProperty;
import godot.api.PackedScene;
import godot.core.Vector3;

@RegisterClass
public class CityWave4 extends Wave{

    @Export
    @RegisterProperty
    public PackedScene city_enemy;
    @Export
    @RegisterProperty
    public PackedScene missile_enemy;

    @Override
    public void begin() {
        createEnemy(city_enemy, new Vector3(-35, 0, 75));
        createEnemy(missile_enemy, new Vector3(0, 0, 75));
        createEnemy(city_enemy, new Vector3(35, 0, 75));
    }
}
