1，本程序只能运行在32位jre下
2，正常以微服jar包运行，端口及项目配置在resources下的application.properties中修改
3，运行jar包，参考运行程序说明
4，需要确认JCustomOpc.dll和jar在同级目录，或者拷贝至classpath

开发须知：
javafish.clients.opc包下的文件为jeasyopc组件的源码，通过jni调用dll，只能运行于windows环境，jre为32位。
若需修改，可对JCustomerOpc.java进行调整
