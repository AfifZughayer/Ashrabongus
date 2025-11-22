import godot.annotation.Export;
import godot.annotation.RegisterClass;
import godot.annotation.RegisterFunction;
import godot.annotation.RegisterProperty;
import godot.api.Tween;
import godot.core.Vector3;

import java.util.Random;

@RegisterClass
public class CityEnemy extends Enemy {

    @RegisterProperty
    public float timer;
    @RegisterProperty
    @Export
    public float fireRate;

    @RegisterFunction
    @Override
    public void _process(double delta){
        int distance = (int) Math.floor(getGlobalPosition().distanceTo(player.getGlobalPosition()));
        if (distance <= 10) exit();
        if (distance <= 50 && distance > 10) shoot(delta);
    }

    @RegisterFunction
    @Override
    public void enter(){
        if (tween != null)
            tween.kill();
        tween = createTween();
        tween.setParallel(true);
        tween.setTrans(Tween.TransitionType.QUART);
        tween.setEase(Tween.EaseType.OUT);
        tween.tweenProperty(model, "position", Vector3.Companion.getZERO(), 1.5);
        tween.tweenProperty(model, "rotation_degrees", Vector3.Companion.getZERO(), 1.5);
    }

    @Override
    public void exit(){
        exit = true;
        if (tween != null)
            tween.kill();
        tween = createTween();
        tween.setParallel(true);
        tween.setTrans(Tween.TransitionType.QUART);
        tween.setEase(Tween.EaseType.OUT);
        tween.tweenProperty(model, "position", new Vector3(0, 50, 0), 2);
        tween.tweenProperty(model, "rotation_degrees", new Vector3(-90, 0, 0), 0.5);
    }

    @Override
    public void shoot(double delta) {
        if (timer > 0){
            timer -= delta;
        }
        if (timer <= 0) {
            Projectile proj_instance = (Projectile) projectile.instantiate();
            proj_instance.tag = "player";
            getTree().getCurrentScene().addChild(proj_instance);
            Random rand = new Random();
            float mix = ((rand.nextFloat()*2)-1) * 0.05f;
            Vector3 pos = new Vector3(gunPos.getGlobalPosition().getX() + mix, gunPos.getGlobalPosition().getY() + mix, gunPos.getGlobalPosition().getZ() + mix);
            proj_instance.setGlobalPosition(pos);
            proj_instance.setGlobalRotation(getGlobalRotation());
            timer = 1.0f / fireRate;
        }
    }
}
