package a.m.restaurant_automation;

import java.io.IOException;

import a.m.restaurant_automation.model.AppStaticData;
import a.m.restaurant_automation.repository.UserSession;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;
    private static final String BASE_URL = AppStaticData.BASE_URL;
    static UserSession session;
    static String token;


    public static Retrofit getRetrofitInstance(){

        session = UserSession.getInstance();
        token=session.getToken();


        OkHttpClient client =new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder ongoing = chain.request().newBuilder();
                        if (token != ""){
                            ongoing.addHeader("Authorization","Bearer"+token);
                        }
                        return chain.proceed(ongoing.build());
                    }
                }).build();

        if (retrofit== null){

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

}
