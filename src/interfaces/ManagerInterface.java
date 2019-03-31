package interfaces;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import functionality.Document;
import functionality.DocumentHandler;
import functionality.Term;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ManagerInterface
{
	private JFrame frame;
    private JPanel panelTop,panelBottom, panelCenter, textArea;
	private JButton user, manager, close, showAllDocs, showNotParsed, showParsed, parseAll, showIndex;
	private JScrollPane scroll; 
	private boolean isParseAllButtonAdd;
	
	public ManagerInterface()
	{
		textArea = new JPanel();
		showAllDocs = new JButton("Show all");
		showNotParsed = new JButton("Not parsed"); 
		showParsed = new JButton("Parsed");
		showIndex = new JButton("Show index");
		user = new JButton("User mode"); 
		manager = new JButton("Manager mode");
		parseAll = new JButton("Parse all");
		close = new JButton("X");
		frame = new JFrame("Inforation retrieval - manager");
		scroll = new JScrollPane(textArea);
		isParseAllButtonAdd = false;
		
        frame.setLayout(new BorderLayout());

		panelTop = new JPanel();	
		panelBottom = new JPanel();
		panelCenter = new JPanel();
		
		panelTop.setBackground(new Color(64,116,177));
		panelTop.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.black));
		panelTop.setPreferredSize(new Dimension(1600, 220));
		panelCenter.setBackground(Color.GRAY);
		panelBottom.setBackground(Color.BLACK);

        Font font = new Font(null, Font.BOLD, 17);

        panelTop.setLayout(null);
        panelCenter.setLayout(null);
        
        panelTop.add(close);
        panelTop.add(showAllDocs);
        panelTop.add(showNotParsed);
        panelTop.add(showParsed);
        panelTop.add(parseAll);
        panelTop.add(showIndex);
        panelCenter.add(scroll);
        panelBottom.add(user);
        panelBottom.add(manager);
        
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(0, 0, 1365, 510);
		textArea.setLayout(new BoxLayout(textArea, BoxLayout.Y_AXIS));
		close.setBounds(1310,10,50,40);
        close.setForeground(Color.black); 
        showAllDocs.setBounds(100, 80, 200, 35);
        showNotParsed.setBounds(420, 80, 200, 35);
    	parseAll.setBounds(420, 140, 200, 35);
    	showParsed.setBounds(740, 80, 200, 35);
    	parseAll.setBackground(Color.red);
    	parseAll.setEnabled(false);
    	showIndex.setBounds(1060, 80, 200, 35);
    	
        frame.add(panelTop,BorderLayout.NORTH);
        frame.add(panelBottom,BorderLayout.SOUTH);
        frame.add(panelCenter,BorderLayout.CENTER);
        
	    frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
	    frame.setUndecorated(true);
        frame.setVisible(true);
	}
	
	void activateListeners(DocumentHandler dh)
	{
		user.addActionListener(new ActionListener() 
		{
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		    	UserInterface ui = new UserInterface();
		    	ui.activateListeners(dh);
		    }
		});
		
		close.addActionListener(new ActionListener() 
		{
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		    	System.exit(0);
		    }
		});
		
		showAllDocs.addActionListener(new ActionListener() 
		{
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	if(isParseAllButtonAdd==true) 
		    		disableParseAllBouttun();
		    	
		    	clearMainPanel();
		    	
		    	for(Document doc : dh.getNotParsedDocuments())
		    		showDocument(doc,dh);
		    	for(Document doc : dh.getParsedDocuments())
		    		showDocument(doc,dh);
		    }
		});
		
		showNotParsed.addActionListener(new ActionListener() 
		{
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	if(isParseAllButtonAdd==false) 
		    		enableParseAllBouttun();
		    	
		    	clearMainPanel();
		    	
		    	for(Document doc : dh.getNotParsedDocuments())
		    		showDocument(doc, dh);
		    }
		});
		
		showParsed.addActionListener(new ActionListener() 
		{
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	if(isParseAllButtonAdd==true) 
		    		disableParseAllBouttun();
		    	
		    	clearMainPanel();
		    	
		    	for(Document doc : dh.getParsedDocuments())
		    		showDocument(doc, dh);
		    }
		});
		
		parseAll.addActionListener(new ActionListener() 
		{
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	List<Document> temporaryList = new ArrayList<Document>(dh.getNotParsedDocuments());
		    	dh.parseList(temporaryList);
		    	clearMainPanel();
		    }
		});
		
		showIndex.addActionListener(new ActionListener() 
		{
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	if(isParseAllButtonAdd==true) 
		    		disableParseAllBouttun();
		    	
		    	clearMainPanel();
		    	
		    	showIndex(dh);
		    }
		});
	}
	
	protected void addStylesToDocument(StyledDocument doc) {
        Style def = StyleContext.getDefaultStyleContext().
                        getStyle(StyleContext.DEFAULT_STYLE);
 
        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");
        
        Style s = doc.addStyle("bold & large", regular);
        StyleConstants.setBold(s, true);
        StyleConstants.setFontSize(s, 16);
        StyleConstants.setUnderline(s, true);

        s = doc.addStyle("italic", regular);
        StyleConstants.setItalic(s, true);
        
        s = doc.addStyle("bold", regular);
        StyleConstants.setBold(s, true);
        
        s = doc.addStyle("large", regular);
        StyleConstants.setFontSize(s, 16);
    }
    
	public void showDocument(Document doc, DocumentHandler dh) 
	{
	    JTextPane textPane = new JTextPane();
	    JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    JButton removeButton = null, addButton = null;
	    
	    String first3Lines = new String();
		String line = null;

	    BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(doc.getPath()));
			for(int j=0; j<3; ++j)  {
				line = br.readLine();
				first3Lines += line;
				if(j==2)
					first3Lines += "\"";
				else
					first3Lines += "\n";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String[] initString =
            { 	"\"" + doc.getTitle() + "\"",
            	"\n\n\"" + first3Lines,
            	"\n\nAuthor: ", doc.getAuthor(),
            	"  |  Category: ", doc.getCategory(),
            	" | Document ID: ", Integer.toString(doc.getDocID())
             };

	    String[] initStyles =
	            { "bold & large", 
            		"italic",
            		"bold", "regular",
            		"bold", "regular",
            		"bold", "regular"
	            };
	    
	    StyledDocument doc2 = textPane.getStyledDocument();
	    addStylesToDocument(doc2);
	
	    try {
	        for (int i=0; i < initString.length; i++) {
	            doc2.insertString(doc2.getLength(), initString[i],
	                             doc2.getStyle(initStyles[i]));
	        }
	    } catch (BadLocationException ble) {
	        System.err.println("Couldn't insert initial text into text pane.");
	    }
	    
	    wrapper.add(textPane);
	    wrapper.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	    wrapper.setBackground(Color.white);
	    textPane.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
		
	    if(doc.isRetrieved()==true && doc.isHidden()==false) {
	    	removeButton = new JButton("Remove file");
	    	wrapper.add(new JLabel("Is parsed"));
		    wrapper.add(removeButton);
	    }
	    else {
	    	addButton = new JButton("Add file");
	    	wrapper.add(new JLabel("Is not parsed"));
		    wrapper.add(addButton);
	    }
	    
	    textArea.add(wrapper);

		if(removeButton!=null)
			removeButton.addActionListener(new ActionListener() {
			    @Override
			    public void actionPerformed(ActionEvent e) {
			    	dh.removeDocument(doc);
			    }
		    });
		if(addButton!=null)
			addButton.addActionListener(new ActionListener() {
			    @Override
			    public void actionPerformed(ActionEvent e) {
			    	dh.parseDocument(doc);
			    	wrapper.add(new JLabel("DONE!"));
			    }
		    });	
	}

	public void showIndex(DocumentHandler dh)
	{
		Set<String> keys = dh.getIndex().keySet();
        for(String key: keys)
        	showTerm(dh.getIndex().get(key));		
	}
	
	public void showTerm(Term term)  
	{
		JTextPane textPane = new JTextPane();
	    JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));

		String[] initString =
            { 	"\"" + term.getText() + "\"",
            	"\n\nNumber of documents the term appears in: ", Integer.toString(term.getHits())
             };

	    String[] initStyles =
	            { "bold & large", 
	            	"bold", "regular"
	            };
	    
	    StyledDocument doc2 = textPane.getStyledDocument();
	    addStylesToDocument(doc2);
	
	    try {
	        for (int i=0; i < initString.length; i++) {
	            doc2.insertString(doc2.getLength(), initString[i],
	                             doc2.getStyle(initStyles[i]));
	        }
	    } catch (BadLocationException ble) {
	        System.err.println("Couldn't insert initial text into text pane.");
	    }
	    
	    wrapper.add(textPane);
	    wrapper.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	    wrapper.setBackground(Color.white);
	    textPane.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
	    
	    textArea.add(wrapper);
	}
	
	public void clearMainPanel()
	{
    	textArea.removeAll();
    	textArea.revalidate();
    	textArea.repaint();
	}

	public void enableParseAllBouttun()
	{
		parseAll.setBackground(Color.green);
		parseAll.setEnabled(true);
		isParseAllButtonAdd = true;
	}
	public void disableParseAllBouttun()
	{
		parseAll.setBackground(Color.RED);
		parseAll.setEnabled(false);
		isParseAllButtonAdd=false;
	}
}