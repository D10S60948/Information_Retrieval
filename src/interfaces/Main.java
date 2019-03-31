package interfaces;

import functionality.DocumentHandler;

public class Main 
{
	public static void main(String[] args) 
	{
		UserInterface ui = new UserInterface();
		DocumentHandler dh = new DocumentHandler();
		
		ui.activateListeners(dh);
		
		dh.createDocumentCollection();

		dh.parseDocument(dh.getNotParsedDocuments().get(0));
		dh.parseDocument(dh.getNotParsedDocuments().get(1));
		dh.parseDocument(dh.getNotParsedDocuments().get(2));
	}
}
