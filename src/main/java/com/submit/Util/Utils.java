package com.submit.Util;

import com.submit.pojo.ApkInfo;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class Utils {
    public static ApkInfo GetFileInfo(File file) throws IOException {
        ApkInfo apkInfo = null;
        if (file.exists() && file.isFile()) {
            ApkFile apkFile = new ApkFile(file);
            ApkMeta apkMeta = apkFile.getApkMeta();

            //  拷贝出的icon文件名 根据需要可以随便改
//                name = apkMeta.getLabel();

//                System.out.println("应用名称   :" + apkMeta.getLabel());
//                System.out.println("包名       :" + apkMeta.getPackageName());
//                System.out.println("版本号     :" + apkMeta.getVersionName());
//                System.out.println("图标       :" + apkMeta.getIcon());
//                System.out.println("大小       :" + (double) (file.length() * 100 / 1024 / 1024) / 100 + " MB");

            apkInfo = new ApkInfo();
            apkInfo.setAppName(apkMeta.getLabel());
            apkInfo.setIconPathName(apkMeta.getIcon());
            apkInfo.setPackageName(apkMeta.getPackageName());
            apkInfo.setAppVersion(apkMeta.getVersionName());
            apkInfo.setSize((double) (file.length() * 100 / 1024 / 1024) / 100);
        }
        return apkInfo;
    }

    public static File MultipartFileToFile(MultipartFile multiFile) throws IOException {

        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若需要防止生成的临时文件重复,可以在文件名后添加随机码
        File file = File.createTempFile(fileName, prefix);
        multiFile.transferTo(file);
        return file;
    }

    public static void saveFile(MultipartFile file, String path, String fileName) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File insertFile = new File(dir, fileName);//创建文件
        OutputStream out = null;
        try {
            out = new FileOutputStream(insertFile);
            BufferedOutputStream buf = new BufferedOutputStream(out);

            byte by[] = file.getBytes();
            file.getBytes();
            buf.write(by);
            buf.flush();
            buf.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //  拷贝图标
    public static String saveAPPIcon(File file, String iconPathName, String path, String ramStr) throws IOException {
        File pathFile = new File(path);
        String iconWebPath = null;
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }

        ZipInputStream zin = null;
        try {
            //  访问apk 里面的文件
            ZipFile zf = new ZipFile(file);
            InputStream in = new BufferedInputStream(new FileInputStream(file));
            zin = new ZipInputStream(in);
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.getName().equals(iconPathName)) {
                    //  拷贝出图标
//                    System.out.println("拷贝开始");
                    InputStream inStream = zf.getInputStream(ze);

                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    //创建一个Buffer字符串
                    byte[] buffer = new byte[1024];
                    //每次读取的字符串长度，如果为-1，代表全部读取完毕
                    int len = 0;
                    //使用一个输入流从buffer里把数据读取出来
                    while ((len = inStream.read(buffer)) != -1) {
                        //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                        outStream.write(buffer, 0, len);
                    }
                    //关闭输入流
                    inStream.close();
                    //把outStream里的数据写入内存

                    //得到图片的二进制数据，以二进制封装得到数据，具有通用性
                    byte[] data = outStream.toByteArray();
                    //new一个文件对象用来保存图片，默认保存当前工程根目录
                    String[] iconFileTypeArr = iconPathName.split("\\.");
                    String iconFileType = iconFileTypeArr[iconFileTypeArr.length - 1];
                    iconWebPath = ramStr + "." + iconFileType;
                    String iconFilePath = path + File.separator + ramStr + "." + iconFileType;
                    File imageFile = new File(iconFilePath);
                    //创建输出流
                    FileOutputStream fileOutStream = new FileOutputStream(imageFile);
                    //写入数据
                    fileOutStream.write(data);
                    fileOutStream.flush();
                    System.out.println("icon拷贝结束");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert zin != null;
            zin.closeEntry();
        }
        return iconWebPath;
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
