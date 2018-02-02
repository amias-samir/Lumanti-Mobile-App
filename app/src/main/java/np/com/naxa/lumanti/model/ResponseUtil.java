//package np.com.naxa.lumanti.model;
//
//import android.provider.MediaStore;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.nio.charset.Charset;
//
///**
// * Created on 11/13/17
// * by nishon.tan@gmail.com
// */
//
//public class ResponseUtil {
//
//    public static void saveHttpResponseToFile(HttpResponse response) throws IOException {
//        BufferedReader r = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//
//        StringBuilder total = new StringBuilder();
//
//        String line = null;
//
//        while ((line = r.readLine()) != null) {
//            total.append(line);
//        }
//        r.close();
//
//
//        File file = new File(Collect.FORMS_PATH, "submission-post-request-2-log.txt");
//        MediaStore.Files.write(total, file, Charset.defaultCharset());
//    }
//
//}