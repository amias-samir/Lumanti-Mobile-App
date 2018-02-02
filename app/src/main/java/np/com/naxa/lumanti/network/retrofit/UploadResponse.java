package np.com.naxa.lumanti.network.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 02/02/2018
 * by samir
 */

public class UploadResponse {


    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private String data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
