package functionality;

import java.io.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import javax.print.Doc;
import javax.swing.JSpinner.ListEditor;

public class DocumentHandler {
	private List<Document> parsedDocuments;
	private List<Document> notParsedDocuments;
	private Hashtable<String, Term> index;
	private List<String> stopList;
	
	public DocumentHandler() {
		this.parsedDocuments = new ArrayList<Document>();
		this.notParsedDocuments = new ArrayList<Document>();
		this.index = new Hashtable<String, Term>();
		this.stopList = new ArrayList<String>();
		createStopList();
	}

	public void createStopList()
	{
		stopList.add("of");
		stopList.add("the");
		stopList.add("is");
		stopList.add("a");
		stopList.add("are");
		stopList.add("at");
		stopList.add("on");
	}
	public List<Document> getParsedDocuments() {
		return parsedDocuments;
	}
	public List<Document> getNotParsedDocuments() {
		return notParsedDocuments;
	}
	public Hashtable<String, Term> getIndex() {
		return index;
	}

	class appearancesCounter {
		String term;
		int appearances;
		
		public appearancesCounter(String term) {
			this.term = term;
			this.appearances = 1;
		}
		public String getTerm() {
			return term;
		}
		public void setTerm(String term) {
			this.term = term;
		}
		public int getAppearances() {
			return appearances;
		}
		public void addAppearances() {
			this.appearances = this.getAppearances() + 1;
		}
		@Override
		public String toString() {
			return "["+term+","+appearances+"]";
		}
	}
	
	public void parseDocument(Document doc)
	{
		if(doc.isHidden()) 
			cancelRemoveDocument(doc);
		else {
			String dest = new String();
			String source = new String();
	
			List<String> words = new ArrayList<String>();
		    List<appearancesCounter> wordsOrganized = new ArrayList<appearancesCounter>();
			FileReader fr = null;
			BufferedReader in = null;
			Scanner input;
		
			dest = "storage/" + doc.getTitle() +".txt";
			source = doc.getPath();
			
			try {
				fr = new FileReader(doc.getPath());
				in = new BufferedReader(fr);
				input = new Scanner(in);
				input.useDelimiter("\\s|!|\\?|,|\n|\r\n|\r|\\r|\\.|\\;");
				while (input.hasNext()) {
				      String word  = input.next();
				      word = word.toLowerCase();
			    	  if(word.contains("'"))
			    		  word = word.substring(0, word.indexOf("'"));
			    	  
				      if(!word.equals(null) && !word.equals("")) 
				    	  words.add(word);   
				}
				Collections.sort(words);
				wordsOrganized = countAppearances(words);
				for(appearancesCounter term : wordsOrganized)
					addToIndex(doc, term);
				doc.setRetrieved(true);
				this.getParsedDocuments().add(doc);
				this.getNotParsedDocuments().remove(doc);
				
				try {
					copy(source,dest);
				} catch (IOException e) {
					e.printStackTrace();
				}					
				
				File fileObject = new File(source);
				fileObject.delete();
				
				doc.setPath(dest);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			finally {
				if(in!=null) {
					try {
						in.close();
					}
					catch(IOException e) {
						e.printStackTrace();
					}
				}
				if(fr!=null) {
					try {
						fr.close();
					}
					catch(IOException e) {
						e.printStackTrace();
					}
				}
			}	
		}
	}
	
	public void parseList(List<Document> docList)
	{
		for(Document doc : docList)
			this.parseDocument(doc);
	}
	
	public List<appearancesCounter> countAppearances(List<String> wordsList) 
	{
	    List<appearancesCounter> words = new ArrayList<appearancesCounter>();
	    for(String word : wordsList)
			if(words.size()>0 && words.get(words.size()-1).getTerm().equals(word))
				words.get(words.size()-1).addAppearances();
			else
				words.add(new appearancesCounter(word));
		return words;
	}
	
	public void addToIndex(Document doc, appearancesCounter word) 
	{
		Posting pos = new Posting(doc.getDocID(), word.getAppearances(), doc);
		if(getIndex().containsKey(word.getTerm())==true) {
			getIndex().get(word.getTerm()).getPosting().add(pos);
			getIndex().get(word.getTerm()).addHit();
		}
		else
			getIndex().put(word.getTerm(), new Term(word.getTerm(), pos));
	}
	
	public void createDocumentCollection()
	{
		notParsedDocuments.add(new Document("Balade", "Geoffrey Chaucer", "love poems"));
		notParsedDocuments.add(new Document("A Death Scene", "Emily Brontë", "death poems"));
		notParsedDocuments.add(new Document("Jerusalem", "William Blake", "religious poems"));
		notParsedDocuments.add(new Document("Patroling Barnegat", "Walt Whitman", "poems"));
		notParsedDocuments.add(new Document("The Crocodile", "Lewis Carroll", "children poems"));
		notParsedDocuments.add(new Document("Humility", "Richard Brome", "loneliness poems"));
		notParsedDocuments.add(new Document("The Kaiser and God", "Barry Pain", "war poems"));
		notParsedDocuments.add(new Document("The cow", "Oliver Herford", "Humorous poems"));
	}
	
	public static void copy( String inputFile, String outputFile) throws IOException 
	{				
			FileInputStream input = null;
			FileOutputStream output = null;
			
			try {
				input = new FileInputStream(inputFile);
				output = new FileOutputStream(outputFile);
				IOUtils.streamCopy(input, output);
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			finally {
				if(input!=null) {
					try {
						input.close();
					}
					catch(IOException e) {
						e.printStackTrace();
					}
				}
				if(output!=null) {
					try {
						output.close();
					}
					catch(IOException e) {
						e.printStackTrace();
					}
				}
			}
	}
	
	public void removeDocument(Document docToRemove) 
	{
		docToRemove.setHidden(true);
		this.getParsedDocuments().remove(docToRemove);
		this.getNotParsedDocuments().add(docToRemove);
	}
	public void cancelRemoveDocument(Document docToRemove) 
	{
		docToRemove.setHidden(false);
		this.getParsedDocuments().add(docToRemove);
		this.getNotParsedDocuments().remove(docToRemove);
	}
	
	public boolean isInStopList(String word) 
	{
		for(String str : stopList)
			if(word.contentEquals(str))
				return true;
		return false;
	}
}