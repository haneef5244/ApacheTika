package com.tika.assignment;

import org.apache.tika.detect.Detector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.*;

public class TikaAnalysis {

    public static String dtectDocTypeUsingDetector(InputStream stream) throws IOException {
        BufferedInputStream buff = new BufferedInputStream(stream);
        AutoDetectParser parser = new AutoDetectParser();
        Detector detector = parser.getDetector();
        Metadata metadata = new Metadata();
        MediaType mediaType = detector.detect(buff, metadata);
        return mediaType.toString();
    }


    public static String[] extractMetaDataUsingParser(InputStream stream) {
        Parser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();

        try {
            ((AutoDetectParser) parser).parse(stream, handler, metadata, context);
        } catch (Exception e) {
            System.out.println("Error parsing");
        }
        for (String i : metadata.names()) {
            System.out.println(i);
        }
        return metadata.names();
    }

    public static String extractContentUsingParser(InputStream stream)
            throws IOException, TikaException, SAXException {

        Parser parser = new AutoDetectParser();
        ContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();

        parser.parse(stream, handler, metadata, context);
        System.out.println("Content : ");
        System.out.println(handler.toString());
        return handler.toString();
    }


}
