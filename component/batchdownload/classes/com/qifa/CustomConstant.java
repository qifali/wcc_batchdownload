package com.qifa;

import java.io.File;

public abstract interface CustomConstant {
    public static final String ADMIN = "weblogic";
    public static final String BATCH_DOWNLOAD_ZIPFILE_NAME = "ucm";
    public static final String BATCH_DOWNLOAD_ZIPFILE_PATH =
        "ucm" + File.separator + "cs" + File.separator + "weblayout" +
        File.separator + "resources" + File.separator + "temp";
    public static final String EXCEL_TEMP_FOLDER =
        "resources" + File.separator + "temp";
}
