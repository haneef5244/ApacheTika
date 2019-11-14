package com.tika.assignment;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.IOException;

public class AssignmentApplication {

	public static void main(String[] args) {
		FileReader fr = new FileReader();
		try {
			fr.detectTypeUsingDetector("PdfFile.pdf");
			fr.extractMetaDataUsingParser("PdfFile.pdf", "pdf");
			fr.extractContentUsingParser("PdfFile.pdf", "pdf");
			fr.convertPdfToPng("PdfFile.pdf", "png");
		} catch (IOException | TikaException | SAXException e) {
			System.out.println("Error reading file +\n" +  e.getMessage());

		}
	}

}
