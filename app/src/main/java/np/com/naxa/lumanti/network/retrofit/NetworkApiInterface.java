package np.com.naxa.lumanti.network.retrofit;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import static np.com.naxa.lumanti.network.retrofit.UrlClass.URL_DATA_SEND_FILE_UPLOAD;


public interface NetworkApiInterface {
    @FormUrlEncoded
    @POST(URL_DATA_SEND_FILE_UPLOAD)
    Call<UploadResponse> uploadLumantiForm(@Field("data") String data);


    @Multipart
    @POST(URL_DATA_SEND_FILE_UPLOAD)
    Call<UploadResponse> uploadFormWithImageFile(@Part MultipartBody.Part[] surveyImage,
                                      @Part("data") RequestBody jsonToSend);
}

