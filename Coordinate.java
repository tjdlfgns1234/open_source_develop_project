package apis;

import java.lang.Math;


class TMCoordinateOrigin {
    final int  WESTERN = 1;
    final int  CENTRAL = 2;
    final int  EASTERN = 3;
    final int  EAST_SEA = 4;
    final int  JEJU = 5;

    public int origin;

    public TMCoordinateOrigin(){
        this.origin = CENTRAL;
    }

    public void origin_change(int num){
        this.origin = num;
    }
    public int get_origin_latitude(){
        return 38;
    }
    public int get_origin_longitude(){
        if (this.origin == WESTERN)
            return 125;
        else if(this.origin == EASTERN)
            return 129;
        else if(this.origin == EAST_SEA)
            return 131;
        else
            return 127;
    }

}
class TMCoordinateType{
    final int BESSEL1841 = 1;
    final int GRS80 = 2;
    final int OLD_BESSEL1841 = 3;
    final int OLD_GRS80 = 4;

    public int type;


    public TMCoordinateType(){
        this.type = 2;
    }

    public void type_init_(int num){
        this.type = num;
    }

    public double get_semi_major_axis(){
        if (this.type == BESSEL1841 || this.type ==OLD_BESSEL1841)
            return 6377397.155;
        else
            return 6378137;
    }
    public double get_flattening(){
        if (this.type == BESSEL1841 || this.type ==OLD_BESSEL1841)
            return  1 / 299.1528128;
        else
            return  1 / 298.257222101;
    }
    public double get_semi_minor_axis(){
        return this.get_semi_major_axis() * (1 - this.get_flattening());
    }
    public double get_scale_coefficient(){
        return 1.0;
    }
    public double get_dx(int origin){
        TMCoordinateOrigin ex = new TMCoordinateOrigin();
        if (this.type == BESSEL1841 || this.type ==GRS80)
            return  600000;
        else
            if (origin == ex.JEJU)
                return 550000;
            else
                return 500000;
    }
    public double get_dy(){
       return 200000;
    }

}


class TMCoordinate{
    double x = 0;
    double y = 0;
    TMCoordinateOrigin origin = new TMCoordinateOrigin();
    TMCoordinateType type = new TMCoordinateType();

    public TMCoordinate(){
        // Constructor
    }
    public void _init_(double a, double b, TMCoordinateOrigin origin,TMCoordinateType type){
        this.x = a;
        this.y = b;
        this.origin = origin;
        this.type = type;
    }

}

class KMACoordinate{
    double x = 0.0;
    double y = 0.0;

    public KMACoordinate(double a,double b){
        this.x = a;
        this.y = b;
    }
}



class WGS84Coordinate {
    double longitude = 0.0;
    double latitude = 0.0;

    public WGS84Coordinate(double a,double b){
        this.longitude = a;
        this.latitude = b;
    }

    public TMCoordinate to_tmcoord(TMCoordinateOrigin origin, TMCoordinateType type){
        double semi_major = type.get_semi_major_axis();
        double semi_minor = type.get_semi_minor_axis();

        double lon_rad = Math.toRadians(this.longitude);
        double lat_rad = Math.toRadians(this.latitude);

        double org_lon = origin.get_origin_longitude();
        double org_lat = origin.get_origin_latitude();

        double org_lon_rad = Math.toRadians(org_lon);
        double org_lat_rad = Math.toRadians(org_lat);


        double e2 = (Math.pow(semi_major, 2) - Math.pow(semi_minor, 2)) / Math.pow(semi_major, 2);
        double  e2p = (Math.pow(semi_major, 2) - Math.pow(semi_minor, 2)) / Math.pow(semi_minor, 2);

        double  t = Math.pow(Math.tan(lat_rad), 2);
        double  c = e2 / (1 - e2) * Math.pow(Math.cos(lat_rad), 2);
        double    a = (lon_rad - org_lon_rad) * Math.cos(lat_rad);
        double   n = semi_major / Math.sqrt(1 - e2 * Math.pow(Math.sin(lat_rad), 2));
        double    m = semi_major * ((1 - e2 / 4 - 3 * Math.pow(e2, 2) / 64 - 5 * Math.pow(e2, 3) / 256) * lat_rad
                - (3 * e2 / 8 + 3 * Math.pow(e2, 2) / 32 + 45 * Math.pow(e2, 3) / 1024) * Math.sin(
                2 * lat_rad)
                + (15 * Math.pow(e2, 2) / 256 + 45 * Math.pow(e2, 3) / 1024) * Math.sin(4 * lat_rad)
                - (35 * Math.pow(e2, 3) / 3072) * Math.sin(6 * lat_rad));
        double   m0 = semi_major * ((1 - e2 / 4 - 3 * Math.pow(e2, 2) / 64 - 5 * Math.pow(e2, 3) / 256) * org_lat_rad
                - (3 * e2 / 8 + 3 * Math.pow(e2, 2) / 32 + 45 * Math.pow(e2, 3) / 1024) * Math.sin(
                2 * org_lat_rad)
                + (15 * Math.pow(e2, 2) / 256 + 45 * Math.pow(e2, 3) / 1024) * Math.sin(4 * org_lat_rad)
                - (35 * Math.pow(e2, 3) / 3072) * Math.sin(6 * org_lat_rad));

        double  x = type.get_dx(origin.origin) + type.get_scale_coefficient() * (m - m0 + n * Math.tan(lat_rad) * (
                Math.pow(a, 2) / 2 + Math.pow(a, 4) / 24 * (5 - t + 9 * c + 4 * Math.pow(c, 2)) + Math.pow(a,
                        6) / 720 * (
                        61 - 58 * t + Math.pow(t, 2) + 600 * c - 330 * e2p)));
        double  y = type.get_dy() + type.get_scale_coefficient() * n * (
                a + Math.pow(a, 3) / 6 * (1 - t + c) + Math.pow(a, 5) / 120 * (
                        5 - 18 * t + Math.pow(t, 2) + 72 * c - 58 * e2p));
        TMCoordinate result =new TMCoordinate();
        result._init_(x, y, origin, type);

        return result;
    }
    public KMACoordinate to_kmacoord(){
        double grid = 5.0;
        double xo = 210 / grid;
        double yo = 675 / grid;

        double re = 6371.00877 / grid;
        double slat1 = Math.toRadians(30.0);
        double slat2 = Math.toRadians(60.0);
        double olon = Math.toRadians(126.0);
        double olat = Math.toRadians(38.0);

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);

        double  ra = Math.tan(Math.PI * 0.25 + Math.toRadians(this.latitude) * 0.5);
        ra = re * sf / Math.pow(ra, sn);
        double  theta = Math.toRadians(this.longitude) - olon;
        if (theta > Math.PI)
             theta -= 2.0 * Math.PI;
        if (theta < -Math.PI)
              theta += 2.0 * Math.PI;
        theta *= sn;
        double x = (int)(ra * Math.sin(theta) + xo + 1.5);
        double y = (int)(ro - ra * Math.cos(theta) + yo + 1.5);
        KMACoordinate result = new KMACoordinate(x,y);

        return result;
    }

}