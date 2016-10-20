package com.gxgrh.wechat.tools;

import com.gxgrh.wechat.wechatapi.service.SystemApiService;
import org.apache.http.HttpConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**发送https请求的工具类
 * Created by Administrator on 2016/9/27.
 */
@Component
public class HttpTool {


    @Autowired
    private IoTool ioTool;

    private static final Logger logger =  LogManager.getLogger(HttpTool.class);

    /**
     * 忽视证书HostName
     */
    private static HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
        public boolean verify(String s, SSLSession sslsession) {
            System.out.println("WARNING: Hostname is not matched for cert.");
            return true;
        }
    };

    /**
     * Ignore Certification
     */
    private static TrustManager ignoreCertificationTrustManger = new X509TrustManager(){
        private X509Certificate[] certificates;
        public void checkClientTrusted(X509Certificate certificates[],
                                       String authType) throws CertificateException {
            if (this.certificates == null) {
                this.certificates = certificates;
            }
        }
        public void checkServerTrusted(X509Certificate[] ax509certificate,
                                       String s) throws CertificateException {
            if (this.certificates == null) {
                this.certificates = ax509certificate;
            }
        }
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return new java.security.cert.X509Certificate[0];
        }
    };

    public  String sendSSLGetMethod(String urlString) throws Exception{
        String repString = null;
        InputStream is = null;
        HttpsURLConnection connection = null;
        try {

            URL url = new URL(urlString);
            /*
             * use ignore host name verifier
             */
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            connection = (HttpsURLConnection) url.openConnection();
            // Prepare SSL Context
            TrustManager[] tm = { ignoreCertificationTrustManger };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());

            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            connection.setSSLSocketFactory(ssf);
            if(connection.getResponseCode() != 200){

            }
            is = connection.getInputStream();
            repString = ioTool.getStringFromInputStream(is);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        } finally {
            if(null != is){
                is.close();
                is = null;
            }
            if(null != connection){
                connection.disconnect();
            }
        }
        return repString;
    }

    public String sendSSLPostMethod(String urlString,String postData) throws Exception{
        String repString = null;
        InputStream is = null;
        HttpsURLConnection connection = null;
        try {

            URL url = new URL(urlString);
            /*
             * use ignore host name verifier
             */
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("content-type","text/json");
            connection.setRequestProperty("content-length",String.valueOf(postData.getBytes().length));
            connection.getOutputStream().write(postData.getBytes("utf-8"));
            connection.getOutputStream().flush();
            connection.getOutputStream().close();
            // Prepare SSL Context
            TrustManager[] tm = { ignoreCertificationTrustManger };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());

            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            connection.setSSLSocketFactory(ssf);
            if(connection.getResponseCode() != 200){

            }
            is = connection.getInputStream();
            repString = ioTool.getStringFromInputStream(is);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        } finally {
            if(null != is){
                is.close();
                is = null;
            }
            if(null != connection){
                connection.disconnect();
            }
        }
        return repString;
    }


    /**
     * 上传文件到微信服务器
     * @param urlString 上传的目标url
     * @param filePath 文件路路径
     * @Param formDataName 表单id
     * @return
     * @throws Exception
     */
    public String sendSSLMutiPartFormData(String urlString,String filePath,String formDataName) throws Exception{
        String repString = null;
        InputStream is = null;
        OutputStream out = null;
        HttpsURLConnection connection = null;
        final String BOUNDARYSTR = ""+System.currentTimeMillis();
        final String BOUNDARY = "--"+BOUNDARYSTR+"\r\n";
        try{
            File file = new File(filePath);
            if(!file.exists() || !file.isFile()){
                String errorMsg = "文件["+filePath+"]不存在。无法上传。";
                logger.error(errorMsg);
                throw new Exception(errorMsg);
            }
            URL url = new URL(urlString);
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            // 设置请求头信息
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");

            connection.setRequestProperty("Content-type", "multipart/form-data;boundary=" + BOUNDARYSTR);
            StringBuilder sb = new StringBuilder();
            sb.append(BOUNDARY);
            sb.append("Content-Disposition: form-data;name=\""+formDataName+"\";filename=\""
                    + file.getName() + "\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");
            byte[] head = sb.toString().getBytes("utf-8");
            // 获得输出流
            out = new DataOutputStream(connection.getOutputStream());
            // 输出表头
            out.write(head);
            // 文件正文部分
            // 把文件已流文件的方式 推入到url中
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = bis.read(bufferOut,0,1024)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            bis.close();
            byte[] foot = ("\r\n--" + BOUNDARYSTR + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
            out.write(foot);
            out.flush();
            TrustManager[] tm = { ignoreCertificationTrustManger };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());

            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            connection.setSSLSocketFactory(ssf);
            if(connection.getResponseCode() != 200){

            }
            is = connection.getInputStream();
            repString = ioTool.getStringFromInputStream(is);
        }catch(Exception ex){
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }finally {
            if(null != is){
                is.close();
                is = null;
            }
            if(null != connection){
                connection.disconnect();
            }
        }
        return repString;
    }


    /**
     *
     * @param urlString
     * @return
     */
    public Map<String,Object> sendSSLGetDownloadMedia(String urlString){
        String fileName = null;
        byte[] repData = null;
        InputStream is = null;
        Map<String,Object> resultInfo = null;
        HttpsURLConnection connection = null;
        try {

            URL url = new URL(urlString);
            /*
             * use ignore host name verifier
             */
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            connection = (HttpsURLConnection) url.openConnection();
            // Prepare SSL Context
            TrustManager[] tm = { ignoreCertificationTrustManger };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());

            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            connection.setSSLSocketFactory(ssf);

            /**从以下头部数据解析出文件名
             * Content-disposition: attachment; filename="MEDIA_ID.jpg"
             */
            String contentDisposition = connection.getHeaderField("Content-disposition");
            if(contentDisposition != null){
                String[] contentDispositionArray = contentDisposition.split(";");
                for(String content:contentDispositionArray){
                    if(content.contains("filename")){
                        String[] contentArry = content.split("=");
                        fileName = contentArry[1];
                        fileName = fileName.replaceAll("\"","");
                    }
                }
            }
            if(connection.getResponseCode() != 200){

            }
            is = connection.getInputStream();
            repData = this.ioTool.getByteArrayFromInputStream(is);
            resultInfo = new HashMap<String,Object>();
            resultInfo.put("fileName",fileName);
            resultInfo.put("data",repData);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        } finally {
            if(null != is){
                try{
                    is.close();
                    is = null;
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            if(null != connection){
                connection.disconnect();
            }
        }
        return resultInfo;
    }

    /**
     *
     * @param urlString
     * @param postData
     * @return
     */
    public Map<String,Object> sendSSLPostDownloadMedia(String urlString, String postData){
        String fileName = null;
        byte[] repData = null;
        InputStream is = null;
        Map<String,Object> resultInfo = null;
        HttpsURLConnection connection = null;
        try{
            URL url = new URL(urlString);
            /*
             * use ignore host name verifier
             */
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("content-type","text/json");
            connection.setRequestProperty("content-length",String.valueOf(postData.getBytes().length));
            connection.getOutputStream().write(postData.getBytes("utf-8"));
            connection.getOutputStream().flush();
            connection.getOutputStream().close();
            // Prepare SSL Context
            TrustManager[] tm = { ignoreCertificationTrustManger };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());

            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            connection.setSSLSocketFactory(ssf);

            /**从以下头部数据解析出文件名
             * Content-disposition: attachment; filename="MEDIA_ID.jpg"
             */
            String contentDisposition = connection.getHeaderField("Content-disposition");
            String[] contentDispositionArray = contentDisposition.split(";");
            for(String content:contentDispositionArray){
                if(content.contains("filename")){
                    String[] contentArry = content.split("=");
                    fileName = contentArry[1];
                    fileName = fileName.replaceAll("\"","");
                }
            }
            if(connection.getResponseCode() != 200){

            }
            is = connection.getInputStream();
            repData = this.ioTool.getByteArrayFromInputStream(is);
            resultInfo = new HashMap<String,Object>();
            resultInfo.put("fileName",fileName);
            resultInfo.put("data",repData);
        }catch (Exception ex){
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }finally {
            if(null != is){
                try{
                    is.close();
                    is = null;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(null != connection){
                connection.disconnect();
            }
        }
        return resultInfo;

    }


    /**
     *
     * @param urlString
     * @return
     */
    public String sendGetMethod(String urlString){
        String repString = null;
        InputStream is = null;
        URLConnection connection = null;
        try {

            URL url = new URL(urlString);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection =  url.openConnection();

            is = connection.getInputStream();
            repString = ioTool.getStringFromInputStream(is);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        } finally {
            if(null != is){
                try{
                    is.close();
                    is = null;
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        return repString;
    }


    /**
     *
     * @param urlString
     * @param postData
     * @return
     */
    public String sendPostMethod(String urlString, String postData){
        String repString = null;
        InputStream is = null;
        URLConnection connection = null;
        try {

            URL url = new URL(urlString);
            connection = url.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("content-type","text/json");
            connection.getOutputStream().write(postData.getBytes("utf-8"));
            connection.getOutputStream().flush();
            connection.getOutputStream().close();
            connection.getOutputStream().close();
            is = connection.getInputStream();
            repString = ioTool.getStringFromInputStream(is);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        } finally {
            if(null != is){
                try{
                    is.close();
                    is = null;
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        return repString;
    }




}
