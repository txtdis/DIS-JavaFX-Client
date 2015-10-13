package ph.txtdis.fx.control;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import ph.txtdis.fx.ImageUtil;
import ph.txtdis.util.Binary;

public class ImageDisplay extends Label {

	private static byte[] imagebytes;
	private String image;

	public ImageDisplay(String image) {
		this.image = image;
		init();
	}

	public byte[] getImage() {
		return imagebytes;
	}

	public void setImage(byte[] bytes) {
		imagebytes = bytes;
		InputStream is = useDefaultForNull(imagebytes);
		setImage(is);
	}

	public void setPadding(double top, double right, double bottom, double left) {
		setPadding(new Insets(top, right, bottom, left));
	}

	private void init() {
		setDefaultImage();
		setTooltip(new Tooltip("Double-click to load image"));
		addEventFilter(MouseEvent.MOUSE_CLICKED, event -> loadImageIfDoubleClick(event));
	}

	private void loadImageFile() {
		File file = ImageUtil.selectImageFile(this);
		if (file != null)
			setImage(file);
	}

	private void loadImageIfDoubleClick(MouseEvent event) {
		if (event.getClickCount() > 1)
			loadImageFile();
		event.consume();
	}

	private void setDefaultImage() {
		InputStream is = ImageUtil.defaultImageStream(image);
		setImage(is);
	}

	private void setImage(File file) {
		imagebytes = Binary.toBytes(file);
		InputStream is = new ByteArrayInputStream(imagebytes);
		setImage(is);
	}

	private void setImage(InputStream is) {
		ImageView iv = ImageUtil.toImageView(is);
		setGraphic(iv);
	}

	private InputStream useDefaultForNull(byte[] bytes) {
		if (bytes == null)
			return ImageUtil.defaultImageStream(image);
		return new ByteArrayInputStream(bytes);
	}
}
