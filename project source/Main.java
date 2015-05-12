/*
 Implemented by : Abanoub Milad Nassif
 e-mail: abanoubcs@gmail.com 
 data  : 11/5/2015
 */
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel main_label;
	private JTextField input_field;
	private JButton enter;

	Main() {
		initialize();
	}

	private void initialize() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(400, 200, 600, 250);
		setTitle("Signal FLow Graph Solver");
		setLayout(null);
		setResizable(false);

		main_label = new JLabel("enter total number of nodes");
		main_label.setBounds(50, 20, 500, 40);
		input_field = new JTextField();
		input_field.setBounds(100, 80, 400, 50);
		enter = new JButton("enter");
		enter.setBounds(200, 180, 200, 50);

		enter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (Valid.isValidInt(input_field.getText())) {
					int n = Integer.parseInt(input_field.getText());
					Data.numOfNodes = n;
					Data.segmentsGains = new double[n][n];
					Panel view = new Panel();
					view.setVisible(true);
					dispose();
				} else {
					ErrorView errView = new ErrorView("invalid numeric value!");
					errView.setVisible(true);
				}
			}
		});

		Font font = new Font("Serif", Font.PLAIN, 32);
		main_label.setFont(font);
		input_field.setFont(font);
		enter.setFont(font);

		getContentPane().add(main_label);
		getContentPane().add(input_field);
		getContentPane().add(enter);

	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main view = new Main();
					view.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
}