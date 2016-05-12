package utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkUtil {

    public static boolean isWifiConnected() {
        try {
            InetAddress.getByName("www.google.com.br");
            return true;
        } catch (UnknownHostException ex) {
            Logger.getLogger(NetworkUtil.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
