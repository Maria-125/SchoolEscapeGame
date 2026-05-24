package io.github.some_example_name.android;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import game.schoolescape.SchoolEscapeGame;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        // Настройки для мобильных устройств
        config.useAccelerometer = false;
        config.useCompass = false;
        config.useImmersiveMode = true;  // полноэкранный режим
        config.numSamples = 2;           // сглаживание

        initialize(new SchoolEscapeGame(), config);
    }
}
