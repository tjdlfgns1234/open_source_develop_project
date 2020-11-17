package apis;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

class TransitRoute{
    WGS84Coordinate northeast = null;
    WGS84Coordinate southwest = null;
    Place start = null;
    Place end = null;
    String departure_time = null;
    String arrival_time = null;
    long distance = 0;
    String distance_text = null;
    long duration = 0;
    String duration_text = null;
    String polyline = null;
    SubRoute[] subroutes = null;
    int stepsize = 0;

    public void get_subroutes(SubRoute[] subRoute){
        this.subroutes = subRoute;
    }

    public void get_subroute(SubRoute sub, int idx){
        this.subroutes[idx] = sub;
    }

}

class SubRoute{
    WGS84Coordinate start = null;
    WGS84Coordinate end = null;
    long distance = 0;
    String distance_text = null;
    long duration = 0;
    String duration_text = null;
    String travel_mode = null;
    String polyline = null;
    
    /* TransitSubRoute */
    // if travel_mode is "TRANSIT"
    long departure_time = 0;
    long arrival_time = 0;
    String departure_stop = null;
    String arrival_stop = null;
    String vehicle_type = null;
    String line = null;
    long num_stops = 0;

}
class Routes{
    TransitRoute[] route;
    int size = 0;

    public Routes(Place start,Place end) throws UnsupportedEncodingException
    {
        this.GetRoutes(start, end);
    }

    public void GetRoutes(Place start,Place end) throws UnsupportedEncodingException {
        /* getRoutes(start_place,end_place)로 구성하고 원하는 파싱한 경로 값을 반환 할 것 */
        BufferedReader in = null;
        String GOOGLE_KEY = "AIzaSyC-bRZoWZFG1QGH7odSS_4TN_YqbDs65oc";
        String key = String.format("&key=%s",GOOGLE_KEY);
        String origin = String.format("origin=%f,%f",start.lat, start.lon);
        String dest = String.format("&destination=%f,%f",end.lat,end.lon);
        String mode = String.format("&mode=%s",URLEncoder.encode("transit","UTF-8"));
        String language = String.format("&language=%s",URLEncoder.encode("ko","UTF-8"));
        String alt = String.format("&alternatives=%s", URLEncoder.encode("true","UTF-8"));
        //String stime = String.format("&departure_time=%s", URLEncoder.encode(String.valueOf(System.currentTimeMillis()),"UTP-8"));
        //String etime = String.format("&arrival_time=%s", URLEncoder.encode(String.valueOf(System.currentTimeMillis()+3600),"UTP-8"));
        String url = "https://maps.googleapis.com/maps/api/directions/json?"+origin +dest+mode+language+alt+key;
        JSONObject response  = null;
        try{
            JSONParser jsonParse = new JSONParser();
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection)obj.openConnection();
            
            con.setRequestMethod("GET");
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
            JSONArray routes = (JSONArray) response.get("routes");
            this.route = new TransitRoute[routes.size()];
            this.size = routes.size();
            JSONArray legs,steps;
            JSONObject route,leg,bounds,northeast,southwest,time,dist,duration,polyline;
            for(int i = 0 ; i < routes.size();i++)
            {
                this.route[i] =  new TransitRoute();
                this.route[i].start = start;
                this.route[i].start = end;
                route = (JSONObject) routes.get(i);
                bounds = (JSONObject) route.get("bounds");
                northeast =  (JSONObject)bounds.get("northeast");
                southwest =  (JSONObject)bounds.get("southwest");
                this.route[i].northeast = new WGS84Coordinate((double)northeast.get("lng"),(double)northeast.get("lat"));
                this.route[i].southwest = new WGS84Coordinate((double)southwest.get("lng"),(double)southwest.get("lat"));
                
                legs= (JSONArray) route.get("legs");
                leg =(JSONObject) legs.get(0);

                time = (JSONObject) leg.get("arrival_time");
                this.route[i].arrival_time = time.get("text").toString();// text 형식으로 가져옴
                time = (JSONObject) leg.get("departure_time");
                this.route[i].departure_time = time.get("text").toString();// text 형식으로 가져옴
                
                dist = (JSONObject)leg.get("distance");
                this.route[i].distance_text = dist.get("text").toString();
                this.route[i].distance = (long)dist.get("value");
            
                duration = (JSONObject)leg.get("duration");
                this.route[i].duration_text = duration.get("text").toString();
                this.route[i].duration = (long)duration.get("value");
                
                polyline = (JSONObject) route.get("overview_polyline");
                this.route[i].polyline = polyline.get("points").toString();


                steps = (JSONArray) leg.get("steps");
                this.route[i].get_subroutes(new SubRoute[steps.size()]);
                this.route[i].stepsize = steps.size();
                for(int k = 0; k < steps.size();k++)
                {
                    this.route[i].get_subroute(new SubRoute(), k);
                    JSONObject step = (JSONObject) steps.get(k);
                    String travel_mode = step.get("travel_mode").toString();
                    JSONObject jobj = (JSONObject) step.get("start_location");
                    
                    this.route[i].subroutes[k].start = new WGS84Coordinate((double)jobj.get("lng"),(double)jobj.get("lat"));
                    jobj = (JSONObject) step.get("end_location");
                    this.route[i].subroutes[k].end = new WGS84Coordinate((double)jobj.get("lng"),(double)jobj.get("lat"));
                    jobj = (JSONObject) step.get("distance");
                    this.route[i].subroutes[k].distance = (long)jobj.get("value");
                    this.route[i].subroutes[k].distance_text = jobj.get("text").toString();
                    jobj = (JSONObject) step.get("polyline");
                    this.route[i].subroutes[k].polyline = jobj.get("points").toString();

                    jobj = (JSONObject) step.get("duration");
                    this.route[i].subroutes[k].duration = (long)jobj.get("value");
                    this.route[i].subroutes[k].duration_text = jobj.get("text").toString();
                    this.route[i].subroutes[k].travel_mode = travel_mode;
                    
                    if ("TRANSIT".equals(travel_mode))
                    {
                        jobj = (JSONObject) step.get("transit_details");
                        this.route[i].subroutes[k].num_stops = (long)jobj.get("num_stops");
                        time = (JSONObject) jobj.get("departure_time");
                        this.route[i].subroutes[k].departure_time = (long)time.get("value");
                        time = (JSONObject) jobj.get("arrival_time");
                        this.route[i].subroutes[k].arrival_time = (long)time.get("value");
                        JSONObject stop = (JSONObject) jobj.get("departure_stop");
                        this.route[i].subroutes[k].departure_stop = stop.get("name").toString();
                        stop = (JSONObject) jobj.get("arrival_stop");
                        this.route[i].subroutes[k].arrival_stop = stop.get("name").toString();
                        JSONObject lines = (JSONObject) jobj.get("line");
                        jobj = (JSONObject) lines.get("vehicle");
                        this.route[i].subroutes[k].vehicle_type = jobj.get("type").toString();
                        this.route[i].subroutes[k].line = lines.get("short_name").toString();

                    }

            }
        }
            
            
            
        }catch(Exception e){
            e.printStackTrace();
            return;
        } 
    }   
}