import godot.annotation.Export;
import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.annotation.RegisterProperty;
import godot.api.Marker3D;
import godot.api.Node3D;
import godot.api.PackedScene;

@RegisterClass
public class Enemy extends HealthComponent implements MechanicShoot {

    @Export
    @RegisterProperty
    public PackedScene projectile;

    @Export
    @RegisterProperty
    public Marker3D gunPos;

    @Export
    @RegisterProperty
    public float fireRate = 1.0f;

    private double timer = 0;

    @Export
    @RegisterProperty
    public float speed = 2.0f;

    @RegisterFunction
    public void _ready() {

    }

    @RegisterFunction
    public void _process(double delta) {
        shoot(delta);
    }

    @Override
    public void shoot(double delta) {
        if (projectile == null || gunPos == null) return;

        timer -= delta;
        if (timer <= 0) {
            Node3D projInstance = (Node3D) projectile.instantiate();
            getTree().getCurrentScene().addChild(projInstance);

            projInstance.setGlobalPosition(gunPos.getGlobalPosition());
            projInstance.setGlobalRotation(gunPos.getGlobalRotation());

            timer = 1.0 / fireRate;
        }
    }
}
