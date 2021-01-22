package com.cleaner.booster.phone.repairer.app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.widget.Toast;

import com.cleaner.booster.phone.repairer.app.models.CommonModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class Utils {

    Context context;
    String destinationPath;
    File folder;
    float docSize = 0;
    float audioSize = 0;
    float size = 0;

    public Utils(Context context) {
        this.context = context;
    }

    // get saved status data
    public List<CommonModel> getSavedStatusFiles(File parentDir) {
        File fold = new File(parentDir.getPath());
        List<CommonModel> docList = new ArrayList<>();
        File[] mlist = fold.listFiles();

        {
            if (mlist != null)
                for (File f : mlist) {
                    if (f.isDirectory()) {
                        docList.addAll(getSavedStatusFiles(new File(f.getAbsolutePath())));
                    } else {
                        CommonModel data = new CommonModel();
                        if (f.getAbsolutePath().endsWith(".jpeg") ||
                                f.getAbsolutePath().endsWith(".jpg") ||
                                f.getAbsolutePath().endsWith(".png") ||
                                f.getName().endsWith("mp4")) {
                            if (f.getName().contains("p_repair")) {
                                data.setName(f.getName());
                                data.setPath(f.getPath());
                                data.setSize(f.length());
                                //                doc.setType(FileTypes.DocumentType);
                                if (f.length() > 0)
                                    docList.add(data);
                            }
                        }
                    }
                }
        }

        return docList;
    }

    // get all data
    public List<CommonModel> getListFiles(File parentDir) {
        File fold = new File(parentDir.getPath());
        List<CommonModel> docList = new ArrayList<>();
        File[] mlist = fold.listFiles();
        File[] mFilelist = fold.listFiles();

        {
            if (mlist != null)
                for (File f : mlist) {
                    if (f.isDirectory()) {
                        docList.addAll(getListFiles(new File(f.getAbsolutePath())));
                    } else {
                        CommonModel data = new CommonModel();
                        data.setName(f.getName());
                        data.setPath(f.getPath());
                        data.setSize(f.length());
                        //                doc.setType(FileTypes.DocumentType);
                        if (f.length() > 0)
                            docList.add(data);
                    }
                }
        }

        return docList;
    }


    //get specific data
    public List<CommonModel> getListFiles(File parentDir, String forWhat) {
        File fold = new File(parentDir.getPath());
        List<CommonModel> docList = new ArrayList<>();
        File[] mlist = fold.listFiles();


        if (forWhat.matches("images")) {
            if (mlist != null) {
                for (File f : mlist) {
                    if (f.isDirectory()) {
                        docList.addAll(getListFiles(new File(f.getAbsolutePath(), "images")));
                    } else {

                        CommonModel data = new CommonModel();
                        if (f.getAbsolutePath().endsWith(".jpeg") || f.getAbsolutePath().endsWith(".jpg") || f.getAbsolutePath().endsWith(".png")) {
                            data.setName(f.getName());
                            data.setPath(f.getPath());
                            data.setSize(f.length());
                            if (f.length() > 0)
                                docList.add(data);
                        }


                    }
                }
            }

        } else {
            if (mlist != null) {
                for (File f : mlist) {
                    if (f.isDirectory()) {
                        docList.addAll(getListFiles(new File(f.getAbsolutePath(), "videos")));
                    } else {
                        CommonModel data = new CommonModel();
                        if (f.getAbsolutePath().endsWith("mp4")) {
                            data.setName(f.getName());
                            data.setPath(f.getPath());
                            data.setSize(f.length());

                            if (f.length() > 0)
                                docList.add(data);
                        }
                    }
                }
            }
        }
        return docList;
    }

    public float getTotalStorage() {
        long totalStorage;
        long totalStorage1;

        String p1 = Environment.getExternalStorageDirectory().getPath();
        String p2 =  Environment.getRootDirectory().getPath();
         StatFs statFs = new StatFs(p1);
         StatFs statFs1 = new StatFs(p2);
         totalStorage = (statFs.getBlockSizeLong() * statFs.getBlockCountLong());
        totalStorage1 = (statFs1.getBlockSizeLong() * statFs1.getBlockCountLong());


        return totalStorage+totalStorage1;
    }

    public float getTotalStorage(String file) {
        long totalStorage;

        StatFs statFs = new StatFs(file);
        totalStorage = (statFs.getBlockSizeLong() * statFs.getBlockCountLong());

        return totalStorage;
    }


    public long getAvailableStorage(String file) {
        long megAvailable;

        StatFs stat = new StatFs(file);
        long bytesAvailable;

        bytesAvailable = stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
        megAvailable = bytesAvailable;
        return megAvailable;
    }

    public static long getAvailableStorage() {
        long megAvailable;

        String path1 = Environment.getExternalStorageDirectory().getPath();
//        String path2 = Environment.getDataDirectory().getPath();
        StatFs stat = new StatFs(path1);
//        StatFs stat1 = new StatFs(path2);

        long bytesAvailable;

        bytesAvailable = stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
        megAvailable = bytesAvailable;
        return megAvailable;
    }


//


    public List<String> getActiveApps() {

        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        List<String> list = new ArrayList<>();

        for (ApplicationInfo packageInfo : packages) {

            if (isSTOPPED(packageInfo)) {
                if (!list.contains(packageInfo.packageName)) {
                    list.add(packageInfo.packageName);
                }
            }
        }
        return list;
    }

    public List<String> getSystemActiveApps() {

        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        List<String> list = new ArrayList<>();

        for (ApplicationInfo packageInfo : packages) {
            if (isSTOPPED(packageInfo) && isSYSTEM(packageInfo)) {
                if (!list.contains(packageInfo.packageName)) {
                    list.add(packageInfo.packageName);
                }
            }
        }
        return list;
    }


    private boolean isSTOPPED(ApplicationInfo pkgInfo) {

        return ((pkgInfo.flags & ApplicationInfo.FLAG_STOPPED) == 0);
    }

    private boolean isSYSTEM(ApplicationInfo pkgInfo) {

        return ((pkgInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    public boolean isSystemPackage(ResolveInfo resolveInfo) {

        return ((resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }


    public String GetAppName(String ApkPackageName) {

        String Name = "";

        ApplicationInfo applicationInfo;

        PackageManager packageManager = context.getPackageManager();

        try {

            applicationInfo = packageManager.getApplicationInfo(ApkPackageName, 0);

            if (applicationInfo != null) {


                Name = (String) packageManager.getApplicationLabel(applicationInfo);

            }

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }
        return Name;
    }


    public float getPercentage(float totalData, float usedData) {

        return (usedData * 100 / totalData);
    }


    public List<String> GetAllInstalledApkInfo() {

        List<String> ApkPackageName = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);

        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent, 0);

        for (ResolveInfo resolveInfo : resolveInfoList) {

            ActivityInfo activityInfo = resolveInfo.activityInfo;

            if (isSystemPackage(resolveInfo) || !isSystemPackage(resolveInfo)) {
                if (!ApkPackageName.contains(activityInfo.applicationInfo.packageName)) {
                    ApkPackageName.add(activityInfo.applicationInfo.packageName);
                }
            }
        }

        return ApkPackageName;

    }

    // return left string from first ,
    public static String getRightStringToThePoint(String s, String pointString) {
        String d = s;
        String ssDate = d.substring(d.lastIndexOf(pointString));
        return ssDate;
    }

    public void moveFile(String sourcePath, String finalDir) {

        String lastName = getRightStringToThePoint(sourcePath, "/");

        for (String path : getExternalMounts()) {
            destinationPath = path + "/" + finalDir + lastName;
            folder = new File(path + "/" + finalDir);
        }


        if (!folder.exists()) {
            folder.mkdirs();
            //                Toast.makeText(context, "created", Toast.LENGTH_SHORT).show();
            //                Toast.makeText(context, "not created", Toast.LENGTH_SHORT).show();

        }
        File sourceFile = new File(sourcePath);

        File destinationFile = new File(destinationPath);

        sourceFile.renameTo(destinationFile);

    }

    public ArrayList<String> getExternalMounts() {
        final ArrayList<String> out = new ArrayList<>();
        String reg = "(?i).*vold.*(vfat|ntfs|exfat|fat32|ext3|ext4).*rw.*";
        String s = "";
        try {
            final Process process = new ProcessBuilder().command("mount")
                    .redirectErrorStream(true).start();
            process.waitFor();
            final InputStream is = process.getInputStream();
            final byte[] buffer = new byte[1024];
            while (is.read(buffer) != -1) {
                s = s + new String(buffer);
            }
            is.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        // parse output
        final String[] lines = s.split("\n");
        for (String line : lines) {
            if (!line.toLowerCase(Locale.US).contains("asec")) {
                if (line.matches(reg)) {
                    String[] parts = line.split(" ");
                    for (String part : parts) {
                        if (part.startsWith("/"))
                            if (!part.toLowerCase(Locale.US).contains("vold"))
                                out.add(part);
                    }
                }
            }
        }
        return out;
    }

    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    public long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
//            return formatSize(availableBlocks * blockSize);
            return availableBlocks * blockSize;
        } else {
            return 0;
        }
    }

    public long getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long totalBlocks = stat.getBlockCountLong();
//            return formatSize(totalBlocks * blockSize);
            return totalBlocks * blockSize;
        } else {
            return 0;
        }
    }


    public void copyFileOrDirectory(String srcDir) {

        try {
            File src = new File(srcDir);
            File dst = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/DCIM/phone_repair/p_repair" + src.getName());

            if (src.isDirectory()) {

                String[] files = src.list();
                int filesLength = 0;
                if (files != null) {
                    filesLength = files.length;
                }
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    copyFileOrDirectory(src1);
                }
            } else {
                copyFile(src, dst);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(new File(dst.getAbsolutePath()))));
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Not Saved", Toast.LENGTH_SHORT).show();
        }
    }

    public void copyFile(File sourceFile, File destFile) throws IOException {
        if (!Objects.requireNonNull(destFile.getParentFile()).exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        try (FileChannel source = new FileInputStream(sourceFile).getChannel(); FileChannel destination = new FileOutputStream(destFile).getChannel()) {
            destination.transferFrom(source, 0, source.size());
        }
    }

    @SuppressLint("Recycle")
    public List<CommonModel> getAllImagePaths() {
        List<CommonModel> list = new ArrayList<>();
        CommonModel file;
        Uri uri;
        Cursor cursor;
        int column_index_data;
        String absolutePath = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String sortOrder = MediaStore.MediaColumns.DATE_MODIFIED + " DESC";
        cursor = context.getContentResolver().query(uri, null, null,
                null, sortOrder);
        assert cursor != null;
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext()) {
            absolutePath = cursor.getString(column_index_data);
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE));
            long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE));
//            Long size = (new File(absolutePathOfVideo)).length();
            file = new CommonModel();
            file.setPath(absolutePath);
//            file.setSize(size);
//            file.setType(FileTypes.ImageType);
            if (/*isPhoto(absolutePathOfVideo) &&*/ size > 0)
                list.add(file);
        }
        return list;
    }

    public List<CommonModel> getAllVideosPaths() {
        List<CommonModel> list = new ArrayList<>();
        CommonModel file;
        Uri uri;
        Cursor cursor;
        int column_index_data;
        String absolutePath = null;
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String sortOrder = MediaStore.MediaColumns.DATE_MODIFIED + " DESC";
        cursor = context.getContentResolver().query(uri, null, null,
                null, sortOrder);
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext()) {
            absolutePath = cursor.getString(column_index_data);
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE));
            long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE));
//            Long size = (new File(absolutePathOfVideo)).length();
            file = new CommonModel();
            file.setPath(absolutePath);
//            file.setSize(size);
//            file.setType(FileTypes.ImageType);
            if (/*isPhoto(absolutePathOfVideo) &&*/ size > 0)
                list.add(file);
        }
        return list;
    }

    public List<CommonModel> getAllAudiosPaths() {
        List<CommonModel> list = new ArrayList<>();
        CommonModel file;
        Uri uri;
        Cursor cursor;
        int column_index_data;
        String absolutePath = null;
        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String sortOrder = MediaStore.MediaColumns.DATE_MODIFIED + " DESC";
        cursor = context.getContentResolver().query(uri, null, null,
                null, sortOrder);
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext()) {
            absolutePath = cursor.getString(column_index_data);
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE));
            long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE));
//            Long size = (new File(absolutePathOfVideo)).length();
            file = new CommonModel();
            file.setPath(absolutePath);
            file.setName(title);
//            file.setSize(size);
//            file.setType(FileTypes.ImageType);
            if (/*isPhoto(absolutePathOfVideo) &&*/ size > 0)
                list.add(file);
        }
        return list;
    }


    public List<CommonModel> getAllDocs(String path) {
        File fold = new File(path);
        List<CommonModel> docList = new ArrayList<>();
        File[] mlist = fold.listFiles();
        File[] mFilelist = fold.listFiles(new AllDoFilter());
        if (mlist != null) {
            for (File f : mlist) {
                if (f.isDirectory()) {
                    List<CommonModel> fList = getAllDocs(f.getAbsolutePath());
                    docList.addAll(fList);
                }
            }
        }
        if (mFilelist != null) {
            for (File f : mFilelist) {
                CommonModel doc = new CommonModel();
                doc.setName(f.getName());
                //                doc.setSize(f.length());
                //                doc.setType(FileTypes.DocumentType);
                doc.setPath(f.getAbsolutePath());
                if (f.length() > 0)
                    docList.add(doc);
            }
        }
        return docList;
    }


    public List<CommonModel> getVideos(String path) {
        File fold = new File(path);
        List<CommonModel> docList = new ArrayList<>();
        File[] mlist = fold.listFiles();
        File[] mFilelist = fold.listFiles();
        if (mlist != null) {
            for (File f : mlist) {
                if (f.isDirectory()) {
                    List<CommonModel> fList = getVideos(f.getAbsolutePath());
                    docList.addAll(fList);
                }
            }
        }
        if (mFilelist != null) {
            for (File f : mFilelist) {
                CommonModel doc = new CommonModel();
                doc.setName(f.getName());
                //                doc.setSize(f.length());
                //                doc.setType(FileTypes.DocumentType);
                doc.setPath(f.getAbsolutePath());
                if (f.length() > 0)
                    docList.add(doc);
            }
        }
        return docList;
    }

    public List<CommonModel> getImages(String path) {
        File fold = new File(path);
        List<CommonModel> docList = new ArrayList<>();
        File[] mlist = fold.listFiles();
        File[] mFilelist = fold.listFiles();
        if (mlist != null) {
            for (File f : mlist) {
                if (f.isDirectory()) {
                    List<CommonModel> fList = getImages(f.getAbsolutePath());
                    docList.addAll(fList);
                }
            }
        }
        if (mFilelist != null) {
            for (File f : mFilelist) {
                CommonModel img = new CommonModel();
                img.setName(f.getName());
                //                doc.setSize(f.length());
                //                doc.setType(FileTypes.DocumentType);
                img.setPath(f.getAbsolutePath());
                if (f.length() > 0)
                    docList.add(img);
            }
        }
        return docList;
    }


    public List<CommonModel> getAllPackages(String path) {
        File fold = new File(path);
        List<CommonModel> docList = new ArrayList<>();
        File[] mlist = fold.listFiles();
        File[] mFilelist = fold.listFiles(new AllPackagesFilter());
        if (mlist != null) {
            for (File f : mlist) {
                if (f.isDirectory()) {
                    List<CommonModel> fList = getAllPackages(f.getAbsolutePath());
                    docList.addAll(fList);
                }
            }
            if (mFilelist != null) {
                for (File f : mFilelist) {
                    CommonModel doc = new CommonModel();
                    doc.setName(f.getName());
                    //                doc.setSize(f.length());
                    //                doc.setType(FileTypes.DocumentType);
                    doc.setPath(f.getAbsolutePath());
                    if (f.length() > 0)
                        docList.add(doc);
                }
            }
        }
        return docList;
    }

    public float getAllSize(String path) {
        float docSize = 0;
        File fold = new File(path);
        File[] mlist = fold.listFiles();
        File[] mFilelist = fold.listFiles();
        if (mlist != null)
            for (File f : mlist) {
                if (f.isDirectory()) {
                    getAllSize(f.getAbsolutePath());
                }
            }
        if (mlist != null)
            if (mFilelist != null) {
                for (File f : mFilelist) {
                    docSize = docSize + f.length();
                }
            }
        return docSize;
    }


    public float getAllDocSize(String path) {

        File fold = new File(path);
        File[] mlist = fold.listFiles();
        File[] mFilelist = fold.listFiles(new AllDoFilter());
        if (mlist != null) {
            for (File f : mlist) {
                if (f.isDirectory()) {
                    getAllDocSize(f.getAbsolutePath());
                }
            }
        }
        if (mFilelist != null) {
            for (File f : mFilelist) {
                docSize = docSize + f.length();
            }
        }
        return docSize;
    }

    public float getAudioSize(String path) {

        File fold = new File(path);
        File[] mlist = fold.listFiles();
        File[] mFilelist = fold.listFiles(new AllAudioFilter());
        if (mlist != null) {
            for (File f : mlist) {
                if (f.isDirectory()) {
                    getAllDocSize(f.getAbsolutePath());
                }
            }
        }
        if (mFilelist != null) {
            for (File f : mFilelist) {
                audioSize = audioSize + f.length();
            }
        }
        return audioSize;
    }

    public float getAllPkgsSize(String path) {
        File fold = new File(path);
        File[] mlist = fold.listFiles();
        File[] mFilelist = fold.listFiles(new AllPackagesFilter());
        if (mlist != null) {
            for (File f : mlist) {
                if (f.isDirectory()) {
                    getAllPkgsSize(f.getAbsolutePath());
                }
            }
        }

        if (mFilelist != null) {
            for (File f : mFilelist) {
                size = size + f.length();
            }
        }
        return size;
    }


    // return images audio videos size
    @SuppressLint("Recycle")
    public float getAllIAAsSize(String forWhat) {
        Uri uri = null;
        Cursor cursor = null;
        String[] projection = new String[0];
        float size = 0;
        if (forWhat.matches("videos")) {
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            projection = new String[]{
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.DISPLAY_NAME,
                    MediaStore.Video.Media.SIZE
            };
        } else if (forWhat.matches("images")) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            projection = new String[]{
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.SIZE
            };

        } else if (forWhat.matches("audios")) {
            uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            projection = new String[]{
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.SIZE
            };

        }

        String sortOrder = MediaStore.MediaColumns.DATE_MODIFIED + " DESC";
        if (uri != null) {
            cursor = context.getContentResolver().query(uri, projection, null,
                    null, sortOrder);
        }
        assert cursor != null;
        while (cursor.moveToNext()) {
            size = size + cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE));

        }
        return size;
    }


    //delete file taken from media store
    public void scanaddedFile(String path) {
        try {
            MediaScannerConnection.scanFile(context, new String[]{path},
                    null, (path1, uri) -> context.getContentResolver()
                            .delete(uri, null, null));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static class AllDoFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            String path = pathname.getPath();
            return (path.endsWith(".ods")
                    || path.endsWith(".xls")
                    || path.endsWith(".xlsx")
                    || path.endsWith(".doc")
                    || path.endsWith(".odt")
                    || path.endsWith(".docx")
                    || path.endsWith(".pps")
                    || path.endsWith(".pptx")
                    || path.endsWith(".ppt")
                    || path.endsWith(".PDF")
                    || path.endsWith(".pdf")
                    || path.endsWith(".txt")
                    || path.endsWith(".ziip")
                    || path.endsWith(".7z")
                    || path.endsWith(".rar")
                    || path.endsWith(".rpm")
                    || path.endsWith(".tar.gz")
                    || path.endsWith(".z")
                    || path.endsWith(".zip"));
        }
    }

    public static class AllImgFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            String path = pathname.getPath();
            return (path.endsWith(".jpeg") ||
                    path.endsWith(".jpg") ||
                    path.endsWith(".png"));
        }
    }

    public static class AllAudioFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            String path = pathname.getPath();
            return (path.endsWith(".mp3") ||
                    path.endsWith(".opus") ||
                    path.endsWith(".m4a") ||
                    path.endsWith(".amr") ||
                    path.endsWith(".mpa") ||
                    path.endsWith(".mid") ||
                    path.endsWith(".ogg") ||
                    path.endsWith(".wav") ||
                    path.endsWith(".wma") ||
                    path.endsWith(".wma") ||
                    path.endsWith(".wpl") ||
                    path.endsWith(".cda") ||
                    path.endsWith(".aif"));
        }
    }

    public static class AllPackagesFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            String path = pathname.getPath();
            return (path.endsWith(".apk"));
        }
    }

    @SuppressLint("DefaultLocale")
    public String getCalculatedDataSize(float size) {
        String sizePrefix = "Bytes";
        float finalSize = size;
        if (size >= 1024) {
            float sizeKb = size / 1024;
            sizePrefix = "KB";
            finalSize = sizeKb;
            if (sizeKb >= 1024) {
                float sizeMB = sizeKb / 1024;
                sizePrefix = "MB";
                finalSize = sizeMB;
                if (sizeMB >= 1024) {
                    float sizeGb = sizeMB / 1024;
                    sizePrefix = "GB";
                    finalSize = sizeGb;
                }
            }
        }
        return String.format("%.2f", finalSize) + sizePrefix;
    }

    public float getCalculatedDataSizeFloat(float size) {
        float finalSize = size;
        if (size >= 1024) {
            float sizeKb = size / 1024;
            finalSize = sizeKb;
            if (sizeKb >= 1024) {
                float sizeMB = sizeKb / 1024;
                finalSize = sizeMB;
                if (sizeMB >= 1024) {
                    float sizeGb = sizeMB / 1024;
                    finalSize = sizeGb;

                }
            }
        }
        return finalSize;
    }

    public float cpuTemperature() {


        Process process;
        try {
            process = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp");
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if (line != null) {
                float temp = Float.parseFloat(line);
                return getCelsius(temp / 1000.0f);
            } else {
                return getCelsius(51.0f);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return getCelsius(0.0f);
        }

    }

    public float getCelsius(float fahren) {
        return (fahren - 32) / (1.8f);

    }

    public float getFahrenheit(float cel) {
        return (cel * 1.8f) + 32;

    }


    public float getCalculatedDataSizeMB(float size) {
        float sizeBytes = size;
        float finalSize = size;
        if (sizeBytes >= 1024) {
            float sizeKb = sizeBytes / 1024;
            finalSize = sizeKb;
            if (sizeKb >= 1024) {
                float sizeMB = sizeKb / 1024;
                finalSize = sizeMB;
            }
        }
        return finalSize;
    }

    public int memoryFun(int total)
    {
        if (total<=2)
        {
            return 2;
        }
        else
        if (total >2 && total<4)
        {
            return 4;

        }
        else
        if (total>4 && total <8)
        {
            return 8;
        }
        else
        if (total>10 && total <16)
        {

            return 16;

        }
        else
        if (total>16 && total <32)
        {
            return 32;

        }
        else
        if (total>32 && total <64)
        {

            return 64;

        } else if (total > 64 && total < 128) {
            return 128;


        }else if (total > 128 && total < 256) {

            return 256;

        }
        else if (total > 256 && total < 512) {

            return 512;

        }else if (total > 512 && total < 1024) {
            return 1024;

        }
        return 0;

    }


}