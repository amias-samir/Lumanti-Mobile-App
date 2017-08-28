package np.com.naxa.lumanti.sugar;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by Samir on 8/28/2017.
 */

public class NissaNo_Details extends SugarRecord {
    @Unique
    String  sn ;

    public String name_of_househead;
    public String district;
    public String prev_VDC_Mun;
    public String current_ward_no;
    public String prev_ward_no;
    public String tole;
    public String nissa_no;
    public String pa_no;
    public String citizenship_no;

    public NissaNo_Details(){
    }

    public NissaNo_Details(String sn, String name_of_househead, String district, String prev_VDC_Mun, String current_ward_no,
                           String prev_ward_no, String tole, String nissa_no, String pa_no, String citizenship_no){
        this.sn = sn;
        this.name_of_househead = name_of_househead;
        this.district = district;
        this.prev_VDC_Mun = prev_VDC_Mun;
        this.current_ward_no = current_ward_no;
        this.prev_ward_no = prev_ward_no;
        this.tole = tole;
        this.nissa_no = nissa_no;
        this.pa_no = pa_no;
        this.citizenship_no = citizenship_no;
    }
}