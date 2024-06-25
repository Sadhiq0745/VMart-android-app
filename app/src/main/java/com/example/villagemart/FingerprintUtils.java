
package com.example.villagemart;

import android.content.Context;
import androidx.biometric.BiometricManager;
import androidx.core.content.ContextCompat;
import android.widget.Toast;

public class FingerprintUtils {
    public static boolean isFingerprintAvailable(Context context) {
        BiometricManager biometricManager = BiometricManager.from(context);
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                return true;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(context, "No biometric features available on this device.", Toast.LENGTH_SHORT).show();
                return false;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(context, "Biometric features are currently unavailable.", Toast.LENGTH_SHORT).show();
                return false;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(context, "The user hasn't associated any biometric credentials with their account.", Toast.LENGTH_SHORT).show();
                return false;
            default:
                return false;
        }
    }
}
