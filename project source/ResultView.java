/*
 Implemented by : Abanoub Milad Nassif
 e-mail: abanoubcs@gmail.com 
 data  : 11/5/2015
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;

public class ResultView extends JFrame {

	private static final long serialVersionUID = 1L;

	ResultView() {
		initialize();
	}

	private void initialize() {
		JLabel loopsLabel, FPLabel, tfLabel, nontouchLabel, loopsGainLabel, FPGainLabel, nonTouchGainLabel,tfLabel_body;

		JTextPane loopsLabel_body, FPLabel_body, nontouchLabel_body, loopsGainLabel_body, FPGainLabel_body, nonTouchGainLabel_body;

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth() - 120;
		int height = (int) screenSize.getHeight() - 100;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 50, width, height);
		setTitle("Signal FLow Graph Solver - Results");
		setLayout(null);
		setResizable(true);

		FPLabel = new JLabel("forwardPaths");
		FPGainLabel = new JLabel("gains");
		FPGainLabel_body = new JTextPane();
		FPLabel_body = new JTextPane();
		FPGainLabel_body.setBackground(Color.pink);

		loopsLabel = new JLabel("loops");
		loopsGainLabel = new JLabel("gains");
		loopsGainLabel_body = new JTextPane();
		loopsLabel_body = new JTextPane();
		loopsGainLabel_body.setBackground(Color.pink);
		
		nontouchLabel = new JLabel("nonTouching loops");
		nonTouchGainLabel = new JLabel("gains");
		nonTouchGainLabel_body = new JTextPane();
		nontouchLabel_body = new JTextPane();
		nonTouchGainLabel_body.setBackground(Color.pink);

		tfLabel = new JLabel("overAll T.F. = ");
		tfLabel_body = new JLabel();

		FPLabel.setBounds(0, 0, 250, 40);
		FPLabel_body.setBounds(0, 40, 250, height - 120);
		FPGainLabel.setBounds(250, 0, 150, 40);
		FPGainLabel_body.setBounds(250, 40, 150, height - 120);

		loopsLabel.setBounds(400, 0, 250, 40);
		loopsLabel_body.setBounds(400, 40, 250, height - 120);
		loopsGainLabel.setBounds(650, 0, 150, 40);
		loopsGainLabel_body.setBounds(650, 40, 150, height - 120);

		nontouchLabel.setBounds(800, 0, 250, 40);
		nontouchLabel_body.setBounds(800, 40, 250, height - 120);
		nonTouchGainLabel.setBounds(1050, 0, 150, 40);
		nonTouchGainLabel_body.setBounds(1050, 40, 150, height - 120);

		tfLabel.setBounds(20, height - 70, 250, 40);
		tfLabel_body.setBounds(270, height - 70, 400, 40);
		Font font = new Font("Serif", Font.PLAIN, 32);
		tfLabel.setFont(font);
		tfLabel_body.setFont(font);

		font = new Font("Serif", Font.BOLD, 16);

		loopsLabel.setFont(font);
		FPLabel.setFont(font);
		nontouchLabel.setFont(font);
		loopsGainLabel.setFont(font);
		FPGainLabel.setFont(font);
		nonTouchGainLabel.setFont(font);

		getContentPane().add(loopsLabel);
		getContentPane().add(FPLabel);
		getContentPane().add(tfLabel);
		getContentPane().add(nontouchLabel);
		getContentPane().add(loopsGainLabel);
		getContentPane().add(FPGainLabel);
		getContentPane().add(nonTouchGainLabel);

		getContentPane().add(loopsLabel_body);
		getContentPane().add(FPLabel_body);
		getContentPane().add(tfLabel_body);
		getContentPane().add(nontouchLabel_body);
		getContentPane().add(loopsGainLabel_body);
		getContentPane().add(FPGainLabel_body);
		getContentPane().add(nonTouchGainLabel_body);

		// populating bodies
		StringBuilder sb = new StringBuilder();
		sb.append("<font size=\"5\">");
		String[] tempArr = Data.forwardPaths;
		for (int i = 0; i < tempArr.length; i++) 
			sb.append((i + 1) + ") " + tempArr[i] + "<br>");
		sb.append("</font>");

		FPLabel_body.setContentType("text/html");
		FPLabel_body.setEditable(false);
		FPLabel_body.setText(sb.toString());
		FPLabel_body.setForeground(Color.blue);
		
		sb = new StringBuilder();
		sb.append("<font size=\"5\">");
		tempArr = Data.loops;
		for (int i = 0; i < tempArr.length; i++) 
			sb.append((i + 1) + ") " + tempArr[i] + "<br>");
		sb.append("</font>");
		loopsLabel_body.setContentType("text/html");
		loopsLabel_body.setEditable(false);
		loopsLabel_body.setText(sb.toString());

		sb = new StringBuilder();
		sb.append("<font size=\"5\">");
		tempArr = Data.nonTouchingloops;
		for (int i = 0; i < tempArr.length; i++) 
			sb.append((i + 1) + ") " + tempArr[i] + "<br>");
		sb.append("</font>");
		
		nontouchLabel_body.setContentType("text/html");
		nontouchLabel_body.setEditable(false);
		nontouchLabel_body.setText(sb.toString());
		
		sb = new StringBuilder();
		sb.append("<font size=\"5\">");
		Double [] tempDou = Data.forwardPathsGain;
		for (int i = 0; i < tempDou.length; i++) 
			sb.append((i + 1) + ") " + tempDou[i].doubleValue() + "<br>");
		sb.append("</font>");

		FPGainLabel_body.setContentType("text/html");
		FPGainLabel_body.setEditable(false);
		FPGainLabel_body.setText(sb.toString());
		
		sb = new StringBuilder();
		sb.append("<font size=\"5\">");
		tempDou = Data.loopsGain;
		for (int i = 0; i < tempDou.length; i++) 
			sb.append((i + 1) + ") " + tempDou[i].doubleValue() + "<br>");
		sb.append("</font>");

		loopsGainLabel_body.setContentType("text/html");
		loopsGainLabel_body.setEditable(false);
		loopsGainLabel_body.setText(sb.toString());
		
		sb = new StringBuilder();
		sb.append("<font size=\"5\">");
		tempDou = Data.nonTouchingloopsGain;
		for (int i = 0; i < tempDou.length; i++) 
			sb.append((i + 1) + ") " + tempDou[i].doubleValue() + "<br>");
		sb.append("</font>");
	
		nonTouchGainLabel_body.setContentType("text/html");
		nonTouchGainLabel_body.setEditable(false);
		nonTouchGainLabel_body.setText(sb.toString());
		
		tfLabel_body.setText(Data.overAllTF + "");

	}
}