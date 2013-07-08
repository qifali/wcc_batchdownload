package com.qifa;

import java.io.InputStream;

public class DocInfo {
    private InputStream input;
    private String docName;

    public void setInput(InputStream in) {
        this.input = in;
    }

    public InputStream getInput() {
        return this.input;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocName() {
        return this.docName;
    }
}
