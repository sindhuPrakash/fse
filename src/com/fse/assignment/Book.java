package com.fse.assignment;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Book implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6212470156629515269L;

	private long bookId;

	private String title;

	private double price;

	private int volume;

	private Date publishDate;

	private Subject Subject;

	public Book() {
		super();
	}

	public Book(String title, double price, int volume, Date publishDate) {
		super();
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

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public Subject getSubject() {
		return Subject;
	}

	public void setSubject(Subject subject) {
		this.Subject = subject;
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
