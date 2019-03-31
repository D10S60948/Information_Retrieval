package functionality;

public class Posting {
	private int docNumber;
	private int hitsInDocument;
	private Document documentReference;

	
	public Posting(int docNumber, int hitsInDocument, Document documentReference) {
		this.docNumber = docNumber;
		this.hitsInDocument = hitsInDocument;
		this.documentReference = documentReference;
	}

	public int getDocNumber() {
		return docNumber;
	}
	public int getHitsInDocument() {
		return hitsInDocument;
	}
	public Document getDocumentReference() {
		return documentReference;
	}		
}