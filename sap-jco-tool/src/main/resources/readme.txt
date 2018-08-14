1，双击bat运行程序
2，程序默认访问：http://localhost:8889/jco
3，功能api：http://localhost:8889/jco/swagger-ui.html
4，需要确认sapjco3.dll和jar在同级目录，或者拷贝至classpath

注
1）若部署在linux下，则需要将dll替换成对应位数的so
2）可以将运行文件发送到桌面快捷方式
3）注意打开防火墙对应的端口
4）若需要修改端口和程序别名，修改resources下的application.properties
5）MES.jcoDestination是系统自动生成的，无需修改。发起端传参后，系统自动生成对应的MES.jcoDestination

当前destination为每次请求每次创建，效率不是很高，后期可扩展动态配置并放入缓存。