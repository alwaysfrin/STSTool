package com.jco.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestProxy.class);

    /**
     * 连接超时
     */
    private int connectTimeOut = 500;
    /**
     * 传输请求连接超时
     */
    private int transferTimeOut = 3000;

    /**
     * 读取数据超时
     */
    private int readTimeOut = 5000;    //（单位[dan wei]：毫秒）jdk

    /**
     * 请求编码
     */
    private String requestEncoding = "UTF-8";

    /**
     * 获取数据编码
     */
    private String reponseEncoding = "UTF-8";

    /******************************** 工具方法 开始 ***************************************/
    /**
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
     * <pre>
     *
     * 发送带参数的POST的HTTP请求
     * </pre>
     *
     * @param reqUrl     HTTP请求URL
     * @param parameters 参数映射表
     */
    public String doPost(String reqUrl, Map<String, String> parameters, int cto, int rto) {
        return doPost(reqUrl, parameters, reponseEncoding, cto, rto);
    }

    public String doPost(String reqUrl, Map<String, String> parameters) {
        return doPost(reqUrl, parameters, reponseEncoding, connectTimeOut, transferTimeOut);
    }

    /**
     * <pre>
     *
     * 发送带参数的POST的HTTP请求
     * </pre>
     *
     * @param reqUrl     HTTP请求URL
     * @param parameters 参数映射表
     * @return HTTP响应的字符串
     */
    public String doPost(String reqUrl, Map<String, String> parameters, String recvEncoding, int cto, int rto) {
        long startTime = System.currentTimeMillis();
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
            url_con.setRequestMethod("POST");
            url_con.setConnectTimeout(cto);
            url_con.setReadTimeout(rto);
            url_con.setDoOutput(true);

            byte[] b = params.toString().getBytes("UTF-8");
            if (b.length < 2048) {
                url_con.getOutputStream().write(b, 0, b.length);
                url_con.getOutputStream().flush();
                url_con.getOutputStream().close();
            } else {
                BufferedOutputStream bos = new BufferedOutputStream(url_con.getOutputStream());
                bos.write(b, 0, b.length);
                bos.flush();
                bos.close();
            }

            LOGGER.info("数据传输大小：" + b.length);

            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer tempStr = new StringBuffer();
            String crlf = System.getProperty("line.separator");
            while (tempLine != null) {
                tempStr.append(tempLine);
                tempStr.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = tempStr.toString();
            rd.close();
            in.close();
        } catch (IOException e) {
            LOGGER.error("网络故障请求：" + e.getMessage() + "，reqUrl=" + reqUrl);
        } finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }
        LOGGER.info("请求结束，耗时：" + (System.currentTimeMillis() - startTime));
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
        HttpURLConnection url_con = null;
        try {
            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setConnectTimeout(cto);
            url_con.setReadTimeout(rto);
            url_con.connect();

            //LOGGER.info(url_con.getHeaderField(0) + " -- " + url_con.getResponseMessage() + " -- " + url_con.getResponseCode());
            if (url_con.getResponseCode() == 200 || url_con.getResponseMessage().indexOf("OK ") > 0) {
                isConnect = true;
            }
        } catch (IOException e) {
            LOGGER.error("网络故障请求：" + e.getMessage() + "，reqUrl=" + reqUrl);
        } finally {
            if (url_con != null) {
                url_con.disconnect();
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
}
