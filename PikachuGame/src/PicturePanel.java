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
	private Dimension panelSize = new Dimension(56, 72);
	private Dimension position = new Dimension(0, 0);
	
	public PicturePanel() {
		initComponents();
	}
	
	// инициализация
	private void initComponents() {
		setPreferredSize(panelSize);
		setSize(panelSize);
		setLayout(null);
	}
	
	// прорисовка
	public void paint(Graphics g) {
		if (image != null) {
			g.drawImage(image, 0, 0, null);
		} else {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, panelSize.width, panelSize.height);
		}
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
	
	// установка координат
	public void setPosition(Dimension position) {
		this.position = position;
	}
	
	// возврат координат
	public Dimension getPosition() {
		return position;
	}
	
}
