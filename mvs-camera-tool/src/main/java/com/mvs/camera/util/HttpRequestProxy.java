package com.mvs.camera.util;

import com.mvs.camera.pojo.MvsCamera;
import com.mvs.camera.pojo.ResultBean;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import org.apache.commons.logging.Log;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

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
     * 
     * 发送不带参数的GET的HTTP请求
     *
     * @param reqUrl HTTP请求URL
     * @param pressText	水印内容
     * @param imageType	图片格式
     * @param qualityType 是否压缩
     * @param cto
     * @param rto
     * @return HTTP响应的字符串
     */
    public ResultBean doImageGet(String reqUrl, String pressText,String imageType,int qualityType, int cto, int rto) {
        HttpURLConnection urlCon = null;
        ResultBean result = null;
        try {
            URL url = new URL(reqUrl);
            urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setRequestMethod("GET");
            
            if(StringUtils.isEmpty(imageType)) {
            	imageType = "jpg";
            }
            //添加头信息，返回的image类型
        	urlCon.setRequestProperty("ImageType", imageType);
            
            //主机连接超时
            urlCon.setConnectTimeout(this.connectTimeOut);
            //数据读取超时
            urlCon.setReadTimeout(this.readTimeOut);
            urlCon.setConnectTimeout(cto);
            urlCon.setReadTimeout(rto);
            urlCon.setDoInput(true);
            
            if(urlCon.getResponseCode() == 200) {
            	//如果响应为“200”，表示成功响应，则返回一个输入流
                //File ifile = new File("F:\\imgtest\\123.jpg");
                //is = new FileInputStream(ifile);
                String imageName = makeImageFromStream(urlCon.getInputStream(), imageType, qualityType, pressText);
                
	            result = new ResultBean(true, imageName);
            }else {
            	result = new ResultBean(false, "无法获取照片，请检查路径：" + reqUrl);
            }
        } catch (Exception e) {
        	result = new ResultBean(false, "获取照片出错：" + e.getMessage());
            LOGGER.error("网络故障请求：" + e.getMessage() + "，reqUrl=" + reqUrl);
        } finally {
            if (urlCon != null) {
                urlCon.disconnect();
            }
        }

        //更换组件再次尝试
		if(!result.isSuccess()) {
			LOGGER.warn("（第一次）获取照片失败，再次尝试...原因："+ result.getMsg());
			try {
				Thread.sleep(200);
				
				result = doImageGetRestTemplateSimple(reqUrl,pressText,imageType,qualityType);
				LOGGER.info("（第二次）获取照片结果："+result.isSuccess()+"，返回信息：" + result.toString());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
        return result;
    }
    
    /**
     * 
     * RestTemplate GET的HTTP请求
     *
     * @param reqUrl HTTP请求URL
     * @param pressText	水印内容
     * @param imageType	图片格式
     * @param qualityType 是否压缩
     * @param cto
     * @param rto
     * @return HTTP响应的字符串
     */
    public ResultBean doImageGetRestTemplate(String reqUrl, String pressText,String imageType,int qualityType, int cto, int rto) {
        ResultBean result = null;
        
    	CloseableHttpClient closeableHttpClient = null;
        CloseableHttpResponse closeableHttpResponse = null;
        try {
        	closeableHttpClient = HttpClients.createDefault(); //1、创建实例
            HttpGet httpGet = new HttpGet(reqUrl); //2、创建请求
        	
            if(StringUtils.isEmpty(imageType)) {
            	imageType = "jpg";
            }
            //添加头信息，返回的image类型
        	httpGet.addHeader("ImageType", imageType);
        	
        	closeableHttpResponse = closeableHttpClient.execute(httpGet); //3、执行
            HttpEntity httpEntity = closeableHttpResponse.getEntity(); //4、获取实体
            
            if(httpEntity != null){
                LOGGER.info("返回ContentType:"+httpEntity.getContentType().getValue());
            }

			InputStream is = httpEntity.getContent();

            if(closeableHttpResponse.getStatusLine().getStatusCode() == 200) {
            	//如果响应为“200”，表示成功响应，则返回一个输入流
                String imageName = makeImageFromStream(is, imageType, qualityType, pressText);
                
	            result = new ResultBean(true, imageName);
            }else {
            	result = new ResultBean(false, "无法获取照片，请检查路径：" + reqUrl);
            }
        } catch (Exception e) {
        	result = new ResultBean(false, "获取照片出错：" + e.getMessage());
            LOGGER.error("网络故障请求：" + e.getMessage() + "，reqUrl=" + reqUrl);
        }finally {
	        if(closeableHttpResponse != null) {
	        	try {
					closeableHttpResponse.close();
				} catch (IOException e) {
					LOGGER.error("关闭closeableHttpResponse出错：" + e.getMessage());
				}
	        }
	        if(closeableHttpClient != null) {
	        	try {
					closeableHttpClient.close();
				} catch (IOException e) {
					LOGGER.error("关闭closeableHttpClient出错：" + e.getMessage());
				}
	        }
		}
		
        return result;
    }

    /**
     * 
     * @param is 输入流
     * @param imageType 图片类型
     * @param qualityType 压缩质量
     * @param pressText 水印文字
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     */
	private String makeImageFromStream(InputStream is, String imageType, int qualityType, String pressText)
			throws IOException, FileNotFoundException {
		LOGGER.info("（1）正常返回，图片流大小：" + is.available());
		
		String imageName = System.currentTimeMillis() + "." + imageType;
		// 将文件输出流与文件myavatar.jpg关联
		// 这里是输出到工程根目录下
		File file = new File(imageName);
		FileOutputStream fos = new FileOutputStream(file);

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
		LOGGER.info("（2）生成图片大小：" + file.length() + "，保存绝对路径： " + file.getAbsolutePath());
		
		//添加文字水印
		if(!StringUtils.isEmpty(pressText)) {
		    //根据文字内容生成水印图片
			/*BufferedImage waterImg = createImage(pressText);
		    
		    Thumbnails.of(file)
		        //添加水印
		        .watermark(Positions.BOTTOM_LEFT, waterImg, 0.7f)	//透明度
		        .outputQuality(1f)   //输出质量
		        .scale(1f)   //等比压缩
		        .toFile(file);
		    LOGGER.info("（3）水印生成图片大小：" + file.length());*/
			
			FileInputStream fis = new FileInputStream(file);
			//添加水印
			BufferedImage bm = addWaterMark(fis, pressText, Color.WHITE);
			ImageIO.write(bm, imageType, file);// 输出图片
			//关闭文件输入流
			fis.close();
		}
		
		//压缩
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
		    
		    long beforeLen = file.length();
		    // 图片压缩处理
		    Thumbnails.of(file)
		    	.scale(1f)
		    	.outputQuality(quality)
		    	.toFile(file);
		    LOGGER.info("（4）压缩后大小：" + file.length() + "，原文件：" + beforeLen);
		}
		return imageName;
	}

    public ResultBean doImageGetSimple(String reqUrl,String pressText,String imageType,int qualityType) {
        return doImageGet(reqUrl, pressText,imageType,qualityType,connectTimeOut, transferTimeOut);
    }

    public ResultBean doImageGetRestTemplateSimple(String reqUrl,String pressText,String imageType,int qualityType) {
        return doImageGetRestTemplate(reqUrl, pressText,imageType,qualityType,connectTimeOut, transferTimeOut);
    }
    
    /**
     * 根据str,font的样式以及输出文件目录  
     * @param str 做成图片的字符
     * @param font	//字体
     * @param outFile	//输出文件
     * @param width	//宽度
     * @param height	//高度
     * @throws Exception
     */
    public BufferedImage createImage(
    		String str
    		//,File outFile
    		) throws Exception {  
        // 创建图片  
        Font font = new Font("宋体", Font.PLAIN, 80);
        
        Integer width = str.length() * 70;
        Integer height = 150;
        
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);  
        Graphics g = image.getGraphics();  
        g.setClip(0, 0, width, height);  
        g.setColor(Color.white);  
        g.fillRect(0, 0, width, height);// 先用黑色填充整张图片,也就是背景  
        g.setColor(Color.black);// 在换成黑色  
        g.setFont(font);// 设置画笔字体  
        
        //设置水印的坐标
        int x = 50;
        int y = height - 50;
        g.drawString(str, x, y);  //画出水印
        g.dispose();  
        
        //ImageIO.write(image, "png", outFile);// 输出png图片
        
        return image;
    }  
    
    /**
     * 在原图上生成水印
     * @param waterMarkContent 水印内容
     * @param markContentColor 水印颜色
     */
    public BufferedImage addWaterMark(InputStream is, String waterMarkContent, Color markContentColor) {
        try {
            Font font = new Font("宋体", Font.PLAIN, 80);
            // 读取原图片信息
            Image srcImg = ImageIO.read(is);//文件转化为图片
            int srcImgWidth = srcImg.getWidth(null);//获取图片的宽
            int srcImgHeight = srcImg.getHeight(null);//获取图片的高
            
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            g.setColor(markContentColor); //根据图片的背景设置水印颜色
            g.setFont(font);//设置字体

            //设置水印的坐标
            int x = 20;
            int y = srcImgHeight - 60;
            g.drawString(waterMarkContent, x, y);  //画出水印
            g.dispose();
            return bufImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public static void main(String[] args) {
		String clientUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1540878579376&di=16452e2f4cc5101e6119e30107a80325&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F0125fd5770dfa50000018c1b486f15.jpg%401280w_1l_2o_100sh.jpg";
		String pressText = "测试水印asdasdasd";
		long l1 = System.currentTimeMillis();
		ResultBean result = new CameraTool(new MvsCamera(clientUrl,pressText,1,1)).getImage();
		LOGGER.info("返回结果：" + result.toString() + "，耗时：" + (System.currentTimeMillis() - l1));
	}
}
