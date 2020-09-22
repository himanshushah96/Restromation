package a.m.restaurant_automation.model;

import java.util.HashMap;

public class AppStaticData {
    public static final int USERTYPE_CUSTOMER = 1;
    public static final int USERTYPE_MANAGER = 2;
    public static final int USERTYPE_CHEF = 3;
    public static final int USERTYPE_CASHIER = 4;


    public static final HashMap<String, Integer> categories = new HashMap<String, Integer>() {{
        put("Appetizer", 1);
        put("Main Course", 2);
        put("Beverage", 3);
        put("Dessert", 4);
    }};

    public static final HashMap<String, Integer> employeeType = new HashMap<String, Integer>() {{
        put("Manager", 2);
        put("Chef", 3);
        put("Cashier", 4);

    }};



    //public static final String BASE_URL= "http://restroapi-dev.us-west-2.elasticbeanstalk.com/api/";

    //public static final String BASE_URL = "http://192.168.2.86:45455/api/";

    public static final String BASE_URL = "http://192.168.0.173/api/";
    //public static final String BASE_URL = "http://192.168.2.24/api/";

}
