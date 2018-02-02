package np.com.naxa.lumanti.network.retrofit;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface NetworkApiInterface {
    @FormUrlEncoded
    @POST("smartapi/register")
    Call<UploadResponse> uploadLumantiForm(@Field("data") String data);

}

