package constants;

public class Constants {

    public class SocketConstants {

        public static final String ADDRESS_IP = "localhost"; // TODO - Replace this ip
        public static final int PORT = 5566;
    }

    public class SaveFile {

        public static final String PATH_ON_DISK = "C:\\chat\\";
    }

    public class UrlHttp {

        public static final String LOGIN_URL = "http://" + SocketConstants.ADDRESS_IP + ":8080/chat/login.jsp";
        public static final String REGISTER_URL = "http://" + SocketConstants.ADDRESS_IP + ":8080/chat/signIn.jsp";
    }
}