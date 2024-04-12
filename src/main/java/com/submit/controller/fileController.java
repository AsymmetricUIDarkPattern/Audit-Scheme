package com.submit.controller;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.read.context.AnalysisContext;
import com.alibaba.excel.read.event.AnalysisEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSONObject;
import com.submit.Util.HttpClientUtil;
import com.submit.pojo.ApkInfo;
import com.submit.Util.Utils;
import com.submit.pojo.*;
import com.submit.service.AppService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class fileController {
//    public static String APK_PATH = "G:\\data\\app";
//    public static String ICON_PATH = "G:\\data\\icon";

    @Value("${file.upload.apk_info_url}")
    private String APK_INFO_PATH;
    @Value("${file.app.install_url}")
    private String INSTALL_URL;

    private static final Logger logger = LoggerFactory.getLogger(fileController.class);

    @Autowired(required = false)
    com.submit.service.AppService appService;

    @RequestMapping("user/searchauditresult")
    @ResponseBody
    public Map<String, Object> searchAuditResult(@RequestParam(value = "submit_hash") String submitHash) throws IOException {
        if (submitHash == null) {
            return null;
        }
        List<AuditRet> list = appService.getAuditRet(submitHash);

        for (AuditRet auditRet : list) {
            System.out.println(auditRet);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("code", "0");
        map.put("count", list.size());
        map.put("data", list);

        return map;
    }

    @PostMapping("user/deleteapkbysubmithash")
    @ResponseBody
    public ResponseEntity<String> deleteApkBySubmitHash(@RequestParam("package_name") String packageName, HttpServletRequest request) {
        // 如果之前有提交过相同的packer name的app，直接把之前的给删除掉
        List<ApkInfo> apkRet = appService.deleteIfExist(packageName);
        String retStr = "";
        if (apkRet != null || apkRet.size() > 0) {
            // 懒得写删除文件的逻辑了，就这么着吧！但是数据库的记录删了,哈哈
            retStr = "删除成功";
        }
        return new ResponseEntity<String>(retStr, HttpStatus.OK);
    }

    @PostMapping("user/fuck")
    @ResponseBody
    public ResponseEntity<String> fuck(@RequestParam("num") Integer num, HttpServletRequest request) {
        System.out.println("fuck: " + num);
        return new ResponseEntity<String>("fuck", HttpStatus.OK);
    }

    Map<String, Object> getRet(String mes) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", "0");
        map.put("msg", mes);
        return map;
    }

    @RequestMapping("user/upload_apk_pro")
    @ResponseBody
    public Map<String, Object> upload(MultipartFile file, HttpServletRequest request) {

        System.out.println("This is upload(@RequestParam(\"file\"): " + file.getOriginalFilename());
        String fileName = file.getOriginalFilename();
        String[] fileNameArr = fileName.split("\\.");
        if (fileNameArr.length < 2 || !fileNameArr[fileNameArr.length - 1].equals("apk")) {
//            return new ResponseEntity<String>("文件格式不合格，请上传.apk文件", HttpStatus.BAD_REQUEST);
            return getRet("文件格式不合格，请上传.apk文件");
        }

        String ramStr = Utils.getRandomString(10);
        String newFileName = ramStr + ".apk";

        ApkInfo appInfo = null;

        String retStr = "";
        // 获取file信息
        try {
            File appFile = Utils.MultipartFileToFile(file);
            appInfo = Utils.GetFileInfo(appFile);
            if (appInfo == null) {
//                return new ResponseEntity<String>("出错啦！！！appInfo==null", HttpStatus.BAD_REQUEST);
                return getRet("出错啦！！！appInfo==null");
            }
            System.out.println(appInfo);

            // 如果之前有提交过相同的packer name的app，直接把之前的给删除掉
            List<ApkInfo> apkRet = appService.deleteIfExist(appInfo.getPackageName());
            if (apkRet != null || apkRet.size() > 0) {
                // 懒得写删除文件的逻辑了，就这么着吧！但是数据库的记录删了,哈哈
                retStr = "上传成功，并覆盖之前的提交";
            } else {
                retStr = "上传成功";
            }

            // 保存app
            Utils.saveFile(file, APK_INFO_PATH + File.separator + "app", newFileName);
            // 保存icon
            // G:\\data\\icon\\
            String ICON_PATH = APK_INFO_PATH + File.separator + "icon" + File.separator;
            String iconFileName = Utils.saveAPPIcon(appFile, appInfo.getIconPathName(), ICON_PATH, ramStr);

            // ApkInfo apkInfo, String userId, String submitHash) {

            String userId = (String) request.getSession().getAttribute("userid");

            System.out.println("iconFileName: " + userId + " " + iconFileName + " " + ramStr);
            appService.addApkInfo(appInfo, userId, iconFileName, ramStr);
        } catch (Exception e) {
            e.printStackTrace();
//            return new ResponseEntity<String>("文件格式不合格", HttpStatus.BAD_REQUEST);
            return getRet("文件格式不合格");
        }

        Thread installTr = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> param = new HashMap<>();
                param.put("hash", "aaaa");
                HttpClientUtil.doGet(INSTALL_URL, param);
            }
        });
        installTr.start();

//        return new ResponseEntity<String>(retStr, HttpStatus.OK);
        return getRet(retStr);
    }

    @ResponseBody
    @GetMapping("user/getapkfile")
    public Map<String, Object> getApkFile(HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute("userid");
        Integer level = (Integer) request.getSession().getAttribute("level");
        List<ApkInfo> list = null;
        if (userId == null) {
            list = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("code", "0");
            map.put("count", 0);
            map.put("data", list);
            return map;
        }
        if(level==1) {
            try {
                list = appService.getAllApkFile();
            } catch (Exception e) {
                list = new ArrayList<>();
                e.printStackTrace();
            }
        } else {
            try {
                list = appService.getApkFile(userId);
            } catch (Exception e) {
                list = new ArrayList<>();
                e.printStackTrace();
            }
        }
        for (ApkInfo apkInfo : list) {
            if (apkInfo.getIconWebPath() != null) {
                String iconWebPath = File.separator + "icon" + File.separator + apkInfo.getIconWebPath();
                apkInfo.setIconWebPath(iconWebPath);
            } else {
                apkInfo.setIconWebPath("/Images/xiguatou.jpg");
            }
            if (level == 1) {
                apkInfo.setStatus(apkInfo.getStatus() + "#1");
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("code", "0");
        map.put("count", list.size());
        map.put("data", list);
        return map;
    }

    @ResponseBody
    @GetMapping("user/getappaudittype")
    public Map<String, Object> getAppAuditType(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        String userid = (String) request.getSession().getAttribute("userid");
        List<Map<String, Object>> ret = appService.getAppAuditType(userid);

        for (Map<String, Object> item : ret) {
            map.put((String) item.get("status"), item.get("num"));
        }

        for(Map.Entry<String,Object> item: map.entrySet()) {
            System.out.println("getappaudittype: " + item.getKey() + " " + item.getValue());
        }

        return map;
    }

    @ResponseBody
    @GetMapping("user/getappauditresult")
    public Map<String, Object> getAppAuditResult(@RequestParam("submit_hash") String submitHash) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> ret = appService.getAppAuditResult(submitHash);

        for (Map<String, Object> item : ret) {
            map.put((String) item.get("type"), item.get("num"));
        }

        for(Map.Entry<String,Object> item: map.entrySet()) {
            System.out.println("getappauditresult: " + item.getKey() + " " + item.getValue());
        }

        return map;
    }

    @ResponseBody
    @PostMapping("user/change2under_audit")
    public ResponseEntity<String> change2underAudit(@RequestParam("submit_hash") String submitHash) throws IOException {
        // 检查文件是否合法
        if (submitHash.isEmpty() || submitHash == null) {
            return new ResponseEntity<String>("submitHash为空", HttpStatus.BAD_REQUEST);
        }
        if (!appService.change2underAudit(submitHash)) {
            return new ResponseEntity<String>("更改失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("under_audit", HttpStatus.OK);
    }

    @PostMapping(value = "/downloadclassfile")//teachclassid jobid
    public String downloadclassfile(int teachclassid, int classFileId) throws IOException {
        return "ok";
    }

//    @PostMapping(value = "/download")//teachclassid jobid
//    public String download(int lesson, int job, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        return downloadzip(false, lesson, job, request, response);
//    }

    //用于判断超时
//    @PostMapping("/downloadovertime")
//    public String downloadovertme(int lesson, int job, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        return downloadzip(true, lesson, job, request, response);
//    }

//    public String downloadzip(boolean isover, int lesson, int job, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String pat = "";
//        if (isover) pat = "fileget/overtime/" + lesson + "/" + job;
//        else pat = "fileget/" + lesson + "/" + job;
//        String zipname = "";
//        teachclass teachclass = teachclassMapper.selectByPrimaryKey(lesson);
//        job job1 = jobMapper.selectByPrimaryKey(job);
//        zipname += teachclass.getCoursename();
//        zipname += "实验" + job1.getNo() + job1.getTitle();
//        zipname += ".zip";
//        String filename = Paths.get(uploadFilePath, pat).toString();
//
//        response.setCharacterEncoding("utf-8");
//        request.setCharacterEncoding("UTF-8");
//        HttpSession session = request.getSession();
//        if (session.getAttribute("teacherid") == null) return null;
//        response.setContentType("text/html");
//
//        //设置文件MIME类型
//        response.setContentType(session.getServletContext().getMimeType(zipname));
//        //设置Content-Disposition
//        response.setHeader("Content-Disposition", "attachment;filename=" + new String(zipname.getBytes("utf-8"), "ISO8859_1"));
//        File file = new File(filename);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//
//        OutputStream out = response.getOutputStream();
//        ZipOutputStream zipout = new ZipOutputStream(out);
//
//        dozip(zipout, file, "");
//        zipout.close();
//        out.close();
//        return null;
//    }

    private static void dozip(ZipOutputStream zipout, File file, String addpath) throws IOException {
        if (file.isDirectory()) {
            File f[] = file.listFiles();
            for (int i = 0; i < f.length; i++) {
                if (f[i].isDirectory()) {
                    dozip(zipout, f[i], addpath + f[i].getName() + "/");
                } else {
                    dozip(zipout, f[i], addpath + f[i].getName());
                }
            }
        } else {
            InputStream input;
            BufferedInputStream buff;
            zipout.putNextEntry(new ZipEntry(addpath));
            input = new FileInputStream(file);
            buff = new BufferedInputStream(input);
            byte b[] = new byte[1024 * 5];
            int a = 0;
            while ((a = buff.read(b)) != -1) {
                zipout.write(b);
            }
            buff.close();
            input.close();
        }

    }


    /* 解析监听器，
     * 每解析一行会回调invoke()方法。
     * 整个excel解析结束会执行doAfterAllAnalysed()方法
     *
     * 下面只是我写的一个样例而已，可以根据自己的逻辑修改该类。
     * @author jipengfei
     * @date 2017/03/14
     */
    public static class ExcelListener extends AnalysisEventListener {

        //自定义用于暂时存储data。
        //可以通过实例获取该值
        private List<Object> datas = new ArrayList<Object>();

        public void invoke(Object object, AnalysisContext context) {
            datas.add(object);//数据存储到list，供批量处理，或后续自己业务逻辑处理。
            doSomething(object);//根据自己业务做处理
        }

        private void doSomething(Object object) {
            //1、入库调用接口
        }

        public void doAfterAllAnalysed(AnalysisContext context) {
            // datas.clear();//解析结束销毁不用的资源
        }

        public List<Object> getDatas() {
            return datas;
        }

        public void setDatas(List<Object> datas) {
            this.datas = datas;
        }
    }


}
