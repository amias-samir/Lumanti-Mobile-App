package np.com.naxa.lumanti.network.retrofit;


import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.github.simonpercic.oklog3.OkLogInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by user on 6/6/2017.
 */

public class NetworkApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getAPIClient() {

        if (retrofit == null) {
            OkLogInterceptor okLogInterceptor = OkLogInterceptor.builder().build();
            OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();

            okHttpBuilder.connectTimeout(2, TimeUnit.MINUTES);
            okHttpBuilder.readTimeout(2, TimeUnit.MINUTES);
            okHttpBuilder.writeTimeout(2,TimeUnit.MINUTES);
            okHttpBuilder.addInterceptor(okLogInterceptor);
            okHttpBuilder.addNetworkInterceptor(new StethoInterceptor());

            OkHttpClient okHttpClient = okHttpBuilder.build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(UrlClass.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;

    }


}
