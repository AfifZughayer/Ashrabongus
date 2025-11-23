import godot.annotation.Export;
import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.annotation.RegisterProperty;
import godot.api.Tween;
import godot.core.Callable;
import godot.core.StringNames;
import godot.core.Vector3;

@RegisterClass
public class GodzillaJumpAttack extends Attack{

    @Export
    @RegisterProperty
    public Godzilla godzilla;
    public Tween tween;

    @Override
    public void attack() {
        if (tween != null) tween.kill();
        tween = createTween();
        tween.getFinished().connect(Callable.create(this, StringNames.toGodotName("comeDown")), 0);
        tween.tweenProperty(godzilla, "position", new Vector3(0, 500, 0), 1);
    }

    @RegisterFunction
    public void comeDown(){
        if (tween != null) tween.kill();
        tween = createTween();
        tween.getFinished().connect(Callable.create(this, StringNames.toGodotName("attackFinish")), 0);
        tween.tweenProperty(godzilla, "position", new Vector3(0, 0, 0), 1);
    }

    @RegisterFunction
    @Override
    public void attackFinish() {
        update();
    }
}
