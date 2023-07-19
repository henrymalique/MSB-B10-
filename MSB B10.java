import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class MainActivity extends WearableActivity {

    private static final int DOUBLE_TAP_TIMEOUT = 300; // Time in milliseconds
    private static final int VIBRATION_DURATION = 200; // Time in milliseconds

    private Vibrator vibrator;
    private GestureDetector gestureDetector;
    private boolean doubleTapDetected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get instance of Vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Initialize GestureDetector
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                // Perform action on double tap
                doubleTapDetected = true;
                vibrate(VIBRATION_DURATION);
                return true;
            }
        });

        // Enable touch events for the view
        setAmbientEnabled();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        } else {
            return super.dispatchTouchEvent(event);
        }
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        // Stop vibrating when entering ambient mode
        stopVibrating();
    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
        // Resume vibrating if double tap was detected before entering ambient mode
        if (doubleTapDetected) {
            vibrate(VIBRATION_DURATION);
        }
    }

    private void vibrate(int duration) {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(duration);
        }
    }

    private void stopVibrating() {
        vibrator.cancel();
        doubleTapDetected = false;
    }
}
