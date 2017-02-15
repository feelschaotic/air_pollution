# air_pollution城市大气污染监控互动app

>说明： 本系统涉及技术:LBS云图、云存储api。异步消息处理、多线程AsyncTask、LruCache图片缓存技术。LocationManager地理定位。XMPP即时通讯协议，Socket框架，aSmart轻量级 开发框架、Mina网络通信应用框架。 Gson， Achartengine，  Highcharts图表库 、Freeline秒级编译工具、MVVM结合MVP架构模式，databinding数据双向绑定框架。  

>用途：在投诉地图的展示上采用高德地图LBS云图进行绘制，云存储api进行上传存储。加载列表的时候采用异步消息处理和多线程AsyncTask提高用户体验。加载图片列表时采用LruCache图片缓存技术。使用LocationManager地理定位并反向地理编码。在推送上，使用XMPP即时通讯协议，Socket框架，aSmart轻量级 开发框架和Mina网络通信应用框架。反序列主要采用Gson，使用安卓图标引擎Achartengine绘制大气走向，采用Highcharts图表库展示投诉报表。项目还采用Freeline秒级编译工具提高开发效率。采用MVVM结合MVP架构模式，databinding数据双向绑定框架降低项目耦合。
  
  
本app主要实现以下功能：   
一、	展示用户所在城市的空气指数和其他污染物的浓度。显示的数据包括PM2.5、PM10、O3、二氧化氮以及二氧化硫。  
二、	30天内指标走向、全国空气质量排名、周边污染源和监测站数据的查阅和分享。  
三、	提供生活助手，给人们出行建议和防护措施。  
四、	用户可以提交污染投诉、快捷搜索投诉，关注投诉。   
五、	清除缓存，计算缓存  
六、	全国城市空气质量订阅和管理。  
七、	后台核实投诉情况，受理或者退回投诉。  
八、	互动小游戏，提高用户黏度和民众参与感：《吹走雾霾》、《扫一扫》：扫街景查询街道空气质量、《计步器》。  
九、	环保法规展示，科普环保知识。   

*注：该项目为练手项目，原先在eclipse环境下开发，本次属于二次开发，迁移到AS环境，并采用MVVM架构，所以有部分Activity因时间关系未分解完成*
![](https://github.com/feelschaotic/air_pollution/blob/master/video/v1_begin.gif)  
![](https://github.com/feelschaotic/air_pollution/blob/master/video/v2_report.gif)  
![](https://github.com/feelschaotic/air_pollution/blob/master/video/v3_push.gif)  
![](https://github.com/feelschaotic/air_pollution/blob/master/video/v4_person_center.gif)  
![](https://github.com/feelschaotic/air_pollution/blob/master/video/v6_scan_game.gif)  

