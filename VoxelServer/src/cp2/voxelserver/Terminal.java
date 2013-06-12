package cp2.voxelserver;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Terminal extends JFrame implements KeyListener {

	private JTextArea text;
	private JScrollPane textPane;
	private JTextField input;
	private JPanel center;
	private JPanel centerBottom;
	private JPanel centerBottomPadding;
	private JPanel paddingN;
	private JPanel paddingS;
	private JPanel paddingE;
	private JPanel paddingW;

	public Terminal(String title) {
		super(title);

		text = new JTextArea();
		text.setEditable(false);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);

		textPane = new JScrollPane(text);

		input = new JTextField();
		input.addKeyListener(this);

		paddingN = new JPanel();
		paddingS = new JPanel();
		paddingE = new JPanel();
		paddingW = new JPanel();

		center = new JPanel();
		centerBottom = new JPanel();
		centerBottomPadding = new JPanel();

		center.setLayout(new BorderLayout());

		center.add(textPane, BorderLayout.CENTER);

		centerBottom.setLayout(new BorderLayout());
		centerBottom.add(centerBottomPadding, BorderLayout.NORTH);
		centerBottom.add(input, BorderLayout.CENTER);

		center.add(centerBottom, BorderLayout.SOUTH);

		add(center, BorderLayout.CENTER);
		add(paddingN, BorderLayout.NORTH);
		add(paddingS, BorderLayout.SOUTH);
		add(paddingE, BorderLayout.EAST);
		add(paddingW, BorderLayout.WEST);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(450, 450);
		int xLoc = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2;
		int yLoc = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2;
		setLocation(xLoc, yLoc);
		setVisible(true);

		input.requestFocus();
	}

	public void println(String input) {
		print(input + "\n");
	}
	public void print(String input) {
		text.append(input);
	}

	private String line = "";
	private volatile boolean hasLine = false;

	public String readLine() {
		while (!hasLine);
		hasLine = false;
		return line;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			line = input.getText();
			hasLine = true;
			input.setText("");
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}

}
