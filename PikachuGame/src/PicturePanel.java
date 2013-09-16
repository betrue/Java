import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PicturePanel extends javax.swing.JPanel {
	
	private BufferedImage image = null;
	
	public PicturePanel() {
		initComponents();
	}
	
	// инициализация
	private void initComponents() {
		Dimension panelSize = new Dimension(56, 72);
		setPreferredSize(panelSize);
		setSize(panelSize);
		setLayout(null);
	}
	
	// прорисовка
	public void paint(Graphics g) {
		if (image != null)
			g.drawImage(image, 0, 0, null);
		super.paintChildren(g);
		super.paintBorder(g);
	}
	
	// возврат картинки
	public BufferedImage getImage() {
		return image;
	}
	
	// установка картинки
	public void setImage(BufferedImage image) {
		this.image = image;
		repaint();
	}
	
	// установка цвета (альтернатива картинке)
	public void setColor(Color color) {
		this.setBackground(color);
	}
	
}
