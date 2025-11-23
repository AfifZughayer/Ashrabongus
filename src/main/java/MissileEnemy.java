import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.annotation.RegisterProperty;
import godot.api.Tween;
import godot.core.Callable;
import godot.core.StringName;
import godot.core.StringNames;
import godot.core.Vector3;

@RegisterClass
public class MissileEnemy extends Enemy{

    @RegisterProperty
    public double timer = 0;
    Boolean shot = false;

    @RegisterFunction
    public void _process(double delta){
        timer += delta;
        if (timer > 0.5 && !shot) {
            shoot(delta);
            shot = true;
        }
        if (timer > 2)
            exit();
    }

    @Override
    public void enter() {
        if (tween != null)
            tween.kill();
        tween = createTween();
        tween.setParallel(true);
        tween.setTrans(Tween.TransitionType.QUART);
        tween.setEase(Tween.EaseType.OUT);
        tween.tweenProperty(model, "position", Vector3.Companion.getZERO(), 1.5);
        tween.tweenProperty(model, "rotation_degrees", new Vector3(0, -90, 0), 1.5);
    }

    @Override
    public void exit() {
        exit = true;
        if (tween != null)
            tween.kill();
        tween = createTween();
        tween.getFinished().connect(Callable.create(this, StringNames.toGodotName("onExitFinish")), 0);
        tween.setParallel(true);
        tween.setTrans(Tween.TransitionType.QUART);
        tween.setEase(Tween.EaseType.OUT);
        tween.tweenProperty(model, "position", startModelPos, 2);
        tween.tweenProperty(model, "rotation_degrees", startModelRot, 0.5);
    }

    @RegisterFunction
    @Override
    public void onExitFinish() {
        onDeath();
    }

    @Override
    public void shoot(double delta) {
        Projectile proj_instance = (Projectile) projectile.instantiate();
        proj_instance.tag = "player";
        getTree().getCurrentScene().addChild(proj_instance);
        proj_instance.setGlobalPosition(gunPos.getGlobalPosition());
        proj_instance.setGlobalRotation(getGlobalRotation());
    }
}
