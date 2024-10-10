package pdf;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.ITextUserAgent;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;

public class Main {
    public static void main(String[] args) {
        try {
            InputStream htmlStream = Main.class.getClassLoader().getResourceAsStream("html/example.html");
            String htmlContent = new String(htmlStream.readAllBytes());

            URI mainUri = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            String outputPath = new File(mainUri).getPath() + "/convertedFromHtml.pdf";

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(htmlContent)));

            ITextRenderer renderer = new ITextRenderer();

            ITextUserAgent userAgent = new ITextUserAgent(renderer.getOutputDevice(), Math.round(renderer.getOutputDevice().getDotsPerPoint()));
            renderer.getSharedContext().setUserAgentCallback(userAgent);

            renderer.setDocument(document, null);

            try (FileOutputStream outputStream = new FileOutputStream(outputPath)) {
                renderer.layout();
                renderer.createPDF(outputStream);
            }
            System.out.println("PDF created successfully at: " + outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
