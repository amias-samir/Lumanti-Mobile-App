package np.com.naxa.lumanti.network.retrofit;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

import static np.com.naxa.lumanti.network.retrofit.UrlClass.URL_DATA_SEND_FILE_UPLOAD;


public interface NetworkApiInterface {
    @FormUrlEncoded
    @POST(URL_DATA_SEND_FILE_UPLOAD)
    Call<UploadResponse> uploadLumantiForm(@Field("data") String data);

}

