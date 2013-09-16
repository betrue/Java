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
	
	// �������������
	private void initComponents() {
		Dimension panelSize = new Dimension(56, 72);
		setPreferredSize(panelSize);
		setSize(panelSize);
		setLayout(null);
	}
	
	// ����������
	public void paint(Graphics g) {
		if (image != null)
			g.drawImage(image, 0, 0, null);
		super.paintChildren(g);
		super.paintBorder(g);
	}
	
	// ������� ��������
	public BufferedImage getImage() {
		return image;
	}
	
	// ��������� ��������
	public void setImage(BufferedImage image) {
		this.image = image;
		repaint();
	}
	
	// ��������� ����� (������������ ��������)
	public void setColor(Color color) {
		this.setBackground(color);
	}
	
}
