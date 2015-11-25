import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DefaultRowSorter;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class MainWindow {

	private JFrame frame;
	private DefaultTableModel model;
	private JTextField textField;
	private JTable table;
	private JList list;
	private Dictionary dict;
	private DefaultListModel lemmaList;
	private ArrayList<String> lemmas;
	private Corpus corpus;
	private String lemma;
	private String position;
	private JLabel topLabel;
	private Properties settings;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Properties settings = new Properties();
					File settingsFile = new File("settings.properties");
					MainWindow window = new MainWindow(settings);
					if(settingsFile.exists()){
						FileReader reader = new FileReader(settingsFile);
						settings.load(reader);
						String dictLocation = settings.getProperty("dictionary");
						String corpusLocation = settings.getProperty("corpus");
						if(!dictLocation.equals(null)){
							window.newDict(dictLocation);
						}
						if(!corpusLocation.equals(null)){
							window.newCorpus(corpusLocation);
						}
					}
					window.frame.setVisible(true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public MainWindow(Properties settings) throws IOException {
		this.settings = settings;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		//Set up menu bar
		JMenuBar menuBar = new JMenuBar();
		frame.getContentPane().add(menuBar, BorderLayout.NORTH);

		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);

		JMenuItem mntmFile = new JMenuItem("Load Transriptions");
		mntmFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = chooser.showOpenDialog(frame);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					newCorpus(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		mnNewMenu.add(mntmFile);

		JMenuItem mntmDict = new JMenuItem("Load Dictionary");

		mntmDict.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnVal = chooser.showOpenDialog(frame);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					newDict(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		mnNewMenu.add(mntmDict);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		panel.add(splitPane, BorderLayout.CENTER);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(BorderFactory.createLineBorder(Color.black));
		splitPane.setLeftComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.NORTH);

		textField = new JTextField();
		panel_3.add(textField);
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setColumns(22);
		textField.setEnabled(false);
		textField.setText("No Dictionary Loaded");

		list = new JList();

		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textField.setText("");
			}
		});
		JScrollPane listScroll = new JScrollPane(list);
		panel_1.add(listScroll, BorderLayout.CENTER);

		//newDict();

		JPanel panel_4 = new JPanel();
		panel_1.add(panel_4, BorderLayout.SOUTH);
		panel_4.setLayout(new BorderLayout(0, 0));

		JComboBox comboBox = new JComboBox();
		comboBox.addItem("preceding");
		comboBox.addItem("following");
		comboBox.addItem("both");
		comboBox.setSelectedIndex(0);
		panel_4.add(comboBox, BorderLayout.CENTER);

		JButton btnSearch = new JButton("Search");
		panel_4.add(btnSearch, BorderLayout.SOUTH);

		JLabel lblNewLabel = new JLabel("Search for words:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel_4.add(lblNewLabel, BorderLayout.NORTH);

		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(dict == null) {
					JOptionPane.showMessageDialog(frame,
							"Please load dictionary first.", "Load Dictionary", JOptionPane.ERROR_MESSAGE);
				}
				else {
					lemma = "";
					if(textField.getText().equals("")){
						lemma = (String) list.getSelectedValue();
					}
					else {
						lemma = textField.getText();
					}
					if(corpus == null){
						JOptionPane.showMessageDialog(frame, "Please load transcriptions first.",
								"Load Transcriptions", JOptionPane.ERROR_MESSAGE);
					}
					else{


						HashMap<String, Integer> results = null;
						position = (String) comboBox.getSelectedItem();
						try {
							results = Search.search(lemma, position, dict);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						int total = results.get("TERM_TOTAL");
						model = new DefaultTableModel(){
							@Override
							public Class getColumnClass(int column){
								if(column == 2){
									return Double.class;
								}
								else{
									return String.class;
								}
							}
						};
						model.addColumn("Appearing with search term");
						model.addColumn("Gloss (meaning)");
						model.addColumn("Frequency");
						
						DecimalFormat format = new DecimalFormat("##.#");
						for (String key : results.keySet()) {
							if(!key.equals("TERM_TOTAL")){
								String gloss = "";
								if(dict.getLemmaList().contains(key)) {
									ArrayList<String> glosses = dict.getGlossList(key);
									for(int i = 1; i < glosses.size(); i++) {
										if(i!=1) {
											gloss = gloss + ", ";
										}
										gloss = gloss + glosses.get(i);
									}
								}
								NumberFormat defaultFormat = NumberFormat.getPercentInstance();
								String freqString = format.format((results.get(key)/(double) total)*100);
								Object[] arr = {key, gloss, Double.parseDouble(freqString)};
								model.addRow(arr);
							}
						}
						String gloss = "";
						ArrayList<String> glosses = dict.getGlossList(lemma);
						for(int i = 1; i < glosses.size(); i++) {
							if(i!=1) {
								gloss = gloss + ", ";
							}
							gloss = gloss + glosses.get(i);
						}

						topLabel.setText("Found the lemma \""+lemma+"\" (" + gloss + ") " + total +" times.");
						table.setModel(model);
						DefaultRowSorter sorter = new TableRowSorter(model);
						table.setRowSorter(sorter);
					}
				}
			}
		});

		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(dict == null) {
					textField.setText("");
					JOptionPane.showMessageDialog(frame,
							"Please load dictionary first.", "Load Dictionary", JOptionPane.ERROR_MESSAGE);
				}
				else {
					String text = textField.getText();
					lemmas = dict.getLemmaList(text);
					lemmaList.removeAllElements();
					for(int i = 0; i < lemmas.size(); i++){
						lemmaList.addElement(lemmas.get(i));
					}
					list.setModel(lemmaList);
					list.updateUI();
				}
			}
		});

		JPanel panel_2 = new JPanel();
		splitPane.setRightComponent(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));

		table = new JTable();
		JScrollPane tableScroll = new JScrollPane(table);
		panel_2.add(tableScroll);

		JPanel panel_5 = new JPanel();
		panel_2.add(panel_5, BorderLayout.SOUTH);

		JButton btnNewButton = new JButton("Advanced Search");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] possibilities = {"0","1", "2", "3", "4"};
				String s = (String)JOptionPane.showInputDialog(
						frame,
						"Select how many words you want inbetween:\n",
						"Advanced Search",
						JOptionPane.PLAIN_MESSAGE,
						null,
						possibilities,
						"0");
				int numBtwn = Integer.parseInt(s);

				HashMap<String, Double> results = null;
				try {
					results = AdvancedSearch.advancedSearch(lemma, numBtwn, position, dict);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				model = new DefaultTableModel();
				model.addColumn("Appearing with search term");
				model.addColumn("Gloss (meaning)");
				model.addColumn("Frequency");
				for (String key : results.keySet()) {
					String gloss = "";
					if(dict.getLemmaList().contains(key)) {
						ArrayList<String> glosses = dict.getGlossList(key);
						for(int i = 1; i < glosses.size(); i++) {
							if(i!=1) {
								gloss = gloss + ", ";
							}
							gloss = gloss + glosses.get(i);
						}
					}
					NumberFormat defaultFormat = NumberFormat.getPercentInstance();
					String[] arr = {key, gloss, new DecimalFormat("##.#").format(results.get(key)*100)};
					model.addRow(arr);
				}
				String gloss = "";
				ArrayList<String> glosses = dict.getGlossList(lemma);
				for(int i = 1; i < glosses.size(); i++) {
					if(i!=1) {
						gloss = gloss + ", ";
					}
					gloss = gloss + glosses.get(i);
				}

				topLabel.setText("Found the lemma \""+lemma+"\" (" + gloss + ") " + results.keySet().size()+" times with " + numBtwn +" words inbetween.");
				table.setModel(model);
				DefaultRowSorter sorter = new TableRowSorter(model);
				table.setRowSorter(sorter);
			}
		});

		panel_5.add(btnNewButton);

		//TODO: Add to dictionary button
		//JButton btnNewButton_1 = new JButton("Add to Dictionary");
		//panel_5.add(btnNewButton_1);

		topLabel = new JLabel();
		topLabel.setText("No Corpus Loaded");
		panel_2.add(topLabel, BorderLayout.NORTH);

	}

	public void newCorpus(String filename){
		corpus = new Corpus(filename);
		new Search(corpus);
		if(corpus.getFiles().isEmpty()){
			JOptionPane.showMessageDialog(frame,
					"No Readable Transcriptions Found.", "No Transcriptions", JOptionPane.ERROR_MESSAGE);
		}
		else{
			topLabel.setText("Transcriptions Loaded");
			settings.setProperty("corpus", filename);
			saveSettings();
		}
	}

	public void newDict(String filename){
		try {
			dict = new Dictionary(filename);
			lemmaList = new DefaultListModel();
			lemmas = dict.getLemmaList();
			for(int i = 0; i < lemmas.size(); i++){
				lemmaList.addElement(lemmas.get(i));
			}

			list.setModel(lemmaList);
			textField.setEnabled(true);
			textField.setText("");
			settings.setProperty("dictionary", filename);
			saveSettings();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void saveSettings(){
		File settingsFile = new File("settings.properties");
		try{
			FileWriter writer = new FileWriter(settingsFile);
			settings.store(writer, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}