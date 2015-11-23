package me.bdaniels.privacytorch;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

public class Torch extends Activity {
    private Camera camera;
    private ToggleButton button;
    private final Context context = this;

    @Override
    protected void onStop() {
        super.onStop();
        if (camera != null) {
            camera.release();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_torch);
        button = (ToggleButton) findViewById(R.id.togglebutton);

        final PackageManager pm = context.getPackageManager();
        if(!isCameraSupported(pm)) {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("No Camera");
            alertDialog.setMessage("The device doesn't support a camera.");
            alertDialog.setButton(RESULT_OK, "OK", new DialogInterface.OnClickListener(){
                        public void onClick(final DialogInterface dialog, final int which) {
                            Log.e("err", "The device doesn't support a camera.");

                        }
                    }
            );
            alertDialog.show();
        }
        camera = Camera.open();
    }
    public void onToggleClicked(View view) {
        PackageManager pm=context.getPackageManager();
        final Parameters p = camera.getParameters();
        if(isFlashSupported(pm)){
            boolean on = ((ToggleButton) view).isChecked();
            if (on) {
                p.setFlashMode(Parameters.FLASH_MODE_TORCH);
                camera.setParameters(p);
                camera.startPreview();
            } else {
                p.setFlashMode(Parameters.FLASH_MODE_OFF);
                camera.setParameters(p);
                camera.stopPreview();}
        } else {
            button.setChecked(false);
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("No Camera Flash");
            alertDialog.setMessage("The device doesn't support flash.");
            alertDialog.setButton(RESULT_OK, "OK", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, final int which) {
                    Log.e("err", "The device doesn't support flash.");
                }
            });
            alertDialog.show();
        }
    }
    private boolean isFlashSupported(PackageManager packageManager) {
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            return true;
        }
        return false;
    }
    private boolean isCameraSupported(PackageManager packageManager) {
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        }
        return false;
    }
}