package functionality;

import java.util.LinkedList;

public class Term {
	private String text;
	private int documentHits;		// Number of documents term appears in
	private LinkedList<Posting> posting;
	
	public Term(String text, Posting posting) {
		this.text = new String(text);
		this.posting = new LinkedList<Posting>();
		this.posting.addFirst(posting); 
		this.documentHits = 1;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getHits() {
		return documentHits;
	}

	public void addHit() {
		this.documentHits++;
	}
	public void removeHit() {
		this.documentHits--;
	}
	
	
	public LinkedList<Posting> getPosting() {
		return posting;
	}

	public void showDocumentsContainTerm()	{
		for(Posting post : getPosting()) {
			System.out.println(post.getDocumentReference());
			System.out .println("Term appreaerrances: " + post.getHitsInDocument() + "\n\n");
		}
	}
	
	@Override
	public String toString() {
		return getText() + " | documentHits: " + getHits();
	}
}
