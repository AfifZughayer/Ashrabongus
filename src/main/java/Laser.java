import godot.annotation.*;
import godot.api.Area3D;
import godot.api.Node3D;
import godot.core.Signal0;

@RegisterClass
public class Laser extends Area3D implements DamageComponent {

    @RegisterProperty
    @Export
    public int dmg;

    @RegisterProperty
    public double timer = 0;

    @RegisterSignal(parameters = "finished")
    public Signal0 finished = Signal0.create(this, "finished");

    @RegisterFunction
    @Override
    public void _process(double delta){
        timer += delta;
        if (timer >= 5){
            queueFree();
            finished.emit();
        }
    }

    @Override
    public void dealDamage(Node3D body) {
        if (body instanceof HealthComponent && body.isInGroup("player")){
            ((HealthComponent) body).takeDamage(dmg);
        }
    }

    @Override
    public void areaDealDamage(Area3D area) {
        if (area instanceof HealthComponent && area.isInGroup("player")){
            ((HealthComponent) area).takeDamage(dmg);
        }
    }
}
