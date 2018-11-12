package vector3.mx.skinnerapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
    private GLSurfaceView glSurfaceView;
    private boolean renderSet = false;
    final OpenGLRendererPrincipal renderer = new OpenGLRendererPrincipal(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(this);
        //Agrgar funcionalidad TOUCH !!
        glSurfaceView.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent != null){
                    // Convert touch coordinates into normalized device
                    // coordinates, keeping in mind that Android's Y
                    // coordinates are inverted.
                    final float normalizedX =
                            (motionEvent.getX() / (float) view.getWidth()) * 2  - 1;
                    final float normalizedY =
                            -((motionEvent.getY() / (float) view.getHeight()) * 2  - 1);
                    if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                        glSurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                renderer.handleTouchPress(normalizedX, normalizedY);
                            }
                        });
                    }else if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                        glSurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                renderer.handleTouchDrag(normalizedX, normalizedY);
                            }
                        });
                    }
                    return true;
                }else{
                    return false;
                }
            }
        });
        setContentView(glSurfaceView);
        final ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                && (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")));
        if (supportsEs2) {
// Request an OpenGL ES 2.0 compatible context.
            glSurfaceView.setEGLContextClientVersion(2);
// Assign our renderer.
            glSurfaceView.setRenderer(renderer);
            renderSet = true;
        } else {
            Toast.makeText(this, "This device does not support OpenGL ES 2.0.",
                    Toast.LENGTH_LONG).show();
            return;
        }

    }

    @Override
    protected void onPause(){
        super.onPause();
        if(renderSet){
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(renderSet){
            glSurfaceView.onResume();
        }
    }
}
