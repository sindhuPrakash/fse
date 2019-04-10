import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "Book", uniqueConstraints = { @UniqueConstraint(columnNames = "bookId") })
public class Book implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6212470156629515269L;

	@Id
	@Column(name = "bookId", unique = true, nullable = false)
	private long bookId;

	@Column(name = "title")
	private String title;

	@Column(name = "price")
	private double price;

	@Column(name = "volume")
	private int volume;

	@Column(name = "publishDate")
	private LocalDate publishDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subjectId", nullable = false)
	private Subject subject;

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

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Books").append("\n");
		builder.append("Book Id: " + bookId).append("\n");
		builder.append("Book Title: " + title).append("\n");
		builder.append("Book Volume: " + volume).append("\n");
		builder.append("Book Published Date: " + publishDate).append("\n");
		builder.append("Book Price: " + price).append("\n");
		return builder.toString();
	}

}
