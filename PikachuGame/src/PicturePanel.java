import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class PicturePanel extends javax.swing.JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3116205224981407873L;
	private BufferedImage image = null;
	public final Dimension panelSize = new Dimension(40, 50);
	private Dimension position = new Dimension(0, 0);
	private int imageIndex = -1;
	// -- ��� ������ �����
	public PicturePanel parent;
	public int weight;
	public int g;
	public double h;
	public double f;
	public int straightLines;
	
	public PicturePanel() {
		initComponents();
	}
	
	// �������������
	private void initComponents() {
		position = new Dimension(0, 0);
		weight = 0;
		g = 0;
		h = 0;
		f = 0;
		straightLines = 0;
		setPreferredSize(panelSize);
		setSize(panelSize);
		setLayout(null);
	}
	
	// ����������
	public void paint(Graphics g) {
		if (image != null) {
			g.drawImage(image, 0, 0, null);
		} else {
			this.imageIndex = -1;
			this.setOpaque(true);
			g.setColor(getBackground());
			g.fillRect(0, 0, panelSize.width, panelSize.height);
		}
		super.paintChildren(g);
		super.paintBorder(g);
	}
	
	// ������� ��������
	public BufferedImage getImage() {
		return image;
	}
	
	// ��������� ��������
	public void setImage(BufferedImage image, int index) {
		this.image = image;
		this.imageIndex = index;
		repaint();
	}
	
	// ������� ��������
	public void clearImage() {
		this.image = null;
		repaint();
	}
	
	// ��������� ����� (������������ ��������)
	public void setColor(Color color) {
		this.setBackground(color);
	}
	
	// ��������� ���������
	public void setPosition(Dimension position) {
		this.position.height = position.height;
		this.position.width = position.width;
	}
	
	// ������� ���������
	public Dimension getPosition() {
		return position;
	}
	
	// ������� ������� ��������
	public int getImageIndex() {
		return imageIndex;
	}
	
}
