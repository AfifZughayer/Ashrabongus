import godot.annotation.Export;
import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.annotation.RegisterProperty;
import godot.api.CharacterBody3D;
import godot.api.Node3D;
import godot.api.Tween;
import godot.core.Vector3;

@RegisterClass
public class Enemy extends CharacterBody3D implements HealthComponent {

    @RegisterProperty
    @Export
    public int maxHealth;
    @RegisterProperty
    public int currentHealth;

    @RegisterProperty
    @Export
    public Node3D model;
    @RegisterProperty
    public Tween tween;
    @RegisterProperty
    public Player player;
    @RegisterProperty
    public Vector3 dir;

    @RegisterFunction
    @Override
    public void _ready() {
        currentHealth = maxHealth;
        model.setPosition(new Vector3(0, 100, 0));
        model.setRotationDegrees(new Vector3(180, 0, 0));
        player = (Player) getTree().getFirstNodeInGroup("player");
        lookAt(player.getPosition());
        setRotation(new Vector3(getRotation().getX(), getRotation().getY() + Math.PI, getRotation().getZ()));
        enter();
    }

    @RegisterFunction
    @Override
    public void _physicsProcess(double delta) {
        lookAt(player.getPosition());
        setRotation(new Vector3(getRotation().getX(), getRotation().getY() + Math.PI, getRotation().getZ()));
        setVelocity(getBasis().getZ().times(10));
        moveAndSlide();
    }

    @RegisterFunction
    public void enter(){
        tween = createTween();
        tween.setParallel(true);
        tween.tweenProperty(model, "position", new Vector3(0, 0, 0), 1).setTrans(Tween.TransitionType.SINE).setEase(Tween.EaseType.OUT);
        tween.tweenProperty(model, "rotation_degrees", new Vector3(0, 0, 0), 1.25).setTrans(Tween.TransitionType.SINE).setEase(Tween.EaseType.OUT);
    }

    @RegisterFunction
    @Override
    public void takeDamage(int amount) {
        currentHealth -= amount;
    }

    @RegisterFunction
    @Override
    public void onDeath() {

    }
}
