import godot.api.Area3D;
import godot.api.Node3D;

public interface DamageComponent {

    void dealDamage(Node3D body);
    void areaDealDamage(Area3D area);

}
