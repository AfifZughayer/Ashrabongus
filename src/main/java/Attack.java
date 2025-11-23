import godot.annotation.RegisterClass;
import godot.annotation.RegisterProperty;
import godot.api.Node;

@RegisterClass
public class Attack extends Node implements AttackComponent, Observer{

    @RegisterProperty
    public Boss boss;

    @Override
    public void attack() {

    }

    @Override
    public void update() {
        if (boss == null) return;
        boss.removeObsever(this);
    }
}
