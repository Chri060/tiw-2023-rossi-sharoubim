package com.christian.rossi.progetto_tiw_2023.Utils;

import javax.servlet.http.Part;

public class FileNameChanger {
    public static String getFileName(Part part, String articleID) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename"))
                return (articleID + ".jpeg");
        }
        return "null";
    }
}
