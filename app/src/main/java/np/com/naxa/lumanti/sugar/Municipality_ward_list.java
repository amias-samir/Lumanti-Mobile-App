package np.com.naxa.lumanti.sugar;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by Samir on 8/21/2017.
 */

public class Municipality_ward_list extends SugarRecord {
    @Unique
    String  sn ;

    String district_name;
    String municipality_name;
    String current_ward;
    String prev_municipality_name;
    String prev_ward;

    public Municipality_ward_list(){
    }

    public Municipality_ward_list(String sn, String district_name, String municipality_name, String current_ward, String prev_municipality_name, String prev_ward){
        this.sn = sn;
        this.district_name = district_name;
        this.municipality_name = municipality_name;
        this.current_ward = current_ward;
        this.prev_municipality_name = prev_municipality_name;
        this.prev_ward = prev_ward;
    }
}
