/*
 Implemented by : Abanoub Milad Nassif
 e-mail: abanoubcs@gmail.com 
 data  : 11/5/2015
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Panel extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel seg_start_label, seg_end_label, seg_gain_label,
			clear1_label, clear2_label;
	private JTextField seg_start_text, seg_end_text, seg_gain_text,
			clear1_text, clear2_text;
	private JButton solve_button, next_button, clear_button;
	private JPanel drawPanel;
	private int width, height;

	Panel() {
		initialize();
	}

	private void initialize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int) screenSize.getWidth() - 120;
		height = (int) screenSize.getHeight() - 120;
		Data.width = width;
		Data.height = height;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, width, height);
		setTitle("Signal FLow Graph Solver");
		setLayout(null);
		setResizable(true);

		seg_start_label = new JLabel("from node #");
		seg_start_label.setBounds(60, height - 120, 160, 50);
		seg_start_text = new JTextField();
		seg_start_text.setBounds(60, height - 60, 160, 50);

		seg_end_label = new JLabel("to node #");
		seg_end_label.setBounds(240, height - 120, 160, 50);
		seg_end_text = new JTextField();
		seg_end_text.setBounds(220, height - 60, 160, 50);

		seg_gain_label = new JLabel("gain");
		seg_gain_label.setBounds(420, height - 120, 160, 50);
		seg_gain_text = new JTextField();
		seg_gain_text.setBounds(380, height - 60, 160, 50);

		drawPanel = new Draw();
		drawPanel.setBounds(0, 0, width, height - 120);
		drawPanel.setBackground(Color.WHITE);

		next_button = new JButton("next");
		next_button.setBounds(560, height - 120, 100, 50);
		solve_button = new JButton("solve");
		solve_button.setBounds(560, height - 60, 100, 50);

		clear1_label = new JLabel("from node #");
		clear1_label.setBounds(720, height - 120, 160, 50);
		clear1_text = new JTextField();
		clear1_text.setBounds(720, height - 60, 160, 50);

		clear2_label = new JLabel("to node #");
		clear2_label.setBounds(900, height - 120, 160, 50);
		clear2_text = new JTextField();
		clear2_text.setBounds(880, height - 60, 160, 50);

		clear_button = new JButton("clear");
		clear_button.setBounds(1080, height - 80, 100, 50);

		solve_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Mason mason = new Mason();
				mason.setSFG(Data.segmentsGains);
				Data.forwardPaths = mason.getForwardPaths();
				Data.loops = mason.getLoops();
				Data.nonTouchingloops = mason.getNonTouchingLoops();
				Data.overAllTF = mason.getOvalAllTF();
				Data.loopsGain = mason.getLoopGains();
				Data.forwardPathsGain = mason.getForwardPathGains();
				Data.nonTouchingloopsGain = mason.getNonTouchingLoopGains();
				ResultView result = new ResultView();
				result.setVisible(true);
			}
		});
		next_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!Valid.isValidInt(seg_start_text.getText())) {
					ErrorView errView = new ErrorView(
							"from node, invalid numeric value!");
					errView.setVisible(true);
				} else if (!Valid.isValidInt(seg_end_text.getText())) {
					ErrorView errView = new ErrorView(
							"to node, invalid numeric value!");
					errView.setVisible(true);
				} else if (!Valid.isValidDouble(seg_gain_text.getText())) {
					ErrorView errView = new ErrorView(
							"segment gain, invalid numeric value!");
					errView.setVisible(true);
				} else {
					int n1 = Integer.parseInt(seg_start_text.getText()), n2 = Integer
							.parseInt(seg_end_text.getText());
					if (n1 > Data.numOfNodes || n2 > Data.numOfNodes) {
						ErrorView errView = new ErrorView(
								"node number exceeded max number of nodes!");
						errView.setVisible(true);
					} else if (n1 < 1 || n2 < 1) {
						ErrorView errView = new ErrorView(
								"invalid node number!");
						errView.setVisible(true);
					} else if (n1 == Data.numOfNodes) {
						ErrorView errView = new ErrorView(
								"no feedback allowded from node # "
										+ Data.numOfNodes);
						errView.setVisible(true);
					} else if (n2 == 1) {
						ErrorView errView = new ErrorView(
								"no feedback allowded to node # 1");
						errView.setVisible(true);
					} else {
						double g = Double.parseDouble(seg_gain_text.getText());
						Data.segmentsGains[n1 - 1][n2 - 1] = g;
						drawPanel.repaint();
						seg_end_text.setText("");
						seg_start_text.setText("");
						seg_gain_text.setText("");
					}
				}
			}
		});

		clear_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!Valid.isValidInt(clear1_text.getText())) {
					ErrorView errView = new ErrorView(
							"from node, invalid numeric value!");
					errView.setVisible(true);
				} else if (!Valid.isValidInt(clear2_text.getText())) {
					ErrorView errView = new ErrorView(
							"to node, invalid numeric value!");
					errView.setVisible(true);
				} else {
					int n1 = Integer.parseInt(clear1_text.getText()), n2 = Integer
							.parseInt(clear2_text.getText());
					if (n1 > Data.numOfNodes || n2 > Data.numOfNodes) {
						ErrorView errView = new ErrorView(
								"node # exceeded max number of nodes!");
						errView.setVisible(true);
					} else if (n1 < 1 || n2 < 1) {
						ErrorView errView = new ErrorView(
								"invalid node number!");
						errView.setVisible(true);
					} else if (Data.segmentsGains[n1 - 1][n2 - 1] == 0) {
						ErrorView errView = new ErrorView(
								"segment doesnot exist!");
						errView.setVisible(true);
					} else {
						Data.segmentsGains[n1 - 1][n2 - 1] = 0;
						drawPanel.repaint();
						clear1_text.setText("");
						clear2_text.setText("");
					}
				}
			}
		});

		Font font = new Font("Serif", Font.PLAIN, 24);
		seg_start_label.setFont(font);
		seg_end_label.setFont(font);
		seg_gain_label.setFont(font);
		seg_start_text.setFont(font);
		seg_end_text.setFont(font);
		seg_gain_text.setFont(font);
		next_button.setFont(font);
		solve_button.setFont(font);

		clear1_label.setFont(font);
		clear1_text.setFont(font);
		clear2_label.setFont(font);
		clear2_text.setFont(font);
		clear_button.setFont(font);

		getContentPane().add(seg_start_label);
		getContentPane().add(seg_end_label);
		getContentPane().add(seg_gain_label);
		getContentPane().add(seg_start_text);
		getContentPane().add(seg_end_text);
		getContentPane().add(seg_gain_text);
		getContentPane().add(drawPanel);
		getContentPane().add(solve_button);
		getContentPane().add(next_button);

		getContentPane().add(clear1_label);
		getContentPane().add(clear1_text);
		getContentPane().add(clear2_label);
		getContentPane().add(clear2_text);
		getContentPane().add(clear_button);
	}
}