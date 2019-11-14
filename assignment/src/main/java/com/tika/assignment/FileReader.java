package com.tika.assignment;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.apache.tika.exception.TikaException;
import org.springframework.core.io.ClassPathResource;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;

public class FileReader {


    public void detectTypeUsingDetector(String fileName) throws IOException {
        File file = new ClassPathResource(fileName).getFile();
        InputStream targetStream = new FileInputStream(file);
        writeToFile("output-mime-type-using-detector-pdf", TikaAnalysis.dtectDocTypeUsingDetector(targetStream));
        targetStream.close();
    }

    public void extractContentUsingParser(String fileName, String fileExt) throws IOException {
        File file = new ClassPathResource(fileName).getFile();
        InputStream targetStream = new FileInputStream(file);
        String prefix = String.format("output-content-%s", fileExt);
        try {
            System.out.println("Extracting content using parser:");
            String content = TikaAnalysis.extractContentUsingParser(targetStream);
            writeToFile(prefix, content);
        } catch (Exception e) {
            System.out.println("Error parsing");
            System.out.println(e);
        } finally {
            targetStream.close();
        }
    }

    public void extractMetaDataUsingParser(String fileName, String fileExt) throws IOException{

        File file = new ClassPathResource(fileName).getFile();
        InputStream targetStream = new FileInputStream(file);
        System.out.println("Extracting meta data using parser : ");
        String[] data = TikaAnalysis.extractMetaDataUsingParser(targetStream);
        String prefix = String.format("output-metadata-%s", fileExt);
        writeToFile(prefix, data);
        targetStream.close();

    }

    public void writeToFile(String fileName, String contents) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(String.format("src/output/%s%s", fileName, ".txt")));
        try {
            bufferedWriter.write(contents);
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            bufferedWriter.close();
        }
    }

    public void writeToFile(String fileName, String[] contents) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(String.format("src/output/%s%s", fileName, ".txt")));
        try {
            for (String s : contents) {
                bufferedWriter.write(s);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            bufferedWriter.close();
        }
    }

    public void convertPdfToPng(String filename, String extension) throws IOException, SAXException, TikaException {
        String fileNameMetadata = "metadata-converted-to-png-";
        String fileNameContent = "content-converted-to-png-";
        File file = new ClassPathResource(filename).getFile();
        PDDocument document = PDDocument.load(file);
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        for (int page = 0; page < document.getNumberOfPages(); ++page) {
            BufferedImage bim = pdfRenderer.renderImageWithDPI(
                    page, 300, ImageType.BINARY);
            ImageIOUtil.writeImage(
                    bim, String.format("src/output/%s-%d.%s", "PdfFile", page + 1, extension), 300);
            ImageIO.write(bim, "png", new File(String.format("src/output/%s-%d.%s", "PdfFile", page + 1, extension)));

            byte[] bytes = ((DataBufferByte)(bim).getRaster().getDataBuffer()).getData();
            InputStream is = new ByteArrayInputStream(bytes);
            try {
                writeToFile(String.format("%s%s", fileNameMetadata, page + 1), TikaAnalysis.extractMetaDataUsingParser(is));
                is = new ByteArrayInputStream(bytes);
                writeToFile(String.format("%s%s", fileNameContent, page + 1), TikaAnalysis.extractContentUsingParser(is));
            } catch (IOException | TikaException | SAXException e) {
                throw e;
            }
            is.close();

        }

        document.close();
    }

}
