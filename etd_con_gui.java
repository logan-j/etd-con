package etd_con;

import java.awt.EventQueue;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;

import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import etd_con.Convert.Guide;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.ScrollPaneConstants;

/**This is the main program file. Launches the GUI and integrates all of the class files.
 * The "TODO" tags are used to mark between the different panes/helper methods in Eclipse and do not 
 * indicate any unfinished work.
 * 
 * @author Logan Jewett with Iowa State University.
 *
 */
public class etd_con_gui {
	
	
	//Locations of the guides and their archives are declared here so that they can be accessed by helper methods.
	final File degGuide = new File(System.getProperty("user.dir") + "\\degguide");
	final File depGuide = new File(System.getProperty("user.dir") + "\\depguide");
	final File disGuide = new File(System.getProperty("user.dir") + "\\disguide");
	final File archDegGuide = new File(System.getProperty("user.dir") + "\\archive\\degguide");
	final File archDepGuide = new File(System.getProperty("user.dir") + "\\archive\\depguide");
	final File archDisGuide = new File(System.getProperty("user.dir") + "\\archive\\disguide");
	
	//Handles the output for the debug window
	final ByteArrayOutputStream myErr = new ByteArrayOutputStream();
	final JTextArea debug = new JTextArea(19, 43);
	
	private JFrame frmEtdProquestXml;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					etd_con_gui window = new etd_con_gui();
					window.frmEtdProquestXml.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public etd_con_gui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//for outputting to the debug window
			System.setErr(new PrintStream(myErr));
			KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
			manager.addKeyEventDispatcher(new Dispatcher());
			
		//frame initializations
			frmEtdProquestXml = new JFrame();
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			frmEtdProquestXml.setResizable(false);
			frmEtdProquestXml.setTitle("ETD ProQuest to Digital Repository Conversion Utility v1.0");
			frmEtdProquestXml.setBounds(100, 100, 500, 370);
			frmEtdProquestXml.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frmEtdProquestXml.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		//Text areas are declared early here so that they can be accessed by all panels.
			final JTextArea busTextArea = new JTextArea();
			final JTextArea conTextArea = new JTextArea();
			final JTextArea editTextArea = new JTextArea();
			final JTextArea hisTextArea = new JTextArea();
			
		//Edit Tab Load button Declared here for access by Convert Tab (disabled when content added)
			final JButton editLoadBtn = new JButton("Load");
			
		
		//Edit tab radial buttons are declared early in the event a mode needs to be changed in other panels.
			final JRadioButton rdbtnDelete = new JRadioButton("Delete");
			final JRadioButton rdbtnBatch = new JRadioButton("Batch Add");
			final JRadioButton rdbtnEdit = new JRadioButton("Edit");
			final JRadioButton rdbtnAdd = new JRadioButton("Add");
		
		//debug window constraints
			debug.setEditable(false);
			debug.setForeground(Color.RED);
		
	// TODO Batch Unzip & Sort Pane
		
		//component declarations
			//panel
				JPanel busPanel = new JPanel();
			
			//text area declared above
			
			//fields
				final JTextField busBrowse1 = new JTextField();
				final JTextField busBrowse2 = new JTextField();
				
			
			//buttons
				JButton busBrowseBtn1 = new JButton("Browse");
				JButton busBrowseBtn2 = new JButton("Browse");
				final JButton btnUnzip = new JButton("UNZIP");
				
			
			//labels
				JLabel busEtdFolderToLabel = new JLabel("ETD Folder To Be Unzipped");
				JLabel busDestFolderLabel = new JLabel("Destination Folder");
				JLabel busPBarLabel = new JLabel("Progress :");
				
			//progress bar
				final JProgressBar busPBar = new JProgressBar();
				
			
		//component constraints
			//panel
				tabbedPane.addTab("Batch Unzip & Sort", null, busPanel, null);
				busPanel.setLayout(null);
			
			//text area
				busTextArea.setEditable(false);
				JScrollPane scroll_2 = new JScrollPane(busTextArea);
				scroll_2.setBounds(10, 142, 469, 162);
				busTextArea.setForeground(Color.RED);
				scroll_2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				scroll_2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				busPanel.add(scroll_2);
				
			//fields
				busBrowse1.setEditable(false);
				busBrowse1.setBounds(10, 30, 363, 20);
				busPanel.add(busBrowse1);
				
				busBrowse2.setEditable(false);
				busBrowse2.setBounds(10, 76, 363, 20);
				busPanel.add(busBrowse2);
			
			//buttons
				busBrowseBtn1.setBounds(379, 28, 100, 24);
				busPanel.add(busBrowseBtn1);
				
				busBrowseBtn2.setBounds(379, 74, 100, 24);
				busPanel.add(busBrowseBtn2);
				
				btnUnzip.setEnabled(false);
				btnUnzip.setBounds(379, 113, 100, 23);
				busPanel.add(btnUnzip);
				
				
			//labels
				busEtdFolderToLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
				busEtdFolderToLabel.setBounds(10, 10, 180, 14);
				busPanel.add(busEtdFolderToLabel);
				
				busDestFolderLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
				busDestFolderLabel.setBounds(10, 56, 140, 14);
				busPanel.add(busDestFolderLabel);
				
				busPBarLabel.setBounds(10, 102, 80, 14);
				busPanel.add(busPBarLabel);
				
			//progress bar
				busPBar.setBounds(10, 122, 257, 14);
				busPanel.add(busPBar);
		
		//action listeners
			//adds functionality to the first browse button
			busBrowseBtn1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					File dir = new File("Y:/");
					JFileChooser browse;
					if(dir.isDirectory()){
						browse = new JFileChooser(dir);
					}else{
						browse = new JFileChooser();
					}
					browse.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int returnVal = browse.showDialog(busBrowse1, "Select ETD Directory");
					if (returnVal == JFileChooser.APPROVE_OPTION){
						busBrowse1.setText(browse.getSelectedFile().getAbsolutePath());
					}
					if(!(busBrowse2.getText().isEmpty() && busBrowse1.getText().isEmpty()))btnUnzip.setEnabled(true);
				}
			});
			
			//adds functionality to the second browse button
			busBrowseBtn2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					File dir = new File("Y:/");
					JFileChooser browse;
					if(dir.isDirectory()){
						browse = new JFileChooser(dir);
					}else{
						browse = new JFileChooser();
					}
					browse.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int returnVal = browse.showDialog(busBrowse1, "Select Destination Directory");
					if (returnVal == JFileChooser.APPROVE_OPTION){
						busBrowse2.setText(browse.getSelectedFile().getAbsolutePath());
					}
					if(!(busBrowse2.getText().isEmpty() && busBrowse1.getText().isEmpty()))btnUnzip.setEnabled(true);
				}
			});
			
			//adds functionality to the unzip button
			btnUnzip.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0){
					clear(busTextArea);
					Runnable unzipThread = new Runnable(){
						public void run(){
							File unzip = new File(busBrowse1.getText());
							File sort = new File(busBrowse2.getText());
							File PDF;
							File XML;
							File Large;
							File Supple;
							File destination;
							ArrayList<File> zipped = new ArrayList<File>();
							(PDF = new File(sort + "/PDF")).mkdir();
							(XML = new File(sort + "/XML")).mkdir();
							(Large = new File(sort + "/Large PDF")).mkdir();
							(Supple = new File(sort + "/Supplemental Files")).mkdir();
							(destination = new File(sort + "/temp")).mkdir();
							
							File[] input = unzip.listFiles();
							for(int i = 0; i < input.length; i++){
								if(input[i].getName().endsWith(".zip"))zipped.add(input[i]);
							}
							
							for(int i = 0; i < zipped.size(); i++){
								busPBar.setValue(((i + 1) * 100) / zipped.size());
								UNZip input1 = new UNZip(zipped.get(i));
								StrBool unzipped = input1.commenceUnzip(destination.toString() + "/");
								if(!unzipped.getStatus()){
									busTextArea.append("\n++ UNZIP OF " + unzipped.getFrom() + " WAS UNSUCCESSFUL ++\n");
								}else for(Files output : unzipped.getToArray()){
									busTextArea.append("Unzipping " + unzipped.getFrom() + ":\n");
									if(output.getType()){
										busTextArea.append("+ Extracting Directory : " + output + "\n");
									}else {
										busTextArea.append("+ Extracting File : " + output + "\n");
									}
								}
								File[] sort1 = destination.listFiles();
								for(int j = 0; j < sort1.length; j++){
									busTextArea.append("> Sorting File: " + sort1[j].getName());
									if(sort1[j].isDirectory()){
										try {
											copyDirectory(sort1[j], new File(Supple + "/" + sort1[j].getName()));
										} catch (IOException e) {
											busTextArea.append(" ERROR WHILE COPYING DIRECTORY " + sort1[j].getName());
											e.printStackTrace();
										}
										busTextArea.append(" -> Supplemental Files\n");
										try {
											delete(sort1[j]);
										} catch (IOException e) {
											busTextArea.append(" ERROR WHILE REMOVING TEMP DIRECTORY "
													+ sort1[j].getName() + "\n");
											e.printStackTrace();
										}
										continue;
									}
									if(sort1[j].isFile()){
										if(sort1[j].getName().endsWith(".xml")){
											try {
												copyFile(sort1[j], new File(XML + "/" + sort1[j].getName()));
											} catch (IOException e) {
												busTextArea.append(" ERROR WHILE COPYING XML FILE " + sort1[j].getName());
												e.printStackTrace();
											}
											busTextArea.append(" -> XML\n");
											try {
												delete(sort1[j]);
											} catch (IOException e) {
												busTextArea.append(" ERROR WHILE REMOVING TEMP XML FILE " 
														+ sort1[j].getName() + "\n");
												e.printStackTrace();
											}
											continue;
										}
										if(sort1[j].getName().endsWith(".pdf") && sort1[j].length() <= 10240000){
											try {
												copyFile(sort1[j], new File(PDF + "/" + sort1[j].getName()));
											} catch (IOException e) {
												busTextArea.append(" ERROR WHILE COPYING PDF FILE " + sort1[j].getName());
												e.printStackTrace();
											}
											busTextArea.append(" -> PDFs\n");
											try {
												delete(sort1[j]);
											} catch (IOException e) {
												busTextArea.append(" ERROR WHILE DELETING TEMP PDF FILE "
														+ sort1[j].getName() + "\n");
												e.printStackTrace();
											}
											continue;
										}
										if(sort1[j].getName().endsWith(".pdf") && sort1[j].length() > 10240000){
											try {
												copyFile(sort1[j], new File(Large + "/" + sort1[j].getName()));
											} catch (IOException e) {
												busTextArea.append(" ERROR WHILE COPYING LARGE PDF FILE " + sort1[j].getName());
												e.printStackTrace();
											}
											busTextArea.append(" -> Large PDFs\n");
											try {
												delete(sort1[j]);
											} catch (IOException e) {
												busTextArea.append("ERROR WHILE DELETING TEMP LARGE PDF FILE "
														+ sort1[j].getName() + "\n");
												e.printStackTrace();
											}
											continue;
										}else{
											try {
												copyFile(sort1[j], new File(Supple + "/" + sort1[j].getName()));
											} catch (IOException e) {
												busTextArea.append(" ERROR WHILE COPYING SUPPLEMENTAL FILE "
														+ sort1[j].getName());
												e.printStackTrace();
											}
											busTextArea.append(" -> Supplemental Files\n");
											try {
												delete(sort1[j]);
											} catch (IOException e) {
												busTextArea.append("ERROR WHILE DELETING TEMP SUPPLEMENTAL FILE"
														+ sort1[j].getName() + "\n");
												e.printStackTrace();
											}
											continue;
										}
									}
								}
							}
							try {
								delete(destination);
							} catch (IOException e) {
								busTextArea.append("ERROR WHILE DELETING TEMP WORKING DIRECTORY\n");
								e.printStackTrace();
							}
						}
					};
					
					Thread thr1 = new Thread(unzipThread);
					thr1.start();
					btnUnzip.setEnabled(false);
				}
		
			});
			
	// TODO Convert Pane
		//component declarations
			//panel
				JPanel conPanel = new JPanel();
				
			//text area declared above
			
			//fields
				final JTextField conBrowse = new JTextField();
				
			//buttons
				final JButton btnBrowse = new JButton("Browse");
				final JButton conConvertBtn = new JButton("Convert");
				final JButton conCombineBtn = new JButton("Combine");
				
			//labels
				JLabel conProDirLabel = new JLabel("ProQuest XML Directory");
				JLabel conPBarLabel = new JLabel("Progress :");
				
			//progress bar
				final JProgressBar conPBar = new JProgressBar();
			
		//component constraints
			//panel
				tabbedPane.addTab("Convert Proquest XMLs", null, conPanel, null);
				conPanel.setLayout(null);
				
			//text area
				conTextArea.setEditable(false);
				JScrollPane scroll = new JScrollPane(conTextArea);
				scroll.setBounds(10, 96, 469, 208);
				conTextArea.setForeground(Color.RED);
				scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				conPanel.add(scroll);
				
			//fields
				conBrowse.setEditable(false);
				conBrowse.setBounds(10, 30, 363, 20);
				conPanel.add(conBrowse);
				
			//buttons
				btnBrowse.setBounds(379, 28, 100, 24);
				conPanel.add(btnBrowse);
				
				conConvertBtn.setEnabled(false);
				conConvertBtn.setBounds(273, 67, 100, 23);
				conPanel.add(conConvertBtn);
				
				conCombineBtn.setBounds(379, 67, 100, 23);
				conPanel.add(conCombineBtn);
				
			//labels
				conProDirLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
				conProDirLabel.setBounds(10, 10, 160, 14);
				conPanel.add(conProDirLabel);
				
				conPBarLabel.setBounds(10, 56, 80, 14);
				conPanel.add(conPBarLabel);
				
			//progress bar
				conPBar.setBounds(10, 76, 257, 14);
				conPBar.setMinimum(0);
				conPBar.setMaximum(100);
				conPanel.add(conPBar);
				
				
			
		//action listeners
			//adds functionality to the browse button
			btnBrowse.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					File dir = new File("Y:/");
					JFileChooser browse;
					if(dir.isDirectory()){
						browse = new JFileChooser(dir);
					}else{
						browse = new JFileChooser();
					}
					browse.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int returnVal = browse.showDialog(conBrowse, "Select XML Directory");
					if (returnVal == JFileChooser.APPROVE_OPTION){
						conBrowse.setText(browse.getSelectedFile().getAbsolutePath());
					}
					conConvertBtn.setEnabled(true);
				}
			});
			
			//adds functionality to the convert button
			conConvertBtn.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0){
					clear(conTextArea);
					conPBar.setValue(0);
					Runnable convertThread = new Runnable(){
						public void run(){
							conCombineBtn.setEnabled(false);
							btnBrowse.setEnabled(false);
							File wDir = new File(conBrowse.getText());
							File xmlODir = new File(wDir.getAbsolutePath() + "\\Digital Repository XML");
							xmlODir.mkdir();
							if(degGuide.exists() && disGuide.exists() && depGuide.exists()){
								Convert DRXML = null;
								try{
									File[] list = (new File(System.getProperty("user.dir"))).listFiles();
									File xsl = null;
									for(File input : list){
										if(input.getName().endsWith(".xsl")){
											xsl = input;
										}
									}
									InputStream url = null;
									if(xsl != null){
										url = new FileInputStream(xsl);
									}
									
									if(url != null){
										DRXML = new Convert(url);
									}else{
										JOptionPane.showMessageDialog(frmEtdProquestXml, "XSL File Not Found");
									}
								}catch(TransformerConfigurationException e){
									JOptionPane.showMessageDialog(frmEtdProquestXml, "Error Occurred While Constructing Transformer Factory With XSL");
									e.printStackTrace();
								} catch (FileNotFoundException e) {
									JOptionPane.showMessageDialog(frmEtdProquestXml, "XSL File Not Found When Constructing Transformer Factory");
									e.printStackTrace();
								}
								
								if(DRXML != null){
									try{
										File[] pq =  wDir.listFiles();
										boolean check = true;
										clear(editTextArea);
										for(int i = 0; i < pq.length; i++){
											conPBar.setValue(((i + 1) * 100)/ pq.length);
											if(!pq[i].getName().startsWith("ETDmetadata")){
												if(!pq[i].isDirectory()){
													DRXML.declare(pq[i], new File(xmlODir.getAbsolutePath() + "\\" + pq[i].getName()));
													conTextArea.append("Converting: " + pq[i].getName() + "\n");
													DRXML.convert();
													
													ArrayList<StrBool> changedDeg = DRXML.normalize(degGuide, Guide.DEG);
													for(StrBool out : changedDeg){
														conTextArea.append("+degree: " + out.getFrom() + " -> " + out.getTo() + "\n");
														if(!out.getStatus() || out.getTo().equals(" ") || out.getTo().isEmpty()){
															if(!out.getFrom().equals(" ") || !out.getFrom().isEmpty()){
																check = false;
																editTextArea.append(Guide.DEG + "\n" + out.getTo() + ">" + out.getFrom() + "\n");
															}
														}
													}
														
													ArrayList<StrBool> changedDis = DRXML.normalize(disGuide, Guide.DIS);
													for(StrBool out : changedDis){
														conTextArea.append("+discipline: " + out.getFrom() + " -> " + out.getTo() + "\n");
														if(!out.getStatus() || out.getTo().equals(" ") || out.getTo().isEmpty()){
															if(!out.getFrom().equals(" ") && !out.getFrom().isEmpty()){
																check = false;
																editTextArea.append(Guide.DIS + "\n" + out.getTo() + ">" + out.getFrom() + "\n");
															}
														}
													}
													
													ArrayList<StrBool> changedDep = DRXML.normalize(depGuide, Guide.DEP);
													for(StrBool out : changedDep){
														conTextArea.append("+department: " + out.getFrom() + " -> " + out.getTo() + "\n");
														if(!out.getStatus() || out.getTo().equals(" ") || out.getTo().isEmpty()){
															if(!out.getFrom().equals(" ") || !out.getFrom().isEmpty()){
																check = false;
																editTextArea.append(Guide.DEP + "\n" + out.getTo() + ">" + out.getFrom() + "\n");
															}
														}
													}
												}
											}
											conTextArea.append("\n");
										}
										if(!check){
											editLoadBtn.setEnabled(false);
											rdbtnBatch.setSelected(true);
											String notify = "Not all Departments, Degrees, or Disciplines were converted.\nSee Edit Guides tab for more options." +
													"\nConversion will need to be run again if any guides are edited.";
											JOptionPane.showMessageDialog(frmEtdProquestXml, notify);
										}
									}catch(Exception e){
										JOptionPane.showMessageDialog(frmEtdProquestXml,  "Error Occurred While Transforming With XSL.");
										e.printStackTrace();
									}
								}
								
							}else{
								JOptionPane.showMessageDialog(frmEtdProquestXml, "Guide(s) Not Found In Program Directory");
							}
							File dir = new File(conBrowse.getText() + "//Digital Repository XML");
							File[] list = dir.listFiles();
							Document doc = null;
							NodeList nodes = null;
								String notApproved = "";
								for(int i = 0; i < list.length; i++){
									if(list[i].isFile() && list[i].getName().endsWith(".xml") && !list[i].getName().toUpperCase().startsWith("ETDMETADATA")){
										try{
											doc = DocumentBuilderFactory.newInstance()
													.newDocumentBuilder().parse(new InputSource(list[i].getAbsolutePath()));
									        
										}catch(Exception e){
											JOptionPane.showMessageDialog(frmEtdProquestXml, "Error occurred while parsing " + list[i].getName() + " XML. Check fulltext URLs manually.");
											e.printStackTrace();
										}
										
										try{
											XPath xpath = XPathFactory.newInstance().newXPath();
											nodes = (NodeList)xpath.evaluate("//documents/document/fulltext-url" , doc, XPathConstants.NODESET);
										}catch(Exception e){
											conTextArea.append(list[i].getName() + " is an improperly formatted XML and does not \n"
													+ "contain the fulltext URL in a appropriate location\n");
											e.printStackTrace();
										}
										if(nodes != null){
											if(nodes.item(0).getTextContent().isEmpty() || nodes.item(0).getTextContent().equals(" ")){
												notApproved += (list[i].getName() + "\n");
											}
										}
										
									}
									
									
								}
								if(!notApproved.isEmpty()){
									conTextArea.append("The following files are not yet approved for publishing:\n");
									conTextArea.append(notApproved);
								}
								
								btnBrowse.setEnabled(true);
								conCombineBtn.setEnabled(true);
						}
						
					};
						Thread thr = new Thread(convertThread);
						thr.start();
					}
			});
			
			//adds functionality to the combine button
			conCombineBtn.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0){
					File xmlO = new File(conBrowse.getText() + "\\Digital Repository XML");
					if(xmlO.exists()){
						Combine dir = new Combine(xmlO);
						try{
							dir.combine();
							conTextArea.append("Combine Was Successful");
						}catch(IOException e){
							conTextArea.append("Combine Was Unsuccessful");
							e.printStackTrace();
						}
					}
					btnBrowse.setEnabled(true);
				}
			});
		
	// TODO Edit Guides Pane
		//component declarations
			//panel
				JPanel editPanel = new JPanel();
				
			//text area declared above
			
			//buttons
				//Edit Tab Load button declared above for access by Convert Tab operations
				JButton btnClear = new JButton("Clear");
				JButton btnApply = new JButton("Apply");
				
			//comboboxes
				final JComboBox<String> editComboBox = new JComboBox<String>();
				
			//radial buttons declared above
			
			//labels
				JLabel editFromProLabel = new JLabel("From (ProQuest):");
				JLabel editToDigLabel = new JLabel("To (Digital Repository):");
				JLabel editModeLabel = new JLabel("Mode:");
			
		//component constraints
			//panel
				tabbedPane.addTab("Edit Guides", null, editPanel, null);
				editPanel.setLayout(null);
				
			//text area
				editTextArea.setFont(new Font("Monospaced", Font.BOLD, 13));
				JScrollPane scroll_1 = new JScrollPane(editTextArea);
				scroll_1.setBounds(10, 55, 469, 214);
				scroll_1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				scroll_1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				editPanel.add(scroll_1);
			
			//buttons
				editLoadBtn.setEnabled(false);
				editLoadBtn.setBounds(291, 11, 89, 24);
				editPanel.add(editLoadBtn);
				
				btnClear.setBounds(390, 11, 89, 24);
				editPanel.add(btnClear);
				
				btnApply.setBounds(390, 280, 89, 24);
				editPanel.add(btnApply);
				
			//comboboxes
				editComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"[Select Guide]", "Degree Guide", "Discipline Guide", "Department Guide"}));
				editComboBox.setBounds(10, 11, 271, 24);
				editPanel.add(editComboBox);
				
			//radial buttons
				rdbtnDelete.setBounds(87, 291, 75, 23);
				editPanel.add(rdbtnDelete);
				
				rdbtnBatch.setBounds(241, 291, 100, 23);
				editPanel.add(rdbtnBatch);
				
				rdbtnEdit.setBounds(10, 291, 75, 23);
				editPanel.add(rdbtnEdit);
				
				rdbtnAdd.setBounds(164, 291, 75, 23);
				editPanel.add(rdbtnAdd);
				
				ButtonGroup bg = new ButtonGroup();
				bg.add(rdbtnDelete);
				bg.add(rdbtnEdit);
				bg.add(rdbtnBatch);
				bg.add(rdbtnAdd);
				rdbtnEdit.setSelected(true);
				
			//labels
				editFromProLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
				editFromProLabel.setBounds(10, 36, 200, 20);
				editPanel.add(editFromProLabel);
				
				editToDigLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
				editToDigLabel.setBounds(291, 36, 148, 20);
				editPanel.add(editToDigLabel);
				
				editModeLabel.setBounds(10, 276, 46, 14);
				editPanel.add(editModeLabel);
			
		//action listeners
			//adds functionality to the dropdown menu
			editComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					if(editComboBox.getSelectedIndex() != 0 && !(rdbtnBatch.isSelected() || rdbtnAdd.isSelected()))editLoadBtn.setEnabled(true);
					else editLoadBtn.setEnabled(false);
				}
			});
			
			//adds functionality to the load button
			editLoadBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						clear(editTextArea);
						Scanner fieldInput = new Scanner(guideType(editComboBox.getSelectedIndex()));
						while(fieldInput.hasNextLine()){
							String next = fieldInput.nextLine();
							if(!next.isEmpty() || !next.equals(" ")){
								editTextArea.append(next + "\n");
							}
						}
						fieldInput.close();
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(frmEtdProquestXml, "Guide Not Found");
						e1.printStackTrace();
					}catch (NullPointerException e2){
						//should be now defunct
						JOptionPane.showMessageDialog(frmEtdProquestXml,  "Please Select a Guide");
						return;
					}
					editLoadBtn.setEnabled(false);
					editComboBox.setEnabled(false);
					rdbtnBatch.setEnabled(false);
					rdbtnAdd.setEnabled(false);
					if(rdbtnBatch.isSelected() || rdbtnAdd.isSelected())rdbtnEdit.setSelected(true);
				}
			});
			
			//adds functionality to the clear button
			btnClear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					clear(editTextArea);
					editLoadBtn.setEnabled(true);
					editComboBox.setEnabled(true);
					rdbtnAdd.setEnabled(true);
					rdbtnBatch.setEnabled(true);
				}
			});
			
			//adds functionality to the batch add radial button
			rdbtnBatch.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					if(rdbtnBatch.isSelected()){
						editLoadBtn.setEnabled(false);
					}
					
				}
				
			});
			
			//adds functionality to the add radial button
			rdbtnAdd.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					if(rdbtnAdd.isSelected()){
						editLoadBtn.setEnabled(false);
					}
					
				}
				
			});
			
			//adds functionality to the apply button
			btnApply.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Scanner text = new Scanner(editTextArea.getText());
					ArrayList<String> rules = new ArrayList<String>();
					while(text.hasNextLine()){
						String line = text.nextLine();
						if(!line.isEmpty()){
							rules.add(line);
						}
					}
					if(rdbtnEdit.isSelected()){
						
						if(editComboBox.getSelectedIndex() == 0){
							JOptionPane.showMessageDialog(frmEtdProquestXml,  "Please Select a Guide");
						}else{
							try {
								if(checkSize(guideType(editComboBox.getSelectedIndex()), rules) > 0){
									JOptionPane.showMessageDialog(frmEtdProquestXml, "List of Rules in Text Area is shorter than in selected guide." +
											"\nTo delete rules select Delete mode below.");
								}else{
									new UpdateGuide(guideType(editComboBox.getSelectedIndex()), 
											archGuideType(editComboBox.getSelectedIndex()));
									FileWriter fstream = new FileWriter(guideType(editComboBox.getSelectedIndex()));
									BufferedWriter out = new BufferedWriter(fstream);
									for(String line : rules){
										out.write(line);
										out.newLine();
									}
									out.close();
								}
							}catch (FileNotFoundException e) {
								JOptionPane.showMessageDialog(frmEtdProquestXml, "Guide or Archive File Not Found");
								e.printStackTrace();
							} catch (IOException e) {
								JOptionPane.showMessageDialog(frmEtdProquestXml, "An Unknown Error Has Occurred");
								e.printStackTrace();
							}
						}
					}
					
					if(rdbtnBatch.isSelected()){
						if(rules.isEmpty()){
							JOptionPane.showMessageDialog(frmEtdProquestXml, "The Text Area is Empty. Please Enter Rules to be Added.");
							return;
						}
						Sort output = new Sort(rules);
						if(output.getDep().isEmpty() && output.getDeg().isEmpty() && output.getDis().isEmpty()){
								JOptionPane.showMessageDialog(frmEtdProquestXml, "Improperly Formatted Rules for a Batch Add");
								return;
						}else{
							if(!output.getDep().isEmpty()){
								try {
									if(!checkFormat(output.getDep())){
										JOptionPane.showMessageDialog(frmEtdProquestXml, "Text Area Contains Improperly Formatted Rules");
										return;
									}
									UpdateGuide dep = new UpdateGuide(depGuide, archDepGuide);
									dep.add(output.getDep());
								} catch (IOException e) {
									JOptionPane.showMessageDialog(frmEtdProquestXml, "Error Occurred While Updating Department Guide");
									e.printStackTrace();
								}
							}
							
							if(!output.getDis().isEmpty()){
								try {
									if(!checkFormat(output.getDis())){
										JOptionPane.showMessageDialog(frmEtdProquestXml, "Text Area Contains Improperly Formatted Rules.");
										return;
									}
									UpdateGuide dis = new UpdateGuide(disGuide, archDisGuide);
									dis.add(output.getDis());
								} catch (IOException e) {
									
									JOptionPane.showMessageDialog(frmEtdProquestXml, "Error Occurred While Updating Discipline Guide.");
									e.printStackTrace();
								}
							}
							if(!output.getDeg().isEmpty()){
								try {
									if(!checkFormat(output.getDeg())){
										JOptionPane.showMessageDialog(frmEtdProquestXml, "Text Area Contains Improperly Formatted Rules.");
										return;
									}
									UpdateGuide deg = new UpdateGuide(degGuide, archDegGuide);
									deg.add(output.getDeg());
								} catch (IOException e) {
									JOptionPane.showMessageDialog(frmEtdProquestXml, "Error Occurred While Updating Degree Guide.");
									e.printStackTrace();
								}
							}
						}
					}
					
					if(rdbtnAdd.isSelected()){
						Scanner input = new Scanner(editTextArea.getText());
						ArrayList<String> toAdd = new ArrayList<String>();
						while(input.hasNextLine()){
							String next = input.nextLine();
							if(!next.isEmpty())toAdd.add(next);
						}
						input.close();
						if(editComboBox.getSelectedIndex() == 0){
							JOptionPane.showMessageDialog(frmEtdProquestXml,  "Please Select a Guide.");
							return;
						}if(editTextArea.getText().isEmpty()){
							JOptionPane.showMessageDialog(frmEtdProquestXml, "The Text Area is Empty. Please Enter Rules to be Added.");
							return;
						}if(!checkFormat(toAdd)){
							JOptionPane.showMessageDialog(frmEtdProquestXml, "Text Area Contains Improperly Formatted Rules.");
							return;
						}else{
							try {
								
								UpdateGuide update = new UpdateGuide(guideType(editComboBox.getSelectedIndex()), 
										archGuideType(editComboBox.getSelectedIndex()));
								update.add(toAdd);
							} catch (FileNotFoundException e1) {
								JOptionPane.showMessageDialog(frmEtdProquestXml, "Guide or Archive File Not Found");
								e1.printStackTrace();
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(frmEtdProquestXml, "An Unknown Error Has Occurred.");
								e1.printStackTrace();
							}
							
						}
					}
					
					if(rdbtnDelete.isSelected()){
						if(editComboBox.getSelectedIndex() == 0){
							JOptionPane.showMessageDialog(frmEtdProquestXml,  "Please Select a Guide.");
							return;
						}if(rules.isEmpty()){
							JOptionPane.showMessageDialog(frmEtdProquestXml, "This Action Would Clear the Guide and is Not Allowed.");
							return;
						}if(!checkFormat(rules)){
							JOptionPane.showMessageDialog(frmEtdProquestXml, "Text Area Contains Improperly Formatted Rules.");
							return;
						}else{
							try {
								int selection = JOptionPane.YES_OPTION;
								if(checkSize(guideType(editComboBox.getSelectedIndex()), rules) > 1){
									selection = JOptionPane.showConfirmDialog(frmEtdProquestXml, "This Action Would Remove More Than Half of the"+
											"Selected Guide. Continue?");
								}else{
									selection = JOptionPane.showConfirmDialog(frmEtdProquestXml, "Continue?");
								}
								if(selection == JOptionPane.YES_OPTION){
									new UpdateGuide(guideType(editComboBox.getSelectedIndex()), 
											archGuideType(editComboBox.getSelectedIndex()));
									FileWriter fstream = new FileWriter(guideType(editComboBox.getSelectedIndex()));
									BufferedWriter out = new BufferedWriter(fstream);
									for(String line : rules){
										out.write(line);
										out.newLine();
									}
									out.close();
								}
							} catch (IOException e) {
								JOptionPane.showMessageDialog(frmEtdProquestXml, "Cannot Edit Guide");
								e.printStackTrace();
							}
						}
						
					}
				}
			});
		
	// TODO Guide History Pane
		//component declarations
			//panel
				JPanel hisPanel = new JPanel();
			//text area declared above
			
			//buttons
				final JButton hisLoadBtn = new JButton("Load");
				JButton hisClearBtn = new JButton("Clear");
				final JButton btnRestore = new JButton("Restore");
				
			//comboboxes
				final JComboBox<String> hisComboBox1 = new JComboBox<String>();
				final JComboBox<Files> hisComboBox2 = new JComboBox<Files>();
				
			//labels
			
			
		//component constraints
			//panel
				tabbedPane.addTab("Guide History", null, hisPanel, null);
				hisPanel.setLayout(null);
			
			//text area
				hisTextArea.setEditable(false);
				hisTextArea.setFont(new Font("Monospaced", Font.BOLD, 13));
				JScrollPane scrollPane = new JScrollPane(hisTextArea);
				scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				scrollPane.setBounds(10, 79, 469, 214);
				hisPanel.add(scrollPane);
			
			//buttons
				hisLoadBtn.setEnabled(false);
				hisLoadBtn.setBounds(291, 11, 89, 24);
				hisPanel.add(hisLoadBtn);
				
				hisClearBtn.setBounds(390, 11, 89, 24);
				hisPanel.add(hisClearBtn);
				
				btnRestore.setEnabled(false);
				btnRestore.setBounds(390, 45, 89, 24);
				hisPanel.add(btnRestore);
				
			//comboboxes
				hisComboBox1.setModel(new DefaultComboBoxModel<String>(new String[] {"[Select Guide Archive]", "Degree Archive", "Discipline Archive", "Department Archive"}));
				hisComboBox1.setBounds(10, 11, 271, 24);
				hisPanel.add(hisComboBox1);
				
				hisComboBox2.setEnabled(false);
				hisComboBox2.setBounds(10, 45, 271, 24);
				hisPanel.add(hisComboBox2);
				
			//labels
		//action listeners
			//adds functionality to the dropdown menus
			hisComboBox1.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0){
					if(hisComboBox1.getSelectedIndex() == 0){
						hisComboBox2.setEnabled(false);
						hisLoadBtn.setEnabled(false);
						btnRestore.setEnabled(false);
						hisComboBox2.removeAllItems();
					}else{
						hisComboBox2.setEnabled(true);
						hisLoadBtn.setEnabled(true);
						File[] archiveDir = null;
						Files[] fileNames = null;
						if(hisComboBox1.getSelectedIndex() == 1)archiveDir = archDegGuide.listFiles();
						if(hisComboBox1.getSelectedIndex() == 2)archiveDir = archDisGuide.listFiles();
						if(hisComboBox1.getSelectedIndex() == 3)archiveDir = archDepGuide.listFiles();
						fileNames = new Files[archiveDir.length];
						for(int i = 0; i < archiveDir.length; i ++){
							fileNames[i] = new Files(archiveDir[i]);
						}
						hisComboBox2.setModel(new DefaultComboBoxModel<Files>(fileNames));
					}
				}
			});				
			
			//adds functionality to the load button
			hisLoadBtn.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0){
					Files toLoad = (Files) hisComboBox2.getSelectedItem();
					clear(hisTextArea);
					hisComboBox1.setEnabled(false);
					hisComboBox2.setEnabled(false);
					try{
						Scanner text = new Scanner(toLoad.getFile());
						while(text.hasNextLine()){
							String next = text.nextLine();
							hisTextArea.append(next + "\n");
						}
						
						text.close();
					}catch(FileNotFoundException e){
						JOptionPane.showMessageDialog(frmEtdProquestXml, "Archive File Not Found");
						e.printStackTrace();
						return;
					}
					if(!((Files)hisComboBox2.getSelectedItem()).toString().equals("Add Log"))btnRestore.setEnabled(true);
					hisLoadBtn.setEnabled(false);
				}
			});
			
			//adds functionality to the clear button
			hisClearBtn.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0){
					clear(hisTextArea);
					hisComboBox1.setEnabled(true);
					hisComboBox2.setEnabled(true);
					hisLoadBtn.setEnabled(true);
					btnRestore.setEnabled(false);
				}
			});
			
			//adds functionality to the restore button
			btnRestore.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0){
					try{
						
						new UpdateGuide(guideType(hisComboBox1.getSelectedIndex()), 
								new File(((Files)hisComboBox2.getSelectedItem()).getFile().getParent()));
						FileWriter fstream = new FileWriter(guideType(hisComboBox1.getSelectedIndex()));
						BufferedWriter out = new BufferedWriter(fstream);
						Scanner rules = new Scanner(((Files)hisComboBox2.getSelectedItem()).getFile());
						while(rules.hasNextLine()){
							out.write(rules.nextLine());
							out.newLine();
						}
						rules.close();
						out.close();
					}catch(IOException e){
						JOptionPane.showMessageDialog(frmEtdProquestXml, "Error Occurred While Restoring Guide");
						e.printStackTrace();
					}
				}
			});
	}
	
	// TODO helper methods
	
	/**The Check Size helper method compares the number of rules in the text area to the most recent save of
	 * the currently selected guide.
	 * 
	 * @param guide File object of the most recent guide that's currently selected.
	 * @param rules An ArrayList with each element being a string representing each one of the crosswalks.
	 * @return 	Returns 2 if number of rules in the edit guides text area is less than half, 1 if it's less, 0 if equal or greater.
	 */
	public int checkSize(File guide, ArrayList<String> rules) throws FileNotFoundException{
		int guideLength = 0;
		
		Scanner reader = new Scanner(guide);
		while(reader.hasNextLine()){
			String next = reader.nextLine();
			if(!next.isEmpty())guideLength++;
		}
		reader.close();
		if(guideLength > (rules.size()*2))return 2;
		if(guideLength > rules.size())return 1;
		else return 0;
	}
	
	/**CheckFormat takes an ArrayList representation of the crosswalks that are being added/edited to see if they're the
	 * appropriate "format". Hard to do other than to see if it contains a "greater than" sign or if the line is
	 * relatively short.
	 * 
	 * @param rules ArrayList representation of the crosswalks in the edit guides text area.
	 * @return returns true if all the crosswalks are "properly formatted" and false otherwise.
	 */
	public boolean checkFormat(ArrayList<String> rules){
		for(String line : rules){
			if(!line.contains(">") || line.length() < 5){
				return false;
			}
		}
		return true;
	}
	
	/**guideType takes an int index and returns a File object of the int index.
	 * 
	 * @param input int index of the dropdown menu.
	 * @return returns a File object of the selected menu item.
	 */
	public File guideType(int input){
		if(input == 1){
			return degGuide;
		}
		if(input == 2){
			return disGuide;
		}
		if(input == 3){
			return depGuide;
		}
		else return null;
	}
	
	/**archGuide takes an index and returns a File object of the target archive directory.
	 * 
	 * @param input The index of the archive file that is selected in a drop down menu.
	 * @return returns a File object of the archive directory of the corresponding selected index.
	 */
	public File archGuideType(int input){
		if(input == 1){
			return archDegGuide;
		}
		if(input == 2){
			return archDisGuide;
		}
		if(input == 3){
			return archDepGuide;
		}
		else return null;
	}
	
	/**Clear is just a helper method that empties the inputted textArea.
	 * 
	 * @param input The textArea that needs to be reset/emptied.
	 */
	public void clear(JTextArea input){
		input.setText("");
	}
	
	/**Recursive function for clearing out a directory and all of its contents.
	 * 
	 * @param resource The file or directory that needs to be emptied.
	 * @return returns true if the item put into the input was deleted.
	 * @throws IOException Throws this exception if file isn't found or if a file or directory can't be removed.
	 */
	public static boolean delete(File resource) throws IOException { 
		if(resource.isDirectory()) {
			File[] childFiles = resource.listFiles();
			for(File child : childFiles) {
				delete(child);
			}
						
		}
		return resource.delete();
		
	}
	
	/**copyFile takes two File objects and copies the contents from one to the other.
	 * 
	 * @param source The source file that needs to be copied.
	 * @param dest Where the source file is being copied to.
	 * @throws IOException Throws file not found exception if source can't be found, or if either the source or destination file can't be read/written to.
	 */
	public static void copyFile(File source, File dest) throws IOException {
		
		if(!dest.exists()) {
			dest.createNewFile();
		}
        InputStream in = null;
        OutputStream out = null;
        try {
        	in = new FileInputStream(source);
        	out = new FileOutputStream(dest);
    
	        byte[] buf = new byte[1024];
	        int len;
	        while ((len = in.read(buf)) > 0) {
	            out.write(buf, 0, len);
	        }
        }
        finally {
        	in.close();
            out.close();
        }
        
	}
	
	/**A recursive directory copying helper method. Copies the contents from a specified directory to
	 * a target directory.
	 * 
	 * @param sourceDir A file object representing a directory that needs to be copied.
	 * @param destDir Where the file object is being copied to.
	 * @throws IOException If either directory can't be found, or if any of the files or directories can't be read or written to.
	 */
	public static void copyDirectory(File sourceDir, File destDir) throws IOException {
		
		if(!destDir.exists()) {
			destDir.mkdir();
		}
		
		File[] children = sourceDir.listFiles();
		
		for(File sourceChild : children) {
			String name = sourceChild.getName();
			File destChild = new File(destDir, name);
			if(sourceChild.isDirectory()) {
				copyDirectory(sourceChild, destChild);
			}
			else {
				copyFile(sourceChild, destChild);
			}
		}	
	}
	
	
	/**This nestled helper class monitors key presses and opens the debug menu if ALT + SHIFT + D is pressed.
	 *
	 */
	public class Dispatcher implements KeyEventDispatcher{

		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if(e.getID() == KeyEvent.KEY_RELEASED && (e.getKeyCode() == KeyEvent.VK_D) 
					&& ((e.getModifiers() & KeyEvent.ALT_MASK) != 0) && (e.getModifiers() & KeyEvent.SHIFT_MASK) != 0){
				final JScrollPane debugScroll = new JScrollPane(debug);
				
				debugScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				debugScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				Object options[] = {debugScroll};
				clear(debug);
				debug.append(myErr.toString());
				JOptionPane.showOptionDialog(null, "", "Debug", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			}
			return false;
		}

	}
}
