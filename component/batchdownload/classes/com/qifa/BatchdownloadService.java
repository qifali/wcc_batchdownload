package com.qifa;

import intradoc.common.ServiceException;

import intradoc.data.DataBinder;
import intradoc.data.DataException;

import intradoc.server.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.List;

public class BatchdownloadService extends Service {
    private BaseServiceHelper serviceHelper = new BaseServiceHelper();

    public void setServiceHelper(BaseServiceHelper serviceHelper) {
        this.serviceHelper = serviceHelper;
    }

    public BaseServiceHelper getServiceHelper() {
        return this.serviceHelper;
    }

    public void readyDownloadZipFile() throws DataException, ServiceException,
                                              FileNotFoundException,
                                              IOException {
        String docs = this.m_binder.get("SelectedDocs");
        String[] dDocNames = docs.split(",");
        List docInfos = new ArrayList();
        for (String doc : dDocNames) {
            DocInfo temp = getDocInputStream(doc);
            docInfos.add(temp);
        }
        System.out.println(this.m_binder.getEnvironmentValue("Domaindri"));

        String fileName =
            getCurrentTime() + CustomConstant.BATCH_DOWNLOAD_ZIPFILE_NAME +
            ".zip";

        String domaindir = this.m_binder.getEnvironmentValue("Domaindri");
        String tempDir =
            domaindir + CustomConstant.BATCH_DOWNLOAD_ZIPFILE_PATH +
            File.separator + fileName;

        String zipFilePath = tempDir;
        ZipUtil.writeZipByStreams(docInfos, zipFilePath);
        this.m_binder.putLocal("zipFilePath",
                               CustomConstant.EXCEL_TEMP_FOLDER +
                               File.separator + fileName);
    }

    private String getCurrentTime() {
        return System.currentTimeMillis() + "";
    }

    public DocInfo getDocInputStream(String dDocName) throws DataException,
                                                             ServiceException,
                                                             FileNotFoundException {
        DataBinder queryBinder = new DataBinder();
        queryBinder.putLocal("IdcService", "GET_FILE");
        queryBinder.putLocal("dDocName", dDocName);
        queryBinder.putLocal("RevisionSelectionMethod", "LatestReleased");

        getServiceHelper().executeService(queryBinder, CustomConstant.ADMIN,
                                          false);

        String filePath = queryBinder.get("FilePath");
        File f = new File(filePath);

        InputStream in = new FileInputStream(f);
        DocInfo doc = new DocInfo();
        doc.setInput(in);
        doc.setDocName(queryBinder.get("dDocTitle") + "." +
                       queryBinder.get("dExtension"));

        return doc;
    }
}
