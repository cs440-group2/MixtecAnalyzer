import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import java.awt.BorderLayout;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DefaultRowSorter;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JSplitPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import net.miginfocom.swing.MigLayout;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JTable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JComboBox;
import javax.swing.JLabel;

public class MainWindow {

	private JFrame frame;
	private DefaultTableModel model;
	private JTextField textField;
	private JTable table;
	private JList list;
	private Dictionary dict;
	private DefaultListModel lemmaList;
	private ArrayList<String> lemmas;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public MainWindow() throws IOException {
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
		
		
		dict = new Dictionary();
		lemmaList = new DefaultListModel();
		lemmas = dict.getLemmaList();
		for(int i = 0; i < lemmas.size(); i++){
			lemmaList.addElement(lemmas.get(i));
		}
		
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
			       System.out.println("You chose to open this folder: " +
			            chooser.getSelectedFile().getName());
			    }
			}
		});
		mnNewMenu.add(mntmFile);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Settings");
		mnNewMenu.add(mntmNewMenuItem);
		
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
		textField.setColumns(10);
		
		JList list = new JList();
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textField.setText("");
			}
		});
		JScrollPane listScroll = new JScrollPane(list);
		panel_1.add(listScroll, BorderLayout.CENTER);
		list.setModel(lemmaList);
		
		JPanel panel_4 = new JPanel();
		panel_1.add(panel_4, BorderLayout.SOUTH);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JComboBox comboBox = new JComboBox();
		comboBox.addItem("preceding");
		comboBox.addItem("following");
		comboBox.setSelectedIndex(0);
		panel_4.add(comboBox, BorderLayout.CENTER);
		
		JButton btnSearch = new JButton("Search");
		panel_4.add(btnSearch, BorderLayout.SOUTH);
		
		JLabel lblNewLabel = new JLabel("Search for words:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel_4.add(lblNewLabel, BorderLayout.NORTH);
		
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String lemma = "";
				if(textField.getText().equals("")){
					lemma = (String) list.getSelectedValue();
				}
				else {
					lemma = textField.getText();
				}
				HashMap<String, Double> results = null;
				String position = (String) comboBox.getSelectedItem();
				try {
					results = Search.search(lemma, position);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				model = new DefaultTableModel();
				model.addColumn("Appearing with search term");
				model.addColumn("Gloss (meaning)");
				model.addColumn("Frequency");
				for (String key : results.keySet()) {
					String[] arr = {key,"", results.get(key).toString()};
					model.addRow(arr);
				}
				table.setModel(model);
				DefaultRowSorter sorter = new TableRowSorter(model);
				table.setRowSorter(sorter);
			}
		});
		
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				String text = textField.getText();
				lemmas = dict.getLemmaList(text);
				System.out.println(lemmas);
				lemmaList.removeAllElements();
				for(int i = 0; i < lemmas.size(); i++){
					lemmaList.addElement(lemmas.get(i));
				}
				list.setModel(lemmaList);
				list.updateUI();
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
		//create advanced search option
		panel_5.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Add to Dictionary");
		panel_5.add(btnNewButton_1);
		
	}
 
}