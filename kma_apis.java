// package apis;

// import java.math.*;
// import java.util.Date;
// import Volley;
// import java.net.HttpURLConnection;
// import java.net.URL;

// class SkyType{
//     int code;

//     public SkyType(int a){
//         this.code = a;
//     }

//     public String GetStr(){
//         if(this.code == 1)
//             return "맑음";
//         else if(this.code == 3)
//             return "구름많음";
//         else if(this.code == 4)
//             return "흐림";
//         else 
//             return "잘못된 데이터 입니다.";
//     }
// }

// class PrecipitationType{
//     int code;

//     public PrecipitationType(int a){
//         this.code = a;
//     }
    
//     public String __Str__(){
//         if(this.code == 0)
//             return "없음";
//         else if (this.code == 1)
//             return "비";
//         else if (this.code == 2)
//             return "비/눈";
//         else if (this.code == 3)
//             return "눈";
//         else if  (this.code == 4)
//             return "소나기";
//         else
//             return "잘못된 데이터 입니다.";
//     }
// }

// class WindDirection{
//     int degree;

//     public WindDirection(int a){
//         this.degree = a;
//     }

//     public String __Str__(){
//         int type = (int)Math.round((this.degree + 22.5*0.5)/22.5);

//         if(type == 0 || type == 16){
//             return "북";
//         }
//         else if(type == 1){
//             return "북북동";
//         }
//         else if(type == 2){
//             return "남동";
//         }
//         else if(type == 3){
//             return "동북동";
//         }
//         else if(type == 4){
//             return "동";
//         }
//         else if(type == 5){
//             return "동남동";
//         }
//         else if(type == 6){
//             return "남동";
//         }
//         else if(type == 7){
//             return "남남동";
//         }
//         else if(type == 8){
//             return "남";
//         }
//         else if(type == 9){
//             return "남남서";
//         }
//         else if(type == 10){
//             return "남서";
//         }
//         else if(type == 11){
//             return "서남서";
//         }
//         else if(type == 12){
//             return "서";
//         }
//         else if(type == 13){
//             return "서북서";
//         }
//         else if(type == 14){
//             return "북서";
//         }
//         else if(type == 15){
//             return "북북서";
//         }
//         else 
//             return "잘못된 데이터입니다.";
//     }
// }

// class UltraShortForecast{
//     Date time;
//     int temperature = 0;
//     int precipitation = 0;
//     SkyType sky_type;
//     int humidity = 0;
//     PrecipitationType precipitation_type;
//     WindDirection wind_direction;
//     int wind_speed = 0;
// }

// class TownShortForecast extends UltraShortForecast{
//     int precipitation_probability = 0;
// }

// class get_current_weather{
//     KMACoordinate kma_coord;
//     Date time = new Date();
//     String url ="http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst?";
   
//     try{
//        URL request  = new URL(url);
//        HttpURLConnection con = (HttpURLConnection)request.openConnection();

//        con.setRequestMethod("GET");
//       }
//       catch(Exception e){
//         e.printStackTrace();
//       }
    


//     public get_current_weather(WGS84Coordinate coord){
//         this.kma_coord = coord.to_kmacoord();
//     }
    
    




// }