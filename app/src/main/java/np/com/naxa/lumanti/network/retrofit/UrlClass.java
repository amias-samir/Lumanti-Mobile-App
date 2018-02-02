package np.com.naxa.lumanti.network.retrofit;

/**
 * Created by Samir on 9/9/2017.
 */

public class UrlClass {

    public static final String REQUEST_OK = "200";
    public static final String REQUEST_401 = "401";
    public static final String REQUEST_400 = "400";

    public static final String BASE_URL = "http://naxa.com.np/lumanti/";
    public final static String URL_DATA_SEND ="ApiController/inserrtdata";
    public final static String URL_DATA_SEND_FILE_UPLOAD ="ApiController/enter_record";


//    insertdata



    public static boolean isInvalidResponse(String responseCode) {
        return !responseCode.equals(UrlClass.REQUEST_OK);
    }
}
