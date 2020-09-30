package com.cleaner.booster.phone.repairer.app.utils;

import android.content.Context;
import android.os.Environment;

import com.cleaner.booster.phone.repairer.app.models.CommonModel;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class StorageUtils {

    long folderAmount = 0;
    long totalSize = 0;

    public long deleteEmptyFolder(String dir) {

        File f = new File(dir);
        String listFiles[] = f.list();
        long totalSize = 0;
        for (String file : listFiles) {

            File folder = new File(dir + "/" + file);
            if (folder.isDirectory()) {
                totalSize += deleteEmptyFolder(folder.getAbsolutePath());
            } else {
                totalSize += folder.length();
            }
        }
        if (totalSize == 0) {
            f.delete();
        }
        return totalSize;
    }

    public void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public List<CommonModel> getAllUnUsableFile(String path) {
        File fold = new File(path);
        List<CommonModel> fileList = new ArrayList<>();
        File[] mlist = fold.listFiles();
        File[] mFilelist = fold.listFiles(new AllUnUsableFilter());
        if (mlist != null) {
            for (File f : mlist) {
                if (f.isDirectory()) {
                    List<CommonModel> fList = getAllUnUsableFile(f.getAbsolutePath());
                    fileList.addAll(fList);
                }
            }
        }
        if (mFilelist != null) {
            for (File f : mFilelist) {
                CommonModel file = new CommonModel();
                file.setName(f.getName());
                file.setSize(f.length());
                //                doc.setType(FileTypes.DocumentType);
                file.setPath(f.getAbsolutePath());
                if (f.length() > 0)
                    fileList.add(file);
            }
        }
        return fileList;
    }


    public long getLogFileSize(String path) {
        long size = 0;
        File fold = new File(path);
        File[] mlist = fold.listFiles();
        File[] mFilelist = fold.listFiles(new AllLogFilter());
        if (mlist != null) {
            for (File f : mlist) {
                if (f.isDirectory()) {
                    getLogFileSize(f.getAbsolutePath());
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

    public long getTempFileSize(String path) {
        long size = 0;
        File fold = new File(path);
        File[] mlist = fold.listFiles();
        File[] mFilelist = fold.listFiles(new AllTempFilter());
        if (mlist != null) {
            for (File f : mlist) {
                if (f.isDirectory()) {
                    getTempFileSize(f.getAbsolutePath());
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

    public long getApkFileSize(String path) {
        long size = 0;
        File fold = new File(path);
        File[] mlist = fold.listFiles();
        File[] mFilelist = fold.listFiles(new AllApkFilter());
        if (mlist != null) {
            for (File f : mlist) {
                if (f.isDirectory()) {
                    getApkFileSize(f.getAbsolutePath());
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

    public long getCachesSize(String path) {
        long size = 0;
        File fold = new File(path);
        File[] mlist = fold.listFiles();
        File[] mFilelist = fold.listFiles(new AllCacheFilter());
        if (mlist != null) {
            for (File f : mlist) {
                if (f.isDirectory()) {
                    getCachesSize(f.getAbsolutePath());
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

    public long getAllUnUsableSize(String path) {
        long size = 0;
        File fold = new File(path);
        File[] mlist = fold.listFiles();
        File[] mFilelist = fold.listFiles(new AllUnUsableFilter());
        if (mlist != null) {
            for (File f : mlist) {
                if (f.isDirectory()) {
                    getAllUnUsableSize(f.getAbsolutePath());
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


    public static class AllUnUsableFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            String path = pathname.getPath();
            return (path.endsWith(".tmp")
                    || path.endsWith(".TEMP")
                    || path.endsWith(".csi")
                    || path.endsWith(".cfa")
                    || path.endsWith(".egt")
                    || path.endsWith(".clean")
                    || path.endsWith(".cache")
                    || path.endsWith(".imagecache")
                    || path.endsWith(".dmp")
                    || path.endsWith(".log")
                    || path.endsWith(".apk")
            );
        }
    }

    public static class AllTempFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            String path = pathname.getPath();
            return (path.endsWith(".tmp") || path.endsWith(".TEMP"));
        }
    }

    public static class AllApkFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            String path = pathname.getPath();
            return (path.endsWith(".apk"));
        }
    }

    public static class AllLogFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            String path = pathname.getPath();
            return (path.endsWith(".log"));
        }
    }

    public static class AllCacheFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            String path = pathname.getPath();
            return path.endsWith(".cache");
        }
    }
}
