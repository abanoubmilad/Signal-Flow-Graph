/*
 Implemented by : Abanoub Milad Nassif
 e-mail: abanoubcs@gmail.com 
 data  : 11/5/2015
 */

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class ErrorView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel label;

	ErrorView(String str) {
		initialize(str);
	}

	private void initialize(String str) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(400, 200, 600, 200);
		setTitle("Oooooooops, Error Message!");
		setLayout(null);
		setResizable(false);

		label = new JLabel(str);
		label.setBounds(50, 0, 500, 200);
        Font font = new Font("Serif", Font.PLAIN, 24);
        label.setFont(font);
		getContentPane().add(label);

	}
}