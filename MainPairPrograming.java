
package com.aaron;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.*;


public class MainPairPrograming extends JFrame implements ActionListener{
	JMenuBar theMenuBar;
	JMenu fileMenu, operationMenu, settingsMenu;
	JMenuItem importJMI, generateJMI, saveJMI ,closeJMI ,showJMI, bridgeWordsJMI, newTextJMI;
	JMenuItem shortestPathJMI, randomlyTravelJMI, interfaceJMI, testJMI;
	JLabel label;
	Canvas panelForDrawing;
	
	private static final long serialVersionUID = -8534844170998963067L;
	

	MainPairPrograming(){
		super("PairProgramming");
		
		theMenuBar = new JMenuBar();
		fileMenu = new JMenu("File(F)");
		operationMenu = new JMenu("Operation(O)");
		settingsMenu = new JMenu("Settings(S)");
		
		importJMI = new JMenuItem("Import(I)");
		generateJMI = new JMenuItem("Generate directed graph(G)");
		saveJMI = new JMenuItem("Save the picture(V)");
		closeJMI = new JMenuItem("Close the file(C)");
		showJMI = new JMenuItem("Show directed graph(S)");
		bridgeWordsJMI = new JMenuItem("Find bridge words(B)");
		newTextJMI = new JMenuItem("Formulate new text(N)");
		shortestPathJMI = new JMenuItem("Calculate shortest path(P)");
		randomlyTravelJMI = new JMenuItem("Randomly travel(T)");
		interfaceJMI = new JMenuItem("Interface settings(U)");
		testJMI = new JMenuItem("Test(X)");
		
		setJMenuBar(theMenuBar);
		theMenuBar.add(fileMenu);
		theMenuBar.add(operationMenu);
		theMenuBar.add(settingsMenu);
		fileMenu.add(importJMI);
		fileMenu.add(generateJMI);
		fileMenu.add(saveJMI);
		fileMenu.add(closeJMI);
		operationMenu.add(showJMI);
		operationMenu.add(bridgeWordsJMI);
		operationMenu.add(newTextJMI);
		operationMenu.add(shortestPathJMI);
		operationMenu.add(randomlyTravelJMI);
		settingsMenu.add(interfaceJMI);
		settingsMenu.add(testJMI);
		
		importJMI.addActionListener(this);
		generateJMI.addActionListener(this);
		saveJMI.addActionListener(this);
		closeJMI.addActionListener(this);
		showJMI.addActionListener(this);
		bridgeWordsJMI.addActionListener(this);
		newTextJMI.addActionListener(this);
		shortestPathJMI.addActionListener(this);
		randomlyTravelJMI.addActionListener(this);
		interfaceJMI.addActionListener(this);
		testJMI.addActionListener(this);
		
		label = new JLabel();
		getContentPane().add(label,  BorderLayout.NORTH);
	}
	
	File fileToOpen;
	ListDG graph;
	GraphComponents componentsToDraw;
	
	public void actionPerformed(ActionEvent e){
		JMenuItem source = (JMenuItem)(e.getSource());
		label.setText("the chosen menu item is "+source.getText());
		label.setHorizontalAlignment(JLabel.CENTER);
		String operation = source.getText();
		int temp = operation.length()-2;
		switch(operation.charAt(temp)){
		case 'I': importFile(); break;
		case 'G': generate(); break;
		case 'B': queryBridgeWords(); break;
		case 'N': formulateNewText(); break;
		case 'P': shortestPath(); break;
		case 'T': randomTravel(); break;
		case 'X': test(); break;
		}
	}

	public static void main(String[] args){
		JFrame frame = new MainPairPrograming();
		SetLookAndFeel.setNativeLookAndFeel();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(800, 500);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	
	public void importFile(){
		final JFileChooser fc = new JFileChooser();
		int returnValue = fc.showOpenDialog(MainPairPrograming.this);
		if (returnValue == JFileChooser.APPROVE_OPTION){
			fileToOpen = fc.getSelectedFile();
			label.setText("Opening File: " + fileToOpen.getName() + ".\n");
			}
		else {
			label.setText("Cancel opening operation.\n");
			fileToOpen = null;
		}
	}
	
	public void generate(){
		try {
			graph = new ListDG(fileToOpen);
			componentsToDraw = new GraphComponents(graph);
		}
		catch(FileNotFoundException e){
			if (fileToOpen.getName() == null)
				label.setText("The file cannot be null");
			else label.setText("File "+fileToOpen.getName()+" is not found.");
		}
	}

	JDialog bridgeWordsDialog;
	JLabel inputWord1, inputWord2;
	JTextField word1, word2;
	JLabel outputWords;
	JButton query;
	GridBagLayout gridbag;
	GridBagConstraints c;
	public void gridset(GridBagConstraints c, int gx, int gy, int gw, int gh, int ix, int iy, double wx, double wy
			){
		c.gridx = gx;
		c.gridy = gy;
		c.gridwidth = gw;
		c.gridheight = gh;
		c.ipadx = ix;
		c.ipady = iy;
		c.weightx = wx;
		c.weighty = wy;
	}
	public void queryBridgeWords(){
		bridgeWordsDialog = new JDialog(this, "Query Bridge Words");
		gridbag = new GridBagLayout();
		c = new GridBagConstraints();
		bridgeWordsDialog.setFont(new Font("SansSerif", Font.PLAIN, 14));
		bridgeWordsDialog.setLayout(gridbag);
		c.fill = GridBagConstraints.BOTH;
		inputWord1 = new JLabel("Input word 1:");
		inputWord2 = new JLabel("Input word 2:");
		word1 = new JTextField();
		word2 = new JTextField();
		query = new JButton("query");
		outputWords = new JLabel();
		
		query.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String wordString1 = new String(word1.getText());
				String wordString2 = new String(word2.getText());
				wordString1.toLowerCase();
				wordString2.toLowerCase();
				String[] result;
				//result = BridgeWords.getBridgeWords(wordString1, wordString2);
				if (result.length == 0){
					outputWords.setText("There are no bridge words between "+wordString1+" and "+wordString2+".");
				}
				else if (result.length == 1){
					outputWords.setText("The bridge word between "+wordString1+" and "+wordString2+" is "+result[0]+".");
				}
				else{
					String outputString = new String("The bridge words between "+wordString1+" and "+wordString2+" are ");
					for (int i = 0; i < result.length-1; i++) outputString+=result[i]+" ";
					outputString+=result[result.length-1]+".";
					outputWords.setText(outputString);
				}
			}
		});
		
		
		gridset(c,0,0,1,1,6,1,0,0);
		gridbag.setConstraints(inputWord1, c);
		bridgeWordsDialog.add(inputWord1);
		gridset(c,1,0,1,1,12,1,1,0);
		gridbag.setConstraints(word1, c);
		bridgeWordsDialog.add(word1);
		gridset(c,0,1,1,1,6,1,0,0);
		gridbag.setConstraints(inputWord2, c);
		bridgeWordsDialog.add(inputWord2);
		gridset(c,1,1,1,1,12,1,1,0);
		gridbag.setConstraints(word2, c);
		bridgeWordsDialog.add(word2);
		gridset(c,0,2,1,1,6,1,0,0);
		gridbag.setConstraints(query, c);
		bridgeWordsDialog.add(query);
		gridset(c,0,3,2,1,18,1,1,1);
		gridbag.setConstraints(outputWords, c);
		bridgeWordsDialog.add(outputWords);
	}
	
	
	JDialog newTextDialog;
	public void formulateNewText(){
		newTextDialog = new JDialog(this, "Formulate new text");
	}
	
	/*
	public void show(){
		graphDialog = new JDialog(this, "Graph");
		Graphics theGraph = new Graphics();
		
		char[] goingToDraw = new char[30];
		goingToDraw = "testing".toCharArray();
		theGraph.drawChars(goingToDraw, 10, 20, 10, 10);
		theGraph.setColor(Color.BLACK);
		goingToDraw = "I am testing you!".toCharArray();
		theGraph.drawChars(goingToDraw, 10, 20, 30, 30);
		theGraph.drawOval(100, 100, 30, 30);
		
	}
	*/
	/*
	public void save(File file){}
	public void close(ListDG g){}
	public String generateNewText(String inputText){}
	public String calShortestPath(String word1, String word2){}
	public void randomWalk(){}
	*/
	public void test(){
		fileToOpen = new File("E:\\Aaron\\软件工程\\实验\\1\\data.txt");
		generate();
	}
}
