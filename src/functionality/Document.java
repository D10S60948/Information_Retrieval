package functionality;

public class Document {
	private String title;
	private int docID;
	private String path;
	private String author;
	private String category;
	private boolean retrieved;
	private boolean isHidden;

	private static int docsID = 0;

	// CONSTRUCTOR
	public Document(String title, String author, String category) {
		this.title = title;
		this.path = ("source/" + title +".txt");
		this.author = author;
		this.category = category;
		this.retrieved = false;
		setDocID(-1);
		this.isHidden = false;
	}
	
	//	GETTERS
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getDocID() {
		return docID;
	}
	public boolean isHidden() {
		return isHidden;
	}
	
	// SETTERS
	public void setDocID(int docID) {
		this.docID = docID;
	}
	public void setRetrieved(boolean retrieved) {
		if(retrieved==true)
			this.setDocID(++docsID);
		else
			this.setDocID(-1);
		this.retrieved = retrieved;
	}
	public boolean isRetrieved() {
		return retrieved;
	}
	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}
		
	@Override
	public String toString() {
		return "Title: " + getTitle() + "\nAuthor: "+ getAuthor() + " \nCategory: " + getCategory();
	}
}