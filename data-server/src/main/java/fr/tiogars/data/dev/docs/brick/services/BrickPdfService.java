package fr.tiogars.data.dev.docs.brick.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.brick.entities.BrickEntity;
import fr.tiogars.data.dev.docs.brick.repositories.BrickRepository;

@Service
public class BrickPdfService {

    private static final float MARGIN = 50f;
    private static final float PAGE_WIDTH = PDRectangle.A4.getWidth();
    private static final float PAGE_HEIGHT = PDRectangle.A4.getHeight();
    private static final float CONTENT_WIDTH = PAGE_WIDTH - 2 * MARGIN;

    private final BrickRepository brickRepository;

    public BrickPdfService(BrickRepository brickRepository) {
        this.brickRepository = brickRepository;
    }

    public byte[] generateCatalog() throws IOException {
        List<BrickEntity> bricks = brickRepository.findAllByOrderByNumberAsc();

        try (PDDocument document = new PDDocument()) {
            if (bricks.isEmpty()) {
                document.addPage(new PDPage(PDRectangle.A4));
            } else {
                for (BrickEntity brick : bricks) {
                    addBrickPage(document, brick);
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    private void addBrickPage(PDDocument document, BrickEntity brick) throws IOException {
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDFont fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        PDFont fontRegular = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

        try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
            float yPos = PAGE_HEIGHT - MARGIN;

            // Numéro de brique
            cs.beginText();
            cs.setFont(fontBold, 18);
            cs.newLineAtOffset(MARGIN, yPos);
            cs.showText("N\u00b0 " + safeText(brick.getNumber()));
            cs.endText();
            yPos -= 28;

            // Titre
            cs.beginText();
            cs.setFont(fontRegular, 13);
            cs.newLineAtOffset(MARGIN, yPos);
            cs.showText(safeText(brick.getTitle()));
            cs.endText();
            yPos -= 22;

            // Tags
            String tags = brick.getTags() != null ? brick.getTags() : "";
            cs.beginText();
            cs.setFont(fontRegular, 10);
            cs.newLineAtOffset(MARGIN, yPos);
            cs.showText("Tags : " + tags);
            cs.endText();
            yPos -= 20;

            // Image
            if (brick.getImageBase64() != null && !brick.getImageBase64().isBlank()) {
                yPos = drawImage(document, cs, brick.getImageBase64(), yPos);
            }
        }
    }

    private float drawImage(PDDocument document, PDPageContentStream cs, String imageBase64, float yPos)
            throws IOException {
        String base64Data = imageBase64;
        if (base64Data.contains(",")) {
            base64Data = base64Data.substring(base64Data.indexOf(',') + 1);
        }

        byte[] imageBytes;
        try {
            imageBytes = Base64.getDecoder().decode(base64Data);
        } catch (IllegalArgumentException e) {
            return yPos;
        }

        try (ByteArrayInputStream imgStream = new ByteArrayInputStream(imageBytes)) {
            BufferedImage bufferedImage = ImageIO.read(imgStream);
            if (bufferedImage == null) {
                return yPos;
            }

            PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);
            float availableHeight = yPos - MARGIN;
            float scale = Math.min(CONTENT_WIDTH / pdImage.getWidth(), availableHeight / pdImage.getHeight());
            float imgWidth = pdImage.getWidth() * scale;
            float imgHeight = pdImage.getHeight() * scale;
            float imgY = yPos - imgHeight;

            cs.drawImage(pdImage, MARGIN, imgY, imgWidth, imgHeight);
            return imgY;
        }
    }

    private static String safeText(String value) {
        return value != null ? value : "";
    }
}
