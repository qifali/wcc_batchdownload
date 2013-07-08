package com.qifa;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.List;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

public class ZipUtil {
    public void createZip(String sourcePath,
                          String zipPath) throws IOException {
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipPath);
            zos = new ZipOutputStream(fos);

            writeZip(new File(sourcePath), "", zos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (zos != null)
                    zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeZip(File file, String parentPath,
                          ZipOutputStream zos) throws IOException {
        if (file.exists())
            if (file.isDirectory()) {
                parentPath = parentPath + file.getName() + File.separator;
                File[] files = file.listFiles();
                for (File f : files)
                    writeZip(f, parentPath, zos);
            } else {
                FileInputStream fis = null;
                DataInputStream dis = null;

                fis = new FileInputStream(file);
                dis = new DataInputStream(new BufferedInputStream(fis));
                ZipEntry ze = new ZipEntry(parentPath + file.getName());
                zos.setEncoding("utf-8");
                zos.putNextEntry(ze);

                byte[] content = new byte[1024];
                int len;
                while ((len = fis.read(content)) != -1) {
                    zos.write(content, 0, len);
                    zos.flush();
                }

                if (dis != null)
                    dis.close();
            }
    }

    public static void writeZipByStreams(List<DocInfo> docs,
                                         String zipPath) throws IOException {
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        DataInputStream dis = null;
        fos = new FileOutputStream(zipPath);
        zos = new ZipOutputStream(fos);
        for (DocInfo doc : docs) {
            dis = new DataInputStream(new BufferedInputStream(doc.getInput()));
            ZipEntry ze = new ZipEntry(doc.getDocName());
            zos.setEncoding("utf-8");
            zos.putNextEntry(ze);
            byte[] content = new byte[1024];
            int len;
            while ((len = doc.getInput().read(content)) != -1) {
                zos.write(content, 0, len);
                zos.flush();
            }
        }
        if (zos != null)
            zos.close();
    }
}
