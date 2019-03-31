package interfaces;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import functionality.Document;
import functionality.DocumentHandler;
import functionality.Posting;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class UserInterface
{
	private JFrame frame;
    private JPanel panelTop,panelBottom, panelCenter, retrievedDocuments;
	private JTextPane contentArea;
	private JTextField writingArea;
	private JButton search, user, manager, close;
	private JScrollPane contentScroll, retrievedDocsScroll; 
	private StyledDocument styledDoc;
	private String searchWord;
	
	public UserInterface()
	{
		frame = new JFrame("Inforation retrieval - user");

		panelTop = new JPanel();	
		panelBottom = new JPanel();
		panelCenter = new JPanel();
		
		searchWord = new String();
 		close = new JButton("X");
		writingArea = new JTextField(50);
		search = new JButton("Search");
		frame.getRootPane().setDefaultButton(search);
		user = new JButton("User mode"); 
		manager = new JButton("Manager mode");
		contentArea = new JTextPane();
        styledDoc = (StyledDocument) contentArea.getDocument();
		contentArea.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
		contentArea.setFont(new Font(null, Font.PLAIN, 15));
		contentScroll = new JScrollPane(contentArea);
		retrievedDocuments = new JPanel();
		retrievedDocuments.setLayout(new BoxLayout(retrievedDocuments, BoxLayout.Y_AXIS));
		retrievedDocsScroll = new JScrollPane(retrievedDocuments);
		
        panelTop.setLayout(null);
		panelTop.setBackground(new Color(64,116,177));
		panelTop.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.black));
		panelTop.setPreferredSize(new Dimension(1600, 100));
		panelCenter.setBackground(Color.GRAY);
		panelBottom.setBackground(Color.BLACK);

        panelTop.add(writingArea);
        panelTop.add(search);
        panelTop.add(close);
		close.setBounds(1310,10,50,40);
        close.setForeground(Color.black); 
        writingArea.setBounds(180, 35, 800, 30);
        search.setBounds(990, 35, 150, 30);
        
        panelCenter.setLayout(null);
        panelCenter.add(retrievedDocsScroll);
        panelCenter.add(contentScroll);
        retrievedDocsScroll.setBounds(0, 0, 700, 630);
        contentScroll.setBounds(700, 0, 670, 630);
        
        panelBottom.add(user);
        panelBottom.add(manager);
		
        contentScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        retrievedDocsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        frame.setLayout(new BorderLayout());

        frame.add(panelTop,BorderLayout.NORTH);
        frame.add(panelBottom,BorderLayout.SOUTH);
        frame.add(panelCenter,BorderLayout.CENTER);
        
	    frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
	    frame.setUndecorated(true);

        frame.setVisible(true);
	}
	
	/**
	
	 * GETTERS
	 * @return
	 */
	public JButton getSearch() {
		return search;
	}
	public JTextField getWritingArea() {
		return writingArea;
	}	
	public JPanel getRetrievedDocuments() {
		return retrievedDocuments;
	}
	
	/**
	 * LISTENERS
	 * @param dh
	 */
	public void activateListeners(DocumentHandler dh)
	{
		manager.addActionListener(new ActionListener() 
		{
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	frame.remove(panelBottom);
		    	frame.remove(panelCenter);
		    	frame.remove(panelTop);
		    	
		    	JPanel passwordRequest = new JPanel();
		    	frame.add(passwordRequest);
		    	
		    	frame.revalidate();
	    		frame.repaint();
	    		
		    	frame.setComponentZOrder(passwordRequest,0);
		    	
		    	passwordRequest.setLayout(null);
		    	passwordRequest.setBackground(Color.ORANGE);
		    	passwordRequest.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));
		    	
		    	JLabel passwordLabel = new JLabel("password:");
		    	JButton submitPassword = new JButton("Go"), returnToUser = new JButton("< back");
		    	JTextField passwordField = new JTextField();
		    	
		    	passwordRequest.add(passwordLabel);
		    	passwordRequest.add(passwordField);
		    	passwordRequest.add(submitPassword);
		    	passwordRequest.add(returnToUser);
		    	
		    	returnToUser.setBounds(10, 10, 80, 40);
		    	passwordLabel.setBounds(405, 230, 100, 30);
		    	passwordLabel.setFont(new Font(null, Font.BOLD, 20));
		    	passwordField.setBounds(510, 230, 195, 35);
		    	submitPassword.setBounds(765, 230, 100, 35);
		    	
		    	submitPassword.addActionListener(new ActionListener() 
				{
				    @Override
				    public void actionPerformed(ActionEvent e) {
				    	if(passwordField.getText().contentEquals("12345")) {
					    	frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
					    	ManagerInterface mi = new ManagerInterface();
					    	mi.activateListeners(dh);
				    	}
				    	else {
				    		JLabel incorrectPass = new JLabel("Password is incorrect");
				    		passwordField.setText("");
				    		passwordRequest.add(incorrectPass);
				    		incorrectPass.setBounds(515, 310, 200, 30);
				    		incorrectPass.setFont(new Font(null,Font.PLAIN,15));
				    	}
				    		
				    }
				});
		    	
		    	returnToUser.addActionListener(new ActionListener() 
				{
				    @Override
				    public void actionPerformed(ActionEvent e) {
				    	passwordRequest.removeAll();
				    	frame.remove(passwordRequest);
				    	frame.add(panelBottom);
				    	frame.add(panelCenter);
				    	frame.add(panelTop);
				    	frame.revalidate();
			    		frame.repaint();
			    		frame.getRootPane().setDefaultButton(search);
			    		frame.setComponentZOrder(passwordRequest,1);
			    	}
				});
				    
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
		
		search.addActionListener(new ActionListener() 
		{
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	ArrayList<Document> listOfRelevantDocs = new ArrayList<Document>();
		    	presentFindings(handleSentence(dh, listOfRelevantDocs, getWritingArea().getText().toLowerCase()));
			}
		});
	}
	
	public void presentFindings(ArrayList<Document> listOfRelevantDocs) 
	{
		ArrayList<Document> tempList = new ArrayList<Document>(listOfRelevantDocs);
		
		retrievedDocuments.removeAll();
    	retrievedDocuments.revalidate();
    	retrievedDocuments.repaint();
    	writingArea.setText("");
    	
    	for(Document d : tempList)
			if(d.isHidden()==true)
				listOfRelevantDocs.remove(d);
		if(listOfRelevantDocs.isEmpty()==false ) 
			for(Document d : listOfRelevantDocs)
				showDocument(d);
		else {
			JLabel label = new JLabel("Term does not appear in index.");
			retrievedDocuments.add(label);
			label.setFont(new Font(null, Font.BOLD, 17));
	      }
	}
	
	public ArrayList<Document> handleSentence(DocumentHandler dh, ArrayList<Document> relevantDocs, String searchText) 
	{
    	Boolean isAnd = false, isOr = false, isNot = false;
		Scanner input = new Scanner(new String(searchText));
		input.useDelimiter("\\s|\\(|\\)");
		
		while (input.hasNext()) {
		      String word  = input.next();
		      if(word.contains("(")) word = word.replaceAll("\\(","");
		      if(word.contains(")")) word = word.replaceAll("\\)","");
		      
		      if(word.contentEquals("and"))
		    	  isAnd=true;
		      else if(word.contentEquals("or"))
		    	  isOr=true;
		      else if(word.contentEquals("not"))
		    	  isNot=true;
		      else if((dh.getIndex().containsKey(word) || dh.getIndex().containsKey(word.replaceAll("\"",""))) && dh.isInStopList(word)==false) {
			      if(word.contains("\"")) word = word.replaceAll("\"","");
			      searchWord = word;
		    	  if(isAnd) {
		    		  relevantDocs = handleAndStatement(dh, relevantDocs, word);
		    		  isAnd=false;
		    	  }
		    	  else if(isOr) {
		    		  relevantDocs = handleOrStatement(dh, relevantDocs, word);
		    		  isOr=false;
		    	  }
		    	  else if(isNot) {
		    		  relevantDocs = handleNotStatement(dh, relevantDocs, word);
		    		  isNot=false;
		    	  }
		    	  else 
		    		  relevantDocs = getRelevantDocuments(dh, word);
		      } 
		      else if(isAnd) {
		    	  relevantDocs.clear();
		    	  isAnd=false;
		      }
		}
    	if(input!=null) 
			input.close();
		return relevantDocs;
	}
	
	public ArrayList<Document> getRelevantDocuments(DocumentHandler dh, String wordToSearch) 
	{
		ArrayList<Document> newList = new ArrayList<Document>();
		
		LinkedList<Posting> postingFile = dh.getIndex().get(wordToSearch).getPosting();
		for(Posting pos : postingFile.toArray(new Posting[postingFile.size()])) 
			newList.add(pos.getDocumentReference());
		
		return newList;
	}
	
	public ArrayList<Document> handleAndStatement(DocumentHandler dh, ArrayList<Document> relevantDocs, String newWord) 
	{
		ArrayList<Document> newList = new ArrayList<Document>();
		LinkedList<Posting> postingFile = dh.getIndex().get(newWord).getPosting();
		for(Posting pos : postingFile.toArray(new Posting[postingFile.size()])) {
			Document doc = pos.getDocumentReference();
			if(relevantDocs.contains(doc))
				newList.add(doc);
		}
		return newList;
	}
	public ArrayList<Document> handleOrStatement(DocumentHandler dh, ArrayList<Document> relevantDocs, String newWord) 
	{
		LinkedList<Posting> postingFile = dh.getIndex().get(newWord).getPosting();
		for(Posting pos : postingFile.toArray(new Posting[postingFile.size()])) {
			Document doc = pos.getDocumentReference();
			if(relevantDocs.contains(doc)==false)
				relevantDocs.add(doc);
		}
		return relevantDocs;
	}
	public ArrayList<Document> handleNotStatement(DocumentHandler dh, ArrayList<Document> relevantDocs, String newWord) 
	{
		LinkedList<Posting> postingFile = dh.getIndex().get(newWord).getPosting();
		for(Posting pos : postingFile.toArray(new Posting[postingFile.size()])) {
			Document doc = pos.getDocumentReference();
			if(relevantDocs.contains(doc))
				relevantDocs.remove(doc);
		}
		return relevantDocs;
	}
	
	public void addStylesToDocument(StyledDocument doc) {
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
    
	public void showDocument(Document doc) 
	{
		JPanel wrapper = new JPanel();
	    JTextPane textPane = new JTextPane();
	    JButton showContentButton = new JButton("show content");
	    JButton printFileButton = new JButton("print file");
	    
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
            	"  |  Category: ", doc.getCategory()
             };

	    String[] initStyles =
	            { "bold & large", 
            		"italic",
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
	    
	    wrapper.setLayout(null);
	    wrapper.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	    wrapper.setPreferredSize(new Dimension(680,162));
	    textPane.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
		
	    wrapper.add(textPane);
	    wrapper.add(showContentButton);
	    wrapper.add(printFileButton);

		textPane.setBounds(1, 1, 350, 160);
	    showContentButton.setBounds(380,62, 120, 35);
		printFileButton.setBounds(520,62, 120, 35);
		
		retrievedDocuments.add(wrapper);
		
		showContentButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	contentArea.setText("");
		    	showDocumentContent(doc);
		    }
	    });
		printFileButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	contentArea.setText("\"" + doc.getTitle() + "\""  + " was sent to printer.");
		    	System.out.println("\"" + doc.getTitle() + "\""  + " was sent to printer.");
		    }
	    });	
		

	}
	
	void showDocumentContent(Document doc) {
		BufferedReader br = null;
		try {	
			br = new BufferedReader(new FileReader(doc.getPath()));
			String line = null;

			SimpleAttributeSet normal = new SimpleAttributeSet();
	        StyleConstants.setFontFamily(normal, "SansSerif");
	        StyleConstants.setFontSize(normal, 16);
	        
	        SimpleAttributeSet bold = new SimpleAttributeSet(normal);
	        StyleConstants.setBold(bold, true);
	        StyleConstants.setForeground(bold, Color.red);
	        
			while ((line = br.readLine()) != null) {
					if(line.contains(" "+searchWord+" ")) {
						styledDoc.insertString(styledDoc.getLength(), line.substring(0, line.indexOf(searchWord)) , normal);
						styledDoc.insertString(styledDoc.getLength(), searchWord, bold);
						styledDoc.insertString(styledDoc.getLength(), line.substring(line.indexOf(searchWord)+searchWord.length(), line.length()) + "\n", normal);
					}
					else
						styledDoc.insertString(styledDoc.getLength(), line + "\n", normal);
			}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (BadLocationException e) {
				e.printStackTrace();
			} 
			finally {
				if(br!=null) {
					try {
						br.close();
					}
					catch(IOException e) {
						e.printStackTrace();
					}
			}
		}
	}

}