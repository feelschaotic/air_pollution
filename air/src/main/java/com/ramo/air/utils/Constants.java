package com.ramo.air.utils;

public class Constants {
    // 高德云图地址 污染企业
    public static final String MAPURL = "http://yuntu.amap.com/share/JRZZbm";
    // 高德云图地址 投诉地图
    public static final String REPORTMAPURL = "http://yuntu.amap.com/share/mYJVNz";
    public static final String REPORTMAPTABLEID = "5691c4c87bbf196a67174b6d";

    // 聚合数据 空气质量检测api
    public static final String JUHE_AQIAPI = "http://web.juhe.cn:8080/environment/air/cityair";
    //空气含量api
    public static final String JUHE_AQIPM_API = "http://web.juhe.cn:8080/environment/air/pm";
    //聚合数据 反向地理编码
    //public static final String JUHE_GEOAPI = "http://apis.juhe.cn/geo/";
    //百度地图 反向地理编码

    public static final String BAI_DU_GEOAPI = "http://api.map.baidu.com/geocoder/v2/";
    public static final String BAI_DU_GEO_AK = "W7s4GIdeRkfbGi3099S8MsXSkOzyInN8";
    //全国空气排行
    public static final String CITY_AQIRANK_API = "http://weather.gtimg.cn/aqi/cityrank.json?callback=cityrank&_=1437026960327";
    /*
	 * 例如
	 * cityrank([{  
        "pm2_5": 13,  
        "primary_pollutant": "",  
        "level": "一级",  
        "co": 0.435,  
        "pm10": 17,  
        "area": "舟山",  
        "o3_8h": 32,  
        "o3": 45,  
        "o3_24h": 119,  
        "quality": "优",  
        "co_24h": 0.497,  
        "no2_24h": 17,  
        "so2": 6,  
        "so2_24h": 10,  
        "time_point": "2015-07-16T12:00:00Z",  
        "pm2_5_24h": 18,  
        "id": "01013411",  
        "o3_8h_24h": 93,  
        "aqi": 19,  
        "pm10_24h": 23,  
        "no2": 10  
    }])
	 */

    // 高德 污染地图
    public static final String AIR_POLLUTION_MAPAPI = "http://weather.webmasterhome.cn/aqi/aqimap.asp";


    // SharedPreferences文件名
    public static final String FILENAME = "air_pollution";

    //身份认证api（阿凡达数据）
    public static final String VERIFY_IDCARD_API = "http://api.avatardata.cn/IdCardCertificate/Verify" +
            "?key=9597d676f41b4d8a89fe7c5ec0d95858";
    //备用的身份认证api
    public static final String VERIFY_IDCARD_API2 = "http://api.avatardata.cn/IdCardCertificate/Verify" +
            "?key=29fe756590484fd19f499100ad261e9d";
}
