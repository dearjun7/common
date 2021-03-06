package com.bh.subject.std.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

public class XmlUtil {

    public static Document buildDocument(File file) throws Exception {
        BufferedInputStream is = null;
        Document document = null;
        try {
            SAXBuilder builder = new SAXBuilder();
            is = new BufferedInputStream(new FileInputStream(file));
            document = builder.build(is);
        } catch(Exception e) {
            throw new Exception(e);
        } finally {
            try {
                if(is != null) is.close();
            } catch(Exception e) {};
        }

        return document;
    }

    public static Document buildDocument(String xmlString) throws Exception {
        BufferedInputStream is = null;
        Document document = null;
        try {
            SAXBuilder builder = new SAXBuilder();
            is = new BufferedInputStream(new ByteArrayInputStream(xmlString.getBytes("UTF-8")));
            document = builder.build(is);
        } catch(Exception e) {
            throw new Exception(e);
        } finally {
            try {
                if(is != null) is.close();
            } catch(Exception e) {};
        }

        return document;
    }

    public static void saveElementToFile(Element rootElement, String elementName, String fileName) throws Exception {
        Element wdlElement = null;
        if("".equals(elementName) || null == elementName) {
            wdlElement = rootElement;
        } else {
            wdlElement = rootElement.getChild(elementName);
        }

        BufferedOutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(fileName));
            XMLOutputter outputter = new XMLOutputter();
            outputter.output(wdlElement, os);
        } catch(Exception e) {
            throw new Exception(e);
        } finally {
            try {
                if(os != null) os.close();
            } catch(Exception e) {};
        }
    }

    public static InputStream saveElementToStream(Element rootElement, String elementName) throws Exception {
        Element wdlElement = null;
        if("".equals(elementName) || null == elementName) {
            wdlElement = rootElement;
        } else {
            wdlElement = rootElement.getChild(elementName);
        }

        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            XMLOutputter outputter = new XMLOutputter();
            outputter.output(wdlElement, os);
            return new ByteArrayInputStream(os.toByteArray());
        } catch(Exception e) {
            throw new Exception(e);
        } finally {
            try {
                if(os != null) os.close();
            } catch(Exception e) {};
        }
    }

    public static void printDocument(OutputStream out, Element rootElement, String elementName) throws Exception {
        Element wdlElement = null;
        if("".equals(elementName) || null == elementName) {
            wdlElement = rootElement;
        } else {
            wdlElement = rootElement.getChild(elementName);
        }

        XMLOutputter outputter = new XMLOutputter();
        outputter.output(wdlElement, out);

    }

    public static void printDocument(OutputStream out, Document document) throws Exception {
        XMLOutputter outputter = new XMLOutputter();
        outputter.output(document, out);
    }

}
