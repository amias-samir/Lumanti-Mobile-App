package np.com.naxa.lumanti.model;

/**
 * Created by Samir on 6/21/2017.
 */

public class Constant {

    public static String [] DISTRICT_NAMR = {"Rasuwa", "Ktm", "Lalitpur", "Makawanpur"};
    public static String [] HOUSE_DAMAGE = {"Fully damaged", "Partially damaged"};
    public static String [] YES_NO = {"Yes", "No"};
    public static String [] SEX = {"Male", "Female", "Other"};

    public static String URL_DATA_SEND = "http://naxa.com.np/lumanti/ApiController/insertdata";

    public static int countGeneral = 0;
    public static int countDemographic = 0;
    public static int countReconstruction = 0;
    public static int countReconstructionGPS = 0;
    public static int countEarthquakeRelief = 0;
    public static int countSaveSend = 0;


    public static boolean takenimg1 = false, takenimg2 = false, takenimg3 = false, takenimg4 = false;
    public static String takenimg1Name = "";
    public static String takenimg2Name = "";
    public static String takenimg3Name = "";
    public static String takenimg4Name = "";

    public static boolean isFomSavedForm = false ;
    public static String formID = "";

}
