package org.home.geonamesdemo.util;

import android.content.Context;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by Kelvin on 15/11/2017.
 */

public final class Constants {

    public static final String GEONAME_USER_NAME = "kelcervan";
    public static final List<String> TIPOS_LUGARES = Arrays.asList("A", "H", "L", "P", "R", "S", "T", "U", "V");
    public static final String LANG = "es";
    public static final String DEFAULT_RADIO = "10";
    public static final String DEFAULT_ROW = "10";
    public static final String DEFAULT_GEONAME_ROW = "100";
    public static final String GEOGNOS_API = "http://www.geognos.com/api/en/countries/flag/%s.png";
    public static final String GEONAME_KEY = "Geoname";

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
}
