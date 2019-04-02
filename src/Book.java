import java.io.Serializable;
import java.time.LocalDate;

public class Book implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6212470156629515269L;
	
	private long bookId;
	
	private String title;
	
	private double price;
	
	private int volume;
	
	private LocalDate publishDate;
	

	public Book() {
		super();
	}

	public Book(long bookId, String title, double price, int volume, LocalDate publishDate) {
		super();
		this.bookId = bookId;
		this.title = title;
		this.price = price;
		this.volume = volume;
		this.publishDate = publishDate;
	}

	public long getBookId() {
		return bookId;
	}

	public void setBookId(long bookId) {
		this.bookId = bookId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public LocalDate getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(LocalDate publishDate) {
		this.publishDate = publishDate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Books").append("\n");
		builder.append("Book Id: "+bookId).append("\n");
		builder.append("Book Title: "+title).append("\n");
		builder.append("Book Volume: "+volume).append("\n");
		builder.append("Book Published Date: "+publishDate).append("\n");
		builder.append("Book Price: "+price).append("\n");
		return builder.toString();
	}
	
}
