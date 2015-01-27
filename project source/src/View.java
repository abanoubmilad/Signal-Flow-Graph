import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

public class View extends JPanel {
	private static JFrame f;
	private JButton next;
	private JButton finish;
	private JButton clear;
	private JButton save;
	private JButton load;
	private JTextArea node1;
	private JTextArea node2;
	private JTextArea gain;
	private JPanel p = this;
	private ArrayList<ArrayList<String>> nodes = new ArrayList<ArrayList<String>>();
	private ArrayList<ArrayList<String>> painted;
	private ImageIcon direction;
	private double[] nodesGain;
	private int[][] nods;

	public View() {
		setBackground(Color.WHITE);
		setLayout(null);
		initializePanel();
	}

	private void getNodes() {
		nods = new int[nodes.size()][2];
		nodesGain = new double[nodes.size()];
		for (int i = 0; i < nodes.size(); i++) {
			int node1 = getNodeNumber(nodes.get(i).get(0));
			int node2 = getNodeNumber(nodes.get(i).get(2));
			int[] entry = new int[2];
			entry[0] = node1;
			entry[1] = node2;
			nods[i] = entry;
			nodesGain[i] = Double.parseDouble(nodes.get(i).get(1));
		}
	}

	private int getNodeNumber(String node) {
		for (int i = 0; i < painted.size(); i++) {
			if (painted.get(i).get(0).equals(node)) {
				return i + 1;
			}
		}
		return 0;
	}

	private void initializePanel() {
		next = new JButton("Next");
		next.setBounds(880, 600, 100, 25);
		next.setFont(new Font("Verdana", Font.ITALIC, 18));
		add(next);
		nextAction();

		finish = new JButton("Finish");
		finish.setBounds(880 + 140, 600, 100, 25);
		finish.setFont(new Font("Verdana", Font.ITALIC, 18));
		add(finish);
		finishAction();

		clear = new JButton("Clear");
		clear.setBounds(880 + 2 * 140, 600, 100, 25);
		clear.setFont(new Font("Verdana", Font.ITALIC, 18));
		add(clear);
		clearAction();

		save = new JButton("Save");
		save.setBounds(880, 600 + 50, 100, 25);
		save.setFont(new Font("Verdana", Font.ITALIC, 18));
		add(save);
		saveAction();

		load = new JButton("Load");
		load.setBounds(880 + 1 * 140, 600 + 50, 100, 25);
		load.setFont(new Font("Verdana", Font.ITALIC, 18));
		add(load);
		loadAction();
		node1 = new JTextArea();
		node1.setBounds(480, 600, 100, 20);
		node1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		add(node1);

		node2 = new JTextArea();
		node2.setBounds(620, 600, 100, 20);
		node2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		add(node2);

		gain = new JTextArea();
		gain.setBounds(760, 600, 100, 20);
		gain.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		add(gain);

		JLabel n1 = new JLabel("Node 1");
		n1.setBounds(480, 570, 100, 20);
		n1.setFont(new Font("Verdana", Font.ITALIC, 18));
		add(n1);

		JLabel n2 = new JLabel("Node 2");
		n2.setBounds(620, 570, 100, 20);
		n2.setFont(new Font("Verdana", Font.ITALIC, 18));
		add(n2);

		JLabel g = new JLabel("Gain");
		g.setBounds(760, 570, 100, 20);
		g.setFont(new Font("Verdana", Font.ITALIC, 18));
		add(g);

		Rectangle2D r = new Rectangle();
	}

	private void nextAction() {
		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (node1.getText().equals("")
							|| node2.getText().equals("")
							|| gain.getText().equals("")) {
						throw new NullPointerException();
					}
					Integer.parseInt(node1.getText());
					Integer.parseInt(node2.getText());
					Double.parseDouble(gain.getText());
					if (isFound_Entry(node1.getText(), node2.getText(),
							gain.getText())) {
						throw new NullPointerException();
					}
					ArrayList<String> entry = new ArrayList<>();
					entry.add(node1.getText());
					entry.add(gain.getText());
					entry.add(node2.getText());
					nodes.add(entry);
					repaint();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error");
				}
			}
		});
	}

	private void finishAction() {
		finish.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				p.setVisible(false);
				JPanel pa = new JPanel();
				pa.setLayout(null);
				f.add(pa);
				JButton back = new JButton("Back");
				back.setBounds(880, 600, 100, 25);
				back.setFont(new Font("Verdana", Font.ITALIC, 18));
				pa.add(back);
				backAction(pa, back);
				getNodes();
				Operate op = new Operate();
				op.setMaxNodes(painted.size());
				op.setNodes(nods);
				op.setNodesGain(nodesGain);
				op.generate();
				JLabel FP = new JLabel("Listing all forward paths: \n"
						+ op.getForwardPaths());
				FP.setBounds(180, 0, 1000, 250);
				FP.setFont(new Font("Verdana", Font.ITALIC, 18));
				pa.add(FP);

				JLabel loops = new JLabel("All loops: \n" + op.getLoops());
				loops.setBounds(180, 100, 1000, 250);
				loops.setFont(new Font("Verdana", Font.ITALIC, 18));
				pa.add(loops);

				JLabel nonTouching = new JLabel("Non toching: \n"
						+ op.getnonTouching());
				nonTouching.setBounds(180, 200, 1000, 250);
				nonTouching.setFont(new Font("Verdana", Font.ITALIC, 18));
				pa.add(nonTouching);

				JLabel TF = new JLabel("TF: \n" + op.getTF());
				TF.setBounds(180, 400, 400, 250);
				TF.setFont(new Font("Verdana", Font.ITALIC, 18));
				pa.add(TF);
			}
		});
	}

	private void clearAction() {
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				nodes = new ArrayList<ArrayList<String>>();
				painted = new ArrayList<ArrayList<String>>();
				p.removeAll();
				initializePanel();
				repaint();
				revalidate();
			}
		});
	}

	private void saveAction() {
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser f = new JFileChooser();
				f.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				f.showSaveDialog(null);
				if (f.getSelectedFile() != null) {
					File file = new File(f.getSelectedFile().getPath() + ".sig");
					saveData(file);
				}
			}
		});
	}

	private void loadAction() {
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser f = new JFileChooser();
				f.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int x = f.showOpenDialog(null);
				FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter(
						"sig files (*.sig)", "sig");
				File file = f.getSelectedFile();
				if (file != null) {
					String s = f.getSelectedFile().getPath();
					if (s.charAt(s.length() - 1) == 'g'
							&& s.charAt(s.length() - 2) == 'i'
							&& s.charAt(s.length() - 3) == 's'
							&& s.charAt(s.length() - 4) == '.') {
					} else {
						JOptionPane.showMessageDialog(null,
								"Invalid choice,\nPlease select .sig files");
					}
				}
				loadData(file);
				repaint();
			}
		});
	}

	private void saveData(File file) {
		try {
			FileOutputStream fout = new FileOutputStream(file.getPath());
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			Data data = new Data();
			data.setNodes(nodes);
			data.setPainted(painted);
			oos.writeObject(data);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadData(File file) {
		try {
			FileInputStream fin = new FileInputStream(file.getPath());
			ObjectInputStream ois = new ObjectInputStream(fin);
			Data data = new Data();
			data = (Data) ois.readObject();
			nodes = data.getNodes();
			painted = data.getPainted();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void backAction(final JPanel pa, JButton back) {
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pa.setVisible(false);
				p.setVisible(true);
			}
		});
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		g.setColor(Color.white);
		g.fillRect(0, 0, 1400, 550);
		g.setColor(Color.black);
		g.drawRect(0, 0, 1400, 550);
		painted = new ArrayList<ArrayList<String>>();

		int count = 0;
		int start = 200;
		int counter = 0;
		for (int i = 0; i < nodes.size(); i++) {
			
			// paint and save first node
			if (!isFound_Node(painted, nodes.get(i).get(0))) {
				g.drawOval(start + (count * 100), 400, 30, 30);
				paintNodeName(start + (count * 100), nodes.get(i).get(0));
				ArrayList<String> entry = new ArrayList<>();
				entry.add(nodes.get(i).get(0));
				entry.add(Integer.toString(start + (count * 100)));
				entry.add(Integer.toString(400));
				count++;
				painted.add(entry);
			}
			// paint and save second node
			if (!isFound_Node(painted, nodes.get(i).get(2))) {
				ArrayList<String> entry2 = new ArrayList<>();
				entry2.add(nodes.get(i).get(2));
				g.drawOval(start + (count * 100), 400, 30, 30);
				paintNodeName(start + (count * 100), nodes.get(i).get(2));
				entry2.add(Integer.toString(start + (count * 100)));
				entry2.add(Integer.toString(400));
				count++;
				painted.add(entry2);
			}
			paintDirection(i, g, counter);
			counter++;
		}
	}
	private void paintDirection(int i, Graphics g, int counter) {
		int node1 = getDimOfNode(nodes.get(i).get(0));
		int node2 = getDimOfNode(nodes.get(i).get(2));
		int diff = node2 - node1;
		if (diff == 100) {
			g.drawLine(node1 + 30, 415, node1 + 100, 415);
			direction = new ImageIcon("sahm1.gif");
			paintGain(node1 + 30, 415, 70, nodes.get(i).get(1), g);
		} else {
			if (diff == 0) {
				direction = new ImageIcon("sahm3.gif");
				g.drawArc(node1 - 40, 400 - 25, 50, 50, 0, 305);
				paintGain(node1 - 40, 400 - 25, 0, nodes.get(i).get(1), g);
			} else {
				if (diff < 0) {
					diff *= -1;
					node1 = node2;
					direction = new ImageIcon("sahm2.gif");
				} else {
					direction = new ImageIcon("sahm1.gif");
				}
				int c = (diff - 200) / 100;
				
				if (counter % 2 == 0) {
					g.drawArc(node1 + 15, 300 - (c * 50), diff, diff, 0,
							180);
					paintGain(node1 + 15, 300 - (c * 50), diff, nodes
							.get(i).get(1), g);
				} else {
					g.drawArc(node1 + 15, 300 - (c * 50) + 30, diff, diff,
							180, 180);
					paintGain(node1 + 15, 300 - (c * 50) + 30 + diff, diff,
							nodes.get(i).get(1), g);
				}
			}
		}
	}
	private void paintNodeName(int x, String node) {
		JLabel n1 = new JLabel(node);
		n1.setBounds(x + 10, 370, 100, 20);
		n1.setFont(new Font("Verdana", Font.ITALIC, 18));
		p.add(n1);
	}

	private void paintGain(int x, int y, int diff, String gain, Graphics g) {
		JLabel ga = new JLabel(gain);
		ga.setBounds(x + diff / 2, y - 30, 100, 20);
		if (diff == 0) {
			x += 10;
		}
		g.drawImage(direction.getImage(), x + diff / 2, y - 10, 20, 20, null);
		ga.setFont(new Font("Verdana", Font.ITALIC, 18));
		p.add(ga);
	}

	private int getDimOfNode(String node) {
		for (int i = 0; i < painted.size(); i++) {
			if (painted.get(i).get(0).equals(node)) {
				return Integer.parseInt(painted.get(i).get(1));
			}
		}
		return 0;
	}

	private boolean isFound_Node(ArrayList<ArrayList<String>> painted,
			String node) {
		for (int k = 0; k < painted.size(); k++) {
			if (painted.get(k).get(0).equals(node)) {
				return true;
			}
		}
		return false;
	}

	private boolean isFound_Entry(String node1, String node2, String gain) {
		for (int i = 0; i < nodes.size(); i++) {
			ArrayList<String> entry = nodes.get(i);
			if (entry.get(0).equals(node1) && entry.get(1).equals(gain)
					&& entry.get(2).equals(node2)) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		f = new JFrame("Signal Flow Graph");
		f.add(new View());
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
