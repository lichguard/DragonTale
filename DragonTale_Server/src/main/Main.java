package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import java.awt.GridBagLayout;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import game.Master;

import java.awt.GridBagConstraints;
import javax.swing.JTextArea;
import java.awt.Insets;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Main {

	private JFrame frame;
	private final JButton stopButton = new JButton("Stop");
	private final JButton startButton = new JButton("Start");
	private final JTextField textField = new JTextField();
	private Master gm;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setResizable(true);
		frame.setTitle("Dragon Tale Server v0.1");
		frame.setBounds(100, 100, 700, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JTextArea logField = new JTextArea();
		DefaultCaret caret = (DefaultCaret)logField.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		logField.setAutoscrolls(true);
		logField.setEditable(false);
		logField.setEnabled(false);
		logField.setLineWrap(true);
		logField.setForeground(Color.WHITE);
		logField.setBackground(Color.BLACK);
		
		JScrollPane scrollPane = new JScrollPane(logField,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		GridBagConstraints gbc_logField = new GridBagConstraints();
		gbc_logField.gridwidth = 3;
		gbc_logField.gridheight = 8;
		gbc_logField.insets = new Insets(0, 0, 5, 0);
		gbc_logField.fill = GridBagConstraints.BOTH;
		gbc_logField.gridx = 0;
		gbc_logField.gridy = 0;
		frame.getContentPane().add(scrollPane, gbc_logField);
		GridBagConstraints gbc_startButton = new GridBagConstraints();
		gbc_startButton.insets = new Insets(0, 0, 0, 5);
		gbc_startButton.gridx = 0;
		gbc_startButton.gridy = 8;
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				executeCommand("server start");
			}
		});
		frame.getContentPane().add(startButton, gbc_startButton);
		GridBagConstraints gbc_stopButton = new GridBagConstraints();
		gbc_stopButton.insets = new Insets(0, 0, 0, 5);
		gbc_stopButton.gridx = 1;
		gbc_stopButton.gridy = 8;
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executeCommand("server stop");
			}
		});
		frame.getContentPane().add(stopButton, gbc_stopButton);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 8;
		textField.setForeground(Color.WHITE);
		textField.setBackground(Color.BLACK);
		frame.getContentPane().add(textField, gbc_textField);
		textField.setColumns(10);
		
		
		
		LOGGER.setLog(logField);

		
		//scrollPane.add(logField);
		//scrollPane.setViewportView(logField);
	}
	
	private void executeCommand(String command) {
		if (command.equals("server start")) {
			if (gm == null) {
				gm = new Master();
				(new Thread(gm)).start();
			} else {
				LOGGER.warn("Server is already running!", this);
			}
			
		} else if (command.equals("server stop")) {
			if (gm != null) {
				gm.running = false;
				gm = null;
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				LOGGER.warn("Server is not running!", this);
			}
		}
	}

}
