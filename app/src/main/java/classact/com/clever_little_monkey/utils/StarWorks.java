package classact.com.clever_little_monkey.utils;

import android.app.Activity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.plattysoft.leonids.ParticleSystem;

import javax.inject.Inject;
import javax.inject.Singleton;

import classact.com.clever_little_monkey.R;

/**
 * Created by hcdjeong on 2017/09/18.
 */

@Singleton
public class StarWorks {

    @Inject
    public StarWorks() {
        // Blank Constructor
    }

    public void play(Activity activity, View view) {

        if (activity == null) {
            return;
        }

        ParticleSystem ps1 = new ParticleSystem(activity, 6, R.drawable.star_particle_001_l, 1000);
        ps1.setScaleRange(0.7f, 1.3f);
        ps1.setSpeedRange(0.1f, 0.25f);
        ps1.setRotationSpeedRange(180, 360);
        ps1.setFadeOut(500, new AccelerateInterpolator());
        ps1.oneShot(view, 6);

        ParticleSystem ps2 = new ParticleSystem(activity, 8, R.drawable.star_particle_001_m, 1000);
        ps2.setScaleRange(0.7f, 1.3f);
        ps2.setSpeedRange(0.15f, 0.3f);
        ps2.setRotationSpeedRange(90, 270);
        ps2.setFadeOut(375, new AccelerateInterpolator());
        ps2.oneShot(view, 8);

        ParticleSystem ps3 = new ParticleSystem(activity, 12, R.drawable.star_particle_001_s, 1000);
        ps3.setScaleRange(0.7f, 1.3f);
        ps3.setSpeedRange(0.2f, 0.35f);
        ps3.setRotationSpeedRange(45, 180);
        ps3.setFadeOut(250, new AccelerateInterpolator());
        ps3.oneShot(view, 12);
    }
}
