package com.mvs.camera.util;

import com.mvs.camera.pojo.ResultBean;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * HTTP请求代理类
 *
 * @author frin
 */
public class HttpRequestProxy {

    public static final Logger LOGGER = Logger.getLogger(HttpRequestProxy.class);

    /**
     * 连接超时
     */
    private int connectTimeOut = 500;
    /**
     * 传输请求连接超时
     */
    private int transferTimeOut = 30000;

    /**
     * 读取数据超时
     */
    private int readTimeOut = 20000;    //（单位[dan wei]：毫秒）jdk

    /**
     * 请求编码
     */
    private String requestEncoding = "UTF-8";

    /**
     * 获取数据编码
     */
    private String reponseEncoding = "UTF-8";/**
     * <pre>
    *
    * 发送带参数的GET的HTTP请求
    * </pre>
    *
    * @param reqUrl     HTTP请求URL
    * @param parameters 参数映射表
    * @return HTTP响应的字符串
    */
   public String doGet(String reqUrl, Map<String, String> parameters, String recvEncoding, int cto, int rto) {
       HttpURLConnection url_con = null;
       String responseContent = null;
       try {
           StringBuffer params = new StringBuffer();
           for (Iterator<Entry<String, String>> iter = parameters.entrySet().iterator(); iter.hasNext(); ) {
               Entry<String, String> element = iter.next();
               params.append(element.getKey().toString());
               params.append("=");
               params.append(URLEncoder.encode(element.getValue().toString(), this.requestEncoding));
               params.append("&");
           }

           if (params.length() > 0) {
               params = params.deleteCharAt(params.length() - 1);
           }

           URL url = new URL(reqUrl);
           url_con = (HttpURLConnection) url.openConnection();
           url_con.setRequestMethod("GET");
           //主机连接超时
           //url_con.setConnectTimeout(this.connectTimeOut);
           //数据读取超时
           //url_con.setReadTimeout(this.readTimeOut);
           url_con.setConnectTimeout(cto);
           url_con.setReadTimeout(rto);
           url_con.setDoOutput(true);

           byte[] b = params.toString().getBytes("UTF-8");
           url_con.getOutputStream().write(b, 0, b.length);
           url_con.getOutputStream().flush();
           url_con.getOutputStream().close();

           InputStream in = url_con.getInputStream();
           BufferedReader rd = new BufferedReader(new InputStreamReader(in, recvEncoding));
           String tempLine = rd.readLine();
           StringBuffer temp = new StringBuffer();
           String crlf = System.getProperty("line.separator");
           while (tempLine != null) {
               temp.append(tempLine);
               temp.append(crlf);
               tempLine = rd.readLine();
           }
           responseContent = temp.toString();
           rd.close();
           in.close();
       } catch (IOException e) {
           LOGGER.error("网络故障请求：" + e.getMessage() + "，reqUrl=" + reqUrl);
       } finally {
           if (url_con != null) {
               url_con.disconnect();
           }
       }

       return responseContent;
   }

   /**
    * <pre>
    *
    * 发送不带参数的GET的HTTP请求
    * </pre>
    *
    * @param reqUrl HTTP请求URL
    * @return HTTP响应的字符串
    */
   public String doGet(String reqUrl, String recvEncoding, int cto, int rto) {
       HttpURLConnection url_con = null;
       String responseContent = null;
       try {
           StringBuffer params = new StringBuffer();
           String queryUrl = reqUrl;
           int paramIndex = reqUrl.indexOf("?");

           if (paramIndex > 0) {
               queryUrl = reqUrl.substring(0, paramIndex);
               String parameters = reqUrl.substring(paramIndex + 1, reqUrl.length());
               String[] paramArray = parameters.split("&");
               for (int i = 0; i < paramArray.length; i++) {
                   String string = paramArray[i];
                   int index = string.indexOf("=");
                   if (index > 0) {
                       String parameter = string.substring(0, index);
                       String value = string.substring(index + 1, string.length());
                       params.append(parameter);
                       params.append("=");
                       params.append(URLEncoder.encode(value, this.requestEncoding));
                       params.append("&");
                   }
               }

               params = params.deleteCharAt(params.length() - 1);
           }

           URL url = new URL(queryUrl);
           url_con = (HttpURLConnection) url.openConnection();
           url_con.setRequestMethod("GET");
           //主机连接超时
           //url_con.setConnectTimeout(this.connectTimeOut);
           //数据读取超时
           //url_con.setReadTimeout(this.readTimeOut);
           url_con.setConnectTimeout(cto);
           url_con.setReadTimeout(rto);
           url_con.setDoOutput(true);
           byte[] b = params.toString().getBytes("UTF-8");
           url_con.getOutputStream().write(b, 0, b.length);
           url_con.getOutputStream().flush();
           url_con.getOutputStream().close();
           InputStream in = url_con.getInputStream();
           BufferedReader rd = new BufferedReader(new InputStreamReader(in, recvEncoding));
           String tempLine = rd.readLine();
           StringBuffer temp = new StringBuffer();
           String crlf = System.getProperty("line.separator");
           while (tempLine != null) {
               temp.append(tempLine);
               temp.append(crlf);
               tempLine = rd.readLine();
           }
           responseContent = temp.toString();
           rd.close();
           in.close();
       } catch (IOException e) {
           LOGGER.error("网络故障请求：" + e.getMessage() + "，reqUrl=" + reqUrl);
       } finally {
           if (url_con != null) {
               url_con.disconnect();
           }
       }

       return responseContent;
   }

   public String doGet(String reqUrl) {
       return doGet(reqUrl, reponseEncoding, connectTimeOut, transferTimeOut);
   }
   
   public String doGet(String reqUrl, Map<String, String> parameters) {
	   return doGet(reqUrl,parameters, reponseEncoding, connectTimeOut, transferTimeOut);
   }

   /**
    * <pre>
    *
    * 发送不带参数的GET的HTTP请求
    * </pre>
    *
    * @param reqUrl HTTP请求URL
    * @return HTTP响应的字符串
    */
   public String doGet(String reqUrl, int cto, int rto) {
       HttpURLConnection url_con = null;
       String responseContent = null;
       try {
           URL url = new URL(reqUrl);
           url_con = (HttpURLConnection) url.openConnection();
           url_con.setRequestMethod("GET");
           //主机连接超时
           //url_con.setConnectTimeout(this.connectTimeOut);
           //数据读取超时
           //url_con.setReadTimeout(this.readTimeOut);
           url_con.setConnectTimeout(cto);
           url_con.setReadTimeout(rto);

           InputStream in = url_con.getInputStream();
           BufferedReader rd = new BufferedReader(new InputStreamReader(in, requestEncoding));
           String tempLine = rd.readLine();
           StringBuffer temp = new StringBuffer();
           while (tempLine != null) {
               temp.append(tempLine);
               temp.append(System.getProperty("line.separator"));
               tempLine = rd.readLine();
           }
           responseContent = temp.toString();
           rd.close();
           in.close();
       } catch (IOException e) {
           LOGGER.error("网络故障请求：" + e.getMessage() + "，reqUrl=" + reqUrl);
       } finally {
           if (url_con != null) {
               url_con.disconnect();
           }
       }

       return responseContent;
   }

    /**
     * 检测url是否可连接
     *
     * @param reqUrl HTTP请求URL
     * @param cto    连接时间
     * @param cto    读取时间
     * @return 是否可连接，不能连接则显示原因
     */
    public boolean urlCheckNet(String reqUrl, int cto, int rto) {
        boolean isConnect = false;
        HttpURLConnection urlCon = null;
        try {
            URL url = new URL(reqUrl);
            urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setConnectTimeout(cto);
            urlCon.setReadTimeout(rto);
            urlCon.connect();

            //LOGGER.info(urlCon.getHeaderField(0) + " -- " + urlCon.getResponseMessage() + " -- " + urlCon.getResponseCode());
            if (urlCon.getResponseCode() == 200 || urlCon.getResponseMessage().indexOf("OK ") > 0) {
                isConnect = true;
            }
        } catch (IOException e) {
            LOGGER.error("网络故障请求：" + e.getMessage() + "，reqUrl=" + reqUrl);
        } finally {
            if (urlCon != null) {
                urlCon.disconnect();
            }
        }
        return isConnect;
    }

    /**
     * 检测ip和端口是否可连接
     *
     * @param host
     * @param port
     * @return
     */
    public static boolean isHostConnectable(String host, int port, int timeOut) {
        Socket socket = new Socket();
        try {
            socket.setSoTimeout(timeOut);
            socket.connect(new InetSocketAddress(host, port));
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                LOGGER.error("ip和端口故障请求：" + e.getMessage() + "，host=" + host);
            }
        }
        return true;
    }

    /******************************** 工具方法 结束 ***************************************/

    /**
     * @return 连接超时(毫秒)
     */
    public int getConnectTimeOut() {
        return this.connectTimeOut;
    }

    /**
     * @return 读取数据超时(毫秒)
     */
    public int getReadTimeOut() {
        return this.readTimeOut;
    }

    /**
     * @return 请求编码
     */
    public String getRequestEncoding() {
        return requestEncoding;
    }

    /**
     * @param connectTimeOut 连接超时(毫秒)
     */
    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    /**
     * @param readTimeOut 读取数据超时(毫秒)
     */
    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    /**
     * @param requestEncoding 请求编码
     */
    public void setRequestEncoding(String requestEncoding) {
        this.requestEncoding = requestEncoding;
    }

    public String getReponseEncoding() {
        return reponseEncoding;
    }

    public void setReponseEncoding(String reponseEncoding) {
        this.reponseEncoding = reponseEncoding;
    }
    
    /******************************** 工具方法 开始 ***************************************/

    /**
     * 发送不带参数的GET的HTTP请求
     *
     * @param reqUrl HTTP请求URL
     * @return HTTP响应的字符串
     */
    public ResultBean doImageGet(String reqUrl,String imageType,int qualityType, int cto, int rto) {
        HttpURLConnection urlCon = null;
        ResultBean result = null;
        try {
            URL url = new URL(reqUrl);
            urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setRequestMethod("GET");
            if(!StringUtils.isEmpty(imageType)) {
	            //添加头信息，返回的image类型
            	urlCon.setRequestProperty("ImageType", imageType);
            }else {
            	imageType = "jpg";
            }
            
            //主机连接超时
            urlCon.setConnectTimeout(this.connectTimeOut);
            //数据读取超时
            urlCon.setReadTimeout(this.readTimeOut);
            urlCon.setConnectTimeout(cto);
            urlCon.setReadTimeout(rto);
            urlCon.setDoInput(true);
            
            if(urlCon.getResponseCode() == 200) {
            	//如果响应为“200”，表示成功响应，则返回一个输入流
                InputStream is = urlCon.getInputStream();
                LOGGER.info("正常返回图片流大小：" + is.available());
                
                String imageName = System.currentTimeMillis() + "." + imageType;

                // 将文件输出流与文件myavatar.jpg关联
                // 这里是输出到工程根目录下
                File file = new File(imageName);
                FileOutputStream fos = new FileOutputStream(file);
                LOGGER.info("图片保存绝对路径： " + file.getAbsolutePath());

                // 将输入流循环写到关联文件的输出流
                // 为了提高效率, 定义缓冲buffer来缓存输入流
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                // 释放资源
                is.close();
                fos.close();
                
                if(qualityType > 1) {
	                float quality = 1f;
	                switch(qualityType) {
		                case 2:
		                	quality = 1f;
			                break;
		                case 3:
		                	quality = 0.75f;
			                break;
		                case 4:
		                	quality = 0.5f;
			                break;
		                case 5:
		                	quality = 0.25f;
			                break;
		                default:
		                	quality = 1f;
		                	break;
	                }
	                
	                LOGGER.info("【启用压缩】缩前大小：" + file.length());
	                // 图片压缩处理
	                Thumbnails.of(file).scale(1f).outputQuality(quality).toFile(file);
	                LOGGER.info("【启用压缩】压缩后大小：" + file.length());
                }
                
	            result = new ResultBean(true, imageName);
            }else {
            	result = new ResultBean(false, "无法获取照片，请检查路径：" + reqUrl);
            }
        } catch (IOException e) {
        	result = new ResultBean(false, "获取照片出错：" + e.getMessage());
            LOGGER.error("网络故障请求：" + e.getMessage() + "，reqUrl=" + reqUrl);
        } finally {
            if (urlCon != null) {
                urlCon.disconnect();
            }
        }

        return result;
    }

    public ResultBean doImageGet(String reqUrl,String imageType,int qualityType) {
        return doImageGet(reqUrl, imageType,qualityType,connectTimeOut, transferTimeOut);
    }
}
