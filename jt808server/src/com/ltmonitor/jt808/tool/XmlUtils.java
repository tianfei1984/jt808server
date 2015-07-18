 package com.ltmonitor.jt808.tool;
 
 import java.io.File;
 import java.io.StringReader;
 import java.io.Writer;
 import org.dom4j.Document;
 import org.dom4j.io.OutputFormat;
 import org.dom4j.io.SAXReader;
 import org.dom4j.io.XMLWriter;
 
 public class XmlUtils
 {
   public static final String ENCODING = "UTF-8";
 
   public static Document parseXml(File inputXml)
   {
     try
     {
       SAXReader saxReader = new SAXReader();
       return saxReader.read(inputXml);
     }
     catch (Exception ex)
     {
       ex.printStackTrace();
     }
     return null;
   }
 
   public static Document parseXml(String path)
   {
     try
     {
       File inputXml = new File(path);
       SAXReader saxReader = new SAXReader();
       return saxReader.read(inputXml);
     }
     catch (Exception ex)
     {
       ex.printStackTrace();
     }
     return null;
   }
 
   public static Document createDocument(String xml)
   {
     try
     {
       StringReader reader = new StringReader(xml);
       SAXReader saxreader = new SAXReader();
       return saxreader.read(reader);
     }
     catch (Exception ex)
     {
       ex.printStackTrace();
     }
     return null;
   }
 
   public static void printXml(Document document, Writer writer)
   {
     try
     {
       OutputFormat format = new OutputFormat("\t", true, "UTF-8");
       XMLWriter xmlWriter = new XMLWriter(writer, format);
       xmlWriter.write(document);
       xmlWriter.close();
     }
     catch (Exception ex)
     {
       ex.printStackTrace();
     }
   }
 }

