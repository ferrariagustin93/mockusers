import com.google.gson.Gson;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.model.Delay;
import org.mockserver.model.Header;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

import static org.mockserver.model.HttpResponse.response;

import static org.mockserver.model.HttpRequest.request;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;


public class Server {

    static MockServerClient mock = startClientAndServer(8081);

    public static void querypost (String method , String path , int statusCode , String bodyReq , String content , String body , long delay) {

        mock.when(
                request()
                        .withMethod(method)
                        .withPath(path)
                        .withHeader("username","marcos")
                        .withHeader("password","1234")

        ).respond(
                response()
                        .withStatusCode(statusCode)
                        .withHeader(new Header("Content-Type" , content))
                        .withBody("{\"id\":\"123456\",\"username\":\"marcos\",\"token\":\"esuntoken\"}")
                        .withDelay(new Delay(TimeUnit.MILLISECONDS,delay))
        );

    }

    public static void queryget (String method , String path , int statusCode , String bodyReq , String content , String body , long delay) {

        mock.when(
                request()
                        .withMethod(method)
                        .withPath(path)
                        .withHeader("token","esuntoken")
                        .withHeader("id","123456")

        ).respond(
                response()
                        .withStatusCode(statusCode)
                        .withHeader(new Header("Content-Type" , content))
                        .withBody(body)
                        .withDelay(new Delay(TimeUnit.MILLISECONDS,delay))
        );

    }



    public static void main(String[] args) {
        Category[] categories = new Gson().fromJson(getJSON("https://api.mercadolibre.com/sites/MLA/categories"),Category[].class);

        Site[] sites = new Site[6];

        sites[0]=( new Site("MLA","Argentina") );
        sites[1]=( new Site("MLB","Brasil") );
        sites[2]=( new Site("MLC","Chile") );
        sites[3]=( new Site("MBO","Bolivia") );
        sites[4]=( new Site("MLV","Venezuela") );
        sites[5]=( new Site("MCR","Costa Rica") );


       /* for(int i=1; i<=users.length ; i++) {
            query("POST", "/users/" + Integer.toString(i), 200, "","application/json", new Gson().toJson(users[i-1]), 50);
        }*/


        /*for(int i=1; i<=users.length ; i++) {
            query("GET", "/sites" + Integer.toString(i), 200,"" ,"application/json", new Gson().toJson(users[i-1]), 50);
        }*/

        queryget("GET","/sites", 200, "","application/json" ,new Gson().toJson(sites),50);

        queryget("GET","/sites/.*/categories", 200, "","application/json" ,new Gson().toJson(categories),50);


        querypost("POST","/users", 200, "","application/json", "" ,50);


    }

    public static BufferedReader getJSON(String urlGet){
        try {
            URL url = new URL(urlGet);

            try {
                URLConnection urlconnection = url.openConnection();
                urlconnection.setRequestProperty("Accept","application/json");
                urlconnection.setRequestProperty("User-Agent","Mozilla/5.0");
                if(urlconnection instanceof HttpURLConnection) {
                    HttpURLConnection connection = (HttpURLConnection) urlconnection;
                    return  (new BufferedReader(new InputStreamReader(connection.getInputStream())));

                } else {
                    System.out.println("URL inválida");
                    return  (null);
                }


            } catch (IOException exception) {
                System.out.println(exception.getMessage());
            }

        } catch (MalformedURLException exception) {
            System.out.println(exception.getMessage());
        }
        return  (null);
    }



}