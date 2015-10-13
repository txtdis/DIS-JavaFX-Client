package ph.txtdis.fx;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.VPos;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class FontIcon extends Image {

    private final static double SIZE = 256;
    private final static String FONT = "txtdis";

    public FontIcon(String text, Color color) {
        super(iconStream(text, color));
    }

    private static ByteArrayInputStream iconStream(String unicode, Color color) {
        Canvas canvas = new Canvas(SIZE, SIZE);
        setGraphicContent(unicode, color, canvas);
        return inputStream(canvas);
    }

    private static ByteArrayInputStream inputStream(Canvas canvas) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeImage(canvas, out);
        return new ByteArrayInputStream(out.toByteArray());
    }

    private static BufferedImage bufferedImage(Canvas canvas) {
        WritableImage wi = writableImage(canvas);
        return SwingFXUtils.fromFXImage(wi, null);
    }

    private static WritableImage writableImage(Canvas canvas) {
        SnapshotParameters sp = snapshotParameters();
        return canvas.snapshot(sp, null);
    }

    private static SnapshotParameters snapshotParameters() {
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        return params;
    }

    private static Font font() {
        UI.loadFont(FONT);
        return new Font(FONT, SIZE);
    }

    private static void setGraphicContent(String text, Color color, Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFont(font());
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFill(color);
        gc.fillText(text, SIZE / 2, SIZE / 2);
    }

    private static void writeImage(Canvas canvas, ByteArrayOutputStream out) {
        try {
            BufferedImage bufferedImage = bufferedImage(canvas);
            ImageIO.write(bufferedImage, "png", out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
