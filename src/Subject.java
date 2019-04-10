import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "Subject", uniqueConstraints = { @UniqueConstraint(columnNames = "subjectId") })
public class Subject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9001386160170483564L;

	@Id
	@Column(name = "subjectId", unique = true, nullable = false)
	private long subjectId;

	@Column(name = "subtitle")
	private String subtitle;

	@Column(name = "durationInHours")
	private int durationInHours;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "Subject")
	private Set<Book> references;

	public Subject() {
		super();
	}

	public Subject(long subjectId, String subtitle, int durationInHours, Set<Book> references) {
		super();
		this.subjectId = subjectId;
		this.subtitle = subtitle;
		this.durationInHours = durationInHours;
		this.references = references;
	}

	public long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public int getDurationInHours() {
		return durationInHours;
	}

	public void setDurationInHours(int durationInHours) {
		this.durationInHours = durationInHours;
	}

	public Set<Book> getReferences() {
		return references;
	}

	public void setReferences(Set<Book> references) {
		this.references = references;
	}

	@Override
	public String toString() {
		String books = this.getBooks();
		StringBuilder builder = new StringBuilder();
		builder.append("\nSubject:\n").append("subjectId: " + subjectId).append("\n")
				.append("Subject Title: " + subtitle).append("\n")
				.append("Subject Duration In Hours: " + durationInHours);
		builder.append("\n").append("References: \n").append(books);
		return builder.toString();
	}

	private String getBooks() {
		StringBuilder builder = new StringBuilder();
		builder.append("Books:").append("\n");
		references.forEach(book -> {
			builder.append("Book Id: " + book.getBookId()).append("\n");
			builder.append("Book Title: " + book.getTitle()).append("\n");
			builder.append("Book Volume: " + book.getVolume()).append("\n");
			builder.append("Book Published Date: " + book.getPublishDate()).append("\n");
			builder.append("Book Price: " + book.getPrice()).append("\n");
		});
		return builder.toString();
	}

}
