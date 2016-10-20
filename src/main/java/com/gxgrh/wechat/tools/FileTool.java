package com.gxgrh.wechat.tools;
import java.io.*;
import java.net.URLDecoder;
import java.util.Properties;

import com.sun.corba.se.spi.orbutil.fsm.Input;
import org.springframework.stereotype.Component;

@Component
public class FileTool {

    /**
     * 从配置文件.properties中取的参数值
     * @param name 参数名
     * @return 参数值
     * @throws Exception
     */
    public String getParamByName(String name,String fileName) throws Exception{
        String filePath = FileTool.class.getClassLoader().getResource(fileName).getPath();
        filePath = URLDecoder.decode(filePath,"utf-8");
        InputStream is = new FileInputStream(filePath);
        Properties pro = new Properties();
        String value = null;
        try {
            pro.load(is);
            value = pro.getProperty(name);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("读取配置文件失败!");
        }finally{
            if(null != is){
                is.close();
                is = null;
            }
        }
        return value;
    }


    /**
     * 更新properties文件的键值对
     * @param name 键名
     * @param value 键值
     * @param fileName 属性文件名字
     * @throws Exception
     */
    public void updateProperties(String name, String value, String fileName) throws Exception{

        Properties pro = new Properties();
        FileInputStream fis = null;
        FileOutputStream fos = null;
        String filePath = FileTool.class.getClassLoader().getResource(fileName).getPath();
        filePath = URLDecoder.decode(filePath,"utf-8");
        try{
            fis = new FileInputStream(filePath);
            pro.load(fis);
            pro.setProperty(name,value);
            fos = new FileOutputStream(filePath);
            pro.store(fos,"update "+name+"[value="+value+"]");
            fos.flush();
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("写入配置文件失败!");
        }finally {
            if(null != fos){
                fos.close();
            }
            if(null != fis){
                fis.close();
            }
        }
    }

    public void inputStreamToFile(InputStream is, File targetFile) throws Exception{
        FileOutputStream fileOutputStream = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bous = null;
        try{
            if(!targetFile.exists()){
                targetFile.createNewFile();
            }
            fileOutputStream  = new FileOutputStream(targetFile);
            bous = new BufferedOutputStream(fileOutputStream);
            bis = new BufferedInputStream(is);
            int count = -1;
            byte[] bytes = new byte[1024];
            while((count = bis.read(bytes,0,1024)) != -1){
                bous.write(bytes,0,count);
            }
            bous.flush();
            bytes = null;
        }catch(Exception e){
            e.printStackTrace();
            throw  new Exception(e.getMessage());
        }finally {
            if(null != fileOutputStream){
                fileOutputStream.close();
            }
            if(null != bous){
                bous.close();
            }
            if(null != bis){
                bis.close();
            }
        }

    }

    public void byteArrayToFile(byte[] data, File targetFile) throws Exception{
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bous = null;
        try{
            if(!targetFile.exists()){
                targetFile.createNewFile();
            }
            fileOutputStream  = new FileOutputStream(targetFile);
            bous = new BufferedOutputStream(fileOutputStream);
            bous.write(data);
            bous.flush();
        }catch(Exception e){
            e.printStackTrace();
            throw  new Exception(e.getMessage());

        }finally {
            if(null != fileOutputStream){
                fileOutputStream.close();
            }
            if(null != bous){
                bous.close();
            }
        }

    }

    public byte[] FileToByteArray(File srouceFile) throws Exception{
        FileInputStream fileInputStream = null;
        BufferedInputStream bis = null;
        byte[] content = null;
        try{
            if(!srouceFile.exists()){
                throw new Exception("文件不存在!");
            }
            fileInputStream = new FileInputStream(srouceFile);
            bis = new BufferedInputStream(fileInputStream);
            content = new byte[bis.available()];
            bis.read(content);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(bis != null){
                try{
                    bis.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            if(fileInputStream != null){
                try{
                    fileInputStream.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return content;
    }

}
