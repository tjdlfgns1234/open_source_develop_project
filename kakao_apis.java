package apis;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

class Place{
    String name = null;
    double lat = 0.0;
    double lon = 0.0;

    public Place(String string) throws UnsupportedEncodingException {
        this.keyword_search(string);
    }

    public WGS84Coordinate get_coordinate(){
        return new WGS84Coordinate(this.lon,this.lat);
    }

    public void keyword_search(String para) throws UnsupportedEncodingException{
        BufferedReader in = null;
        String query = String.format("query=%s", URLEncoder.encode(para, "UTF-8")) ;
        String SERVICE_KEY = "ad06aa9afaeecec43b70cc1b136c041b";
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json?"+query;
        String header = "KakaoAK " + SERVICE_KEY;
        JSONObject response  = null;
        
        try{
            JSONParser jsonParse = new JSONParser();
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection)obj.openConnection();
            
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", header);
            int responseCode = con.getResponseCode();

            if(responseCode==401 || responseCode==400)
            {
                System.out.println("Error");
                return;
            }
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = "";
           
            while((line = in.readLine())!=null){
                sb.append(line);
            }
            response =(JSONObject)jsonParse.parse(sb.toString());
            JSONArray tmp = (JSONArray) response.get("documents");
            JSONObject tmp2 = (JSONObject) tmp.get(0);

            this.name = para;
            this.lon = Double.valueOf(tmp2.get("x").toString());
            this.lat = Double.valueOf(tmp2.get("y").toString());
            
            return;
        }catch(Exception e){
            e.printStackTrace();
            return;
        } 
    }
}