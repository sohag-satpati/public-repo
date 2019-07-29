package com.sohag.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileSystemView;
import javax.swing.border.LineBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Gui {

	private JFrame mainFrame;
	private JTextField textField;
	private JTextField textField_1;
	private JTable table_1;
	private JTextPane textPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
					window.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Gui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		mainFrame = new JFrame();
		mainFrame.setTitle("TC Uploader");
		mainFrame.setResizable(false);
		mainFrame.setBounds(100, 100, 879, 730);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);
		
		//Status console
		textPane = new JTextPane();
		textPane.setFont(new Font("Arial", Font.PLAIN, 10));
		textPane.setBackground(SystemColor.menu);
		textPane.setEditable(false);
		textPane.setBounds(15, 572, 842, 86);
		mainFrame.getContentPane().add(textPane);
		
		//Mapping
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(15, 0, 842, 571);
		mainFrame.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		tabbedPane.addTab("Header Mapping", null, panel, null);
		panel.setLayout(null);
		
		
		JLabel lblExcelMappingFile = new JLabel("Excel Mapping File:");
		lblExcelMappingFile.setBounds(15, 16, 145, 20);
		panel.add(lblExcelMappingFile);
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@SuppressWarnings("static-access")
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == e.VK_ENTER) {
					ReadPropertyAction();
				}
			}
		});
		textField.setBounds(153, 13, 492, 26);
		panel.add(textField);
		textField.setColumns(10);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText(Browse());
				ReadPropertyAction();
			}
		});
		btnBrowse.setBounds(696, 12, 115, 29);
		panel.add(btnBrowse);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileName = textField.getText();
				PropertyTableModel myModel = (PropertyTableModel) table_1.getModel();
				Object[][] rowData = myModel.getTableData();
				try {
					writePropertiesFile(fileName, rowData);
					textPane.setText("Property updates saved \n");
				} catch(FileNotFoundException fnfe) {
					throwErrorMessage("Given file was not found or file Name field is empty");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnSave.setBounds(530, 493, 115, 29);
		panel.add(btnSave);		
		
		//properties table
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(15, 52, 796, 425);
		panel_1.setLayout(new BorderLayout());
		panel.add(panel_1);
		
		//PropertyTable here
		setupPropertyTable();
		panel_1.add(table_1.getTableHeader(),BorderLayout.PAGE_START);		
		panel_1.add(table_1,BorderLayout.CENTER);
		
		JButton btnGetMetaData = new JButton("Get META Data");
		btnGetMetaData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showInputDialog(mainFrame, "Project Key:", "E.g. LUIGI");
			}
		});
		btnGetMetaData.setBounds(375, 493, 145, 29);
		panel.add(btnGetMetaData);
		
		JButton btnPrepareTcField = new JButton("Prepare TC Field Types");
		btnPrepareTcField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//run code to setup the TC Fields
			}
		});
		btnPrepareTcField.setBounds(153, 493, 214, 29);
		panel.add(btnPrepareTcField);
		panel.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{lblExcelMappingFile, textField, btnBrowse, btnSave}));
		
		
		//Upload/Amend
		JPanel panel2 = new JPanel();
		tabbedPane.addTab("Upload/Amend", null, panel2, null);
		panel2.setLayout(null);
		
		JLabel lblAbsolutePathTo = new JLabel("Absolute path to Test case file:");
		lblAbsolutePathTo.setBounds(15, 27, 266, 20);
		panel2.add(lblAbsolutePathTo);
		
		textField_1 = new JTextField();
		textField_1.setBounds(25, 55, 647, 26);
		panel2.add(textField_1);
		textField_1.setColumns(10);
		
		JRadioButton rdbtnUploadNewCases = new JRadioButton("Upload New Cases");
		rdbtnUploadNewCases.setBounds(123, 105, 163, 29);
		panel2.add(rdbtnUploadNewCases);
		
		JRadioButton rdbtnAmendExistingCases = new JRadioButton("Amend Existing cases");
		rdbtnAmendExistingCases.setBounds(378, 105, 185, 29);
		panel2.add(rdbtnAmendExistingCases);
		
		rdbtnUploadNewCases.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println(rdbtnUploadNewCases.isSelected());
				if(rdbtnUploadNewCases.isSelected()) {
					rdbtnAmendExistingCases.setSelected(false);
				}
			}
		}); 
		
		rdbtnAmendExistingCases.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println(rdbtnAmendExistingCases.isSelected());
				if(rdbtnAmendExistingCases.isSelected()) {
					rdbtnUploadNewCases.setSelected(false);
				}
			}
		}); 
		
		
		
		
		JButton btnExecute = new JButton("Execute");
		btnExecute.setBounds(327, 205, 115, 29);
		panel2.add(btnExecute);
		
		JLabel lblChooseAction = new JLabel("Choose Action:");
		lblChooseAction.setBounds(15, 114, 238, 20);
		panel2.add(lblChooseAction);
		
		JButton btnBrowse_1 = new JButton("Browse");
		btnBrowse_1.setBounds(707, 54, 115, 29);
		panel2.add(btnBrowse_1);
		
		
	}
	
	public void setupPropertyTable() {
		Object [][] rowData = {{"","",null}};
		PropertyTableModel myModel = new PropertyTableModel();
		myModel.setRowsData(rowData);
		table_1 = new JTable(myModel);	
		
		table_1.setRowHeight(30);
		table_1.getColumnModel().getColumn(0).setPreferredWidth(125);
		table_1.getColumnModel().getColumn(0).setMinWidth(125);
		table_1.getColumnModel().getColumn(0).setMaxWidth(250);
		table_1.getColumnModel().getColumn(1).setPreferredWidth(125);
		table_1.getColumnModel().getColumn(1).setMinWidth(125);
		table_1.getColumnModel().getColumn(1).setMaxWidth(250);
		table_1.getColumnModel().getColumn(2).setPreferredWidth(125);
		table_1.getColumnModel().getColumn(2).setMinWidth(125);
		table_1.getColumnModel().getColumn(2).setMaxWidth(125);
		table_1.setColumnSelectionAllowed(true);
		table_1.setCellSelectionEnabled(true);
		table_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		table_1.setFont(new Font("Arial", Font.PLAIN, 16));
	}
	
	public void updatePropertyTable(Object[][] data) {
		PropertyTableModel myModel = (PropertyTableModel) table_1.getModel();
		myModel.resetTable();
		myModel.setRowsData(data);
	}
	
	@SuppressWarnings({ "resource", "null" })
	public Object[][] readPropertiesFile(String fileName) {
		Object[][] data = null;
		List<Object[]> listOfObjects = new ArrayList<Object[]>();
		try {
			Scanner scan = new Scanner(new File(fileName));
			while(scan.hasNext()) {
				String line = scan.nextLine();
				boolean toBeUsed = true;
				if(line.startsWith("#")) {
					toBeUsed = false;
					line = line.replaceAll("#", "");
				}
				String key = line.split("=")[0];
				String val = line.split("=")[1];
				
				listOfObjects.add(new Object[] {key,val,toBeUsed});
			}
			scan.close();
			
			int rows = listOfObjects.size();
			data = new Object[rows][3];
			int indx = -1;
			for(indx=0; indx<rows; indx++){
				data[indx][0] = listOfObjects.get(indx)[0];
				data[indx][1] = listOfObjects.get(indx)[1];
				data[indx][2] = listOfObjects.get(indx)[2];
			};
			
			
		}catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(mainFrame, fileName + " NOT Found", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return data;
	}
	
	public void writePropertiesFile(String fileName, Object[][] data) throws Exception{
		PrintWriter pw = new PrintWriter(fileName);
		for(Object[] dat : data) {
			String key = (String) dat[0];
			String val = (String) dat[1];
			boolean toBeUsed = (boolean) dat[2];
			String line = key + "=" + val;
			if(!toBeUsed) {
				pw.write("#" + line);
			} else {
				pw.write(line);
			}
			pw.write("\n");
		}
		pw.close();		
	}
	
	private String Browse() {
		JFileChooser jc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jc.showOpenDialog(mainFrame);
		return jc.getSelectedFile().getAbsolutePath();
	}
	
	public void ReadPropertyAction() {
		String fileName = textField.getText();
		Object[][] rowData;
		try {
			rowData = readPropertiesFile(fileName);
			updatePropertyTable(rowData);
			String msg = textPane.getText()+"\n"+"Table is now updated.";
			textPane.setText(msg);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private void throwErrorMessage(String message) {
		JOptionPane.showMessageDialog(mainFrame, message, "Error!", JOptionPane.ERROR_MESSAGE);
	}
}
