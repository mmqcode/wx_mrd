package com.gxgrh.wechat.tools.test;

import com.gxgrh.wechat.tools.FileTool;
import com.gxgrh.wechat.tools.Validate;
import com.gxgrh.wechat.wechatapi.service.MaterialApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.Map;

/**文件操作测试类
 * Created by Administrator on 2016/10/11.
 */

@Controller
@RequestMapping("/FileController")
public class FileController {

    @Autowired
    private FileTool fileTool;

    @Autowired
    private MaterialApiService mediaService;

    @RequestMapping(value="/saveMeida", method = RequestMethod.POST)
    @ResponseBody
    public String uploadWeChatMedia(
            @RequestParam(value="file", required = false)MultipartFile file,
            @RequestParam("uploader")String uploader,
            HttpServletRequest request){
        String fileStoreDirectoryString = System.getProperty("Web.root")+ File.separator+"fileUpload";
        File fileStoreDirectory = new File(fileStoreDirectoryString);
        File fileToStore = null;
        InputStream is = null;
        try{
            if(!fileStoreDirectory.exists()){
                fileStoreDirectory.mkdirs();
            }
            String fileName = file.getOriginalFilename();
            fileToStore = new File(fileStoreDirectoryString,fileName);
            if(fileToStore.exists()){
                System.out.println("文件已经存在.");
            }else{
                fileToStore.createNewFile();
                is =  file.getInputStream();
                fileTool.inputStreamToFile(is,fileToStore);
            }
           // mediaService.upLoadTempMedia("image",fileToStore.getPath());
            mediaService.uploadOtherPermanentMaterial("image",fileToStore.getPath());
           // mediaService.uploadNewsImg(fileToStore.getPath());
            //打印cookies
            Cookie[] cookies = request.getCookies();
            if(cookies != null && cookies.length > 0){
                for(Cookie c:cookies){
                    System.out.println(c.getName()+c.getValue());
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(null != is){
                try{
                    is.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

        }
        return fileToStore.getPath();
    }

    @RequestMapping(value="/downloadMedia", method = RequestMethod.GET)
    @ResponseBody
    public void dowloadMedia(
            @RequestParam("mediaId")String mediaId,
            HttpServletRequest request,
            HttpServletResponse response
            ){

        try{
            String fileStoreDirectoryString = System.getProperty("Web.root")+ File.separator+"staticResources"+
                    File.separator+"downloadFile";
            File fileStoreDirectory = new File(fileStoreDirectoryString);
            if(!fileStoreDirectory.exists()){
                fileStoreDirectory.mkdirs();
            }
            //Map<String,Object> rspData = this.mediaService.downLoadTempMedia(mediaId);
            Map<String,Object> rspData = this.mediaService.downloadOtherMaterial(mediaId);
            String fileName =(String) rspData.get("fileName");
            byte[] fileData = (byte[])rspData.get("data");
            File file = new File(fileStoreDirectoryString,fileName);
            if(!file.exists()){
                file.createNewFile();
            }

            fileTool.byteArrayToFile(fileData,file);

            response.setCharacterEncoding("utf-8");
            String url = "http://wxtest.gxit.ren/wx_mrd/staticResources/downloadFile/"+fileName;
            response.getWriter().write(url);
            response.getWriter().flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping(value="/streamDownload",method = RequestMethod.GET)
    @ResponseBody
    public void  streamDownload(HttpServletRequest request, HttpServletResponse response){
        String fileStoreDirectoryString = System.getProperty("Web.root")+ File.separator+"fileUpload";
        String fileName = "idea快捷键.txt";
        String fileString = fileStoreDirectoryString+File.separator+fileName;
        try{
            fileString = URLDecoder.decode(fileString,"utf-8");

            byte[] content = fileTool.FileToByteArray(new File(fileString));

            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName="+fileName);
            response.getOutputStream().write(content);
            response.getOutputStream().flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }

    }


    @RequestMapping(value="/accessWebApp", method = RequestMethod.GET)
    public String accessApplication(HttpServletRequest request, HttpServletResponse response){

        boolean isTokenExists = false;

        try{
            Cookie[] cookies = request.getCookies();
            if(cookies != null && cookies.length > 0){
                for(Cookie cookie : cookies){
                    if("token".equals(cookie.getName())){

                        //验证token有效性
                        if(cookie.getValue().equals("12312414")){
                            isTokenExists = true;
                            break;
                        }
                    }
                }
            }
            String token = request.getParameter("token");
            if(Validate.isString(token)){
                isTokenExists = true;
                Cookie cookie = new Cookie("token",token);
                cookie.setPath("/wx_mrd");
                cookie.setDomain("gxit.ren");
                response.addCookie(cookie);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            if(!isTokenExists){
                //302重定向到客户服务登录界面
                response.sendRedirect("http://192.168.1.145:8087/EasyUILearningII/login/applyToken?url=accessWebApp");
            }
        }catch (Exception e){

        }
        String targetPage = "testToken";
        return targetPage;
    }

}
