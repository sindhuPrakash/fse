import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class menu {

	private static Scanner scanner = new Scanner(System.in);
	/*private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/library";*/

	public static void main(String[] args) throws ClassNotFoundException {
		boolean exitstatus = false;
		while (!exitstatus) {
			System.out.println(
					"Menu \n1.Add a Subject. \n2.Add a Book.\n3.Delete a Subject\n4.Delete a book\n5.Search for a Book\n6.Search for a subject.\n7.Sort Book By Title. \n8.Sort Subject by Subject Title.\n 9.Sort Books.\n10.Exit");
			switch (scanner.nextLine()) {
			case "1": {
				addSubject();
				break;
			}
			case "2": {
				addBook();
				break;
			}
			case "3": {
				deleteSubject();
				break;
			}
			case "4": {
				deleteBook();
				break;
			}
			case "5": {
				searchBook();
				break;
			}
			case "6": {
				searchSubject();
				break;
			}
			case "7": {
				sortBookByTitle();
				break;
			}
			case "8": {
				sortSubjectByTitle();
				break;
			}
			case "9": {
				sortBookByDate();
				break;
			}
			case "10": {
				exitstatus = true;
				break;
			}
			default: {
				System.out.println(getSubjects().toString());
			}
			}
		}
	}

	private static void sortBookByDate() {
		getBooks().stream().sorted((Book b1, Book b2) -> b1.getPublishDate().compareTo(b2.getPublishDate()))
				.forEach(System.out::println);
	}

	private static void sortSubjectByTitle() {
		getSubjects().stream().sorted((Subject s1, Subject s2) -> s1.getSubtitle().compareTo(s2.getSubtitle()))
				.forEach(System.out::println);
	}

	private static void sortBookByTitle() {
		getBooks().stream().sorted((Book b1, Book b2) -> b1.getTitle().compareTo(b2.getTitle()))
				.forEach(System.out::println);
	}

	private static void searchSubject() {
		System.out.println("Search Subject \n1.Search with Subject Id.\n2.Search with Subject Title.");
		switch (scanner.nextLine()) {
		case "1": {
			System.out.println("Enter the subject Id");
			long subjectId = Long.parseLong(scanner.nextLine());
			Subject filteredSubject = getSubject(subjectId);
			System.out.println(filteredSubject.toString());
			break;
		}
		case "2": {
			System.out.println("Enter the subject Title");
			String subjectName = scanner.nextLine();
			//List<Subject> filteredSubject = searchSubjectWithSubjectTitle(subjectName);
			//filteredSubject.forEach(subject -> System.out.println(subject.toString()));
			break;
		}
		default:
			System.out.println("Invalid selection");
			break;
		}
	}

	private static void searchBook() {
		System.out.println("Search Book\n1.Search by Book Id.\n2.Search by Book title");
		switch (scanner.nextLine()) {
		case "1": {
			System.out.println("Enter Book Id:");
			long bookId = Long.parseLong(scanner.nextLine());
			//Book filteredBook = getBook(bookId);
			//System.out.println(filteredBook.toString());
			break;
		}
		case "2": {
			System.out.println("Enter Book Title:");
			String bookTitle = scanner.nextLine();
			//List<Book> filteredBooks = searchBookWithBookTitle(bookTitle);
			//filteredBooks.forEach(book -> System.out.println(book.toString()));
			break;
		}

		default:
			System.out.println("Invalid selection");
			break;
		}
	}

	private static void deleteBook() {
		try {
			System.out.println("Enter book Id");
			long bookId = Long.parseLong(scanner.nextLine());
			if (getBook(bookId) != null) {
				if (deleteBookFromDB(bookId)) {
					System.out.println("book deleted successfully");
				} else {
					System.out.println("failed to delete book");
				}
			} else {
				System.out.println("no book found for the entered book id");
			}
		} catch (NumberFormatException e) {
			System.out.println("Exception occurred while deleting Book: " + e.getMessage());
		}
	}

	private static boolean deleteBookFromDB(long bookId) {
		boolean status = false;
		Session session;
		Transaction transaction = null;
		try (SessionFactory sessionFactory = HibernateUtil.getSessionFactory();) {
			session = sessionFactory.getCurrentSession();
			transaction = session.beginTransaction();
			transaction.begin();
			session.delete(getBook(bookId));
			transaction.commit();
			if (getBook(bookId) == null) {
				status = true;
			}
		} catch (Exception e) {
			System.out.println("Exception occurred while saving subject to DB: " + e.getMessage());
			transaction.rollback();
		}
		return status;
	}

	private static Object getBook(long bookId) {
		Book book = null;
		try (SessionFactory sessionFactory = HibernateUtil.getSessionFactory();) {
			Session session = sessionFactory.getCurrentSession();
			book = session.load(Book.class, bookId);
		} catch (Exception e) {
			System.out.println("Exception occurred while saving subject to DB: " + e.getMessage());
		}
		return book;
	}

	private static void deleteSubject() {
		try {
			System.out.println("Enter the subject Id");
			long subjectId = Long.parseLong(scanner.nextLine());
			if (getSubject(subjectId) != null) {
				if (deleteSubjectFromDB(subjectId)) {
					System.out.println("successfully deleted subject and it's books");
				} else {
					System.out.println("failed to delete subject");
				}
			} else {
				System.out.println("No records found First add subject then add book");
			}
		} catch (NumberFormatException e) {
			System.out.println("Exception occurred while deleting subject: " + e.getMessage());
		}
	}

	private static boolean deleteSubjectFromDB(long subjectId) {
		boolean status = false;
		Session session;
		Transaction transaction = null;
		try (SessionFactory sessionFactory = HibernateUtil.getSessionFactory();) {
			session = sessionFactory.getCurrentSession();
			transaction = session.beginTransaction();
			transaction.begin();
			session.delete(getSubject(subjectId));
			transaction.commit();
			if (getSubject(subjectId) == null) {
				status = true;
			}
		} catch (Exception e) {
			System.out.println("Exception occurred while saving subject to DB: " + e.getMessage());
			transaction.rollback();
		}
		return status;
	}

	private static void addSubject() throws ClassNotFoundException {
		try {
			System.out.println("Enter subject title");
			String subjectName = scanner.nextLine();
			System.out.println("Enter subject Id");
			long subjectId = Long.parseLong(scanner.nextLine());
			System.out.println("Enter Duration");
			int duration = Integer.parseInt(scanner.nextLine());
			System.out.println("Do you want to add Book? Y/N");
			if (scanner.nextLine().equalsIgnoreCase("Y")) {
				System.out.println("Enter book title");
				String bookName = scanner.nextLine();
				System.out.println("Enter book Id");
				long bookId = Long.parseLong(scanner.nextLine());
				System.out.println("Enter book price");
				double price = Double.parseDouble(scanner.nextLine());
				System.out.println("Enter book volume");
				int volume = Integer.parseInt(scanner.nextLine());
				System.out.println("Enter book publish date in MM/dd/yyyy format");
				String date = scanner.nextLine();
				LocalDate bookPublishDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
				Subject subject = new Subject(subjectId, subjectName, duration, new HashSet<>());
				Book book = new Book(bookId, bookName, price, volume, bookPublishDate);
				book.setSubject(subject);
				subject.getReferences().add(book);
				System.out.println(saveSubjectToDB(subject) ? "Book Added to Subject Successfully"
						: "failed to add book to subject");
			}
		} catch (NumberFormatException e1) {
			System.out.println("Failed to add subject Try Again");
		}
	}

	private static boolean saveSubjectToDB(Subject subject) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = session.beginTransaction();
		boolean status = false;
		try {
			transaction.begin();
			session.save(subject);
			transaction.commit();
			status = true;
		} catch (Exception e) {
			System.out.println("Exception occurred while saving subject");
			transaction.rollback();
		}
		return status;
	}

	private static void addBook() {
		try {
			System.out.println("Enter the subject Id of the book");
			long subjectId = Long.parseLong(scanner.nextLine());
			Subject subject = getSubject(subjectId);
			if (subject != null) {
				System.out.println("Enter book title");
				String bookName = scanner.nextLine();
				System.out.println("Enter book Id");
				long bookId = Long.parseLong(scanner.nextLine());
				System.out.println("Enter book price");
				double price = Double.parseDouble(scanner.nextLine());
				System.out.println("Enter book volume");
				int volume = Integer.parseInt(scanner.nextLine());
				System.out.println("Enter book publish date in MM/dd/yyyy format");
				String date = scanner.nextLine();
				LocalDate bookPublishDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
				Book book = new Book(bookId, bookName, price, volume, bookPublishDate);
				book.setSubject(subject);
				addBookToSubject(book);
			} else {
				System.out.println("No records found First add subject then add book");
			}
		} catch (NumberFormatException e) {
			System.out.println("Exception occurred while adding book: " + e.getMessage());
		}
	}

	private static void addBookToSubject(Book book) {
		Session session;
		Transaction transaction = null;
		try (SessionFactory sessionFactory = HibernateUtil.getSessionFactory()) {
			session = sessionFactory.getCurrentSession();
			transaction = session.beginTransaction();
			transaction.begin();
			session.save(book);
			transaction.commit();
		} catch (Exception e) {
			System.out.println("Exception occurred while saving subject");
			transaction.rollback();
		}
	}

	private static Subject getSubject(long subjectId) {
		Subject subject = null;
		try (SessionFactory sessionFactory = HibernateUtil.getSessionFactory();) {
			Session session = sessionFactory.getCurrentSession();
			subject = session.load(Subject.class, subjectId);
		} catch (Exception e) {
			System.out.println("Exception occurred while saving subject to DB: " + e.getMessage());
		}
		return subject;
	}

	private static List<Book> getBooks() {
		Session session;
		List<Book> books = new ArrayList<>();
		try (SessionFactory sessionFactory = HibernateUtil.getSessionFactory()) {
			session = sessionFactory.getCurrentSession();
			books = session.createQuery("FROM Book").list();
		} catch (Exception e) {
			System.out.println("Exception occurred while reading books");
		}
		return books;
	}

	private static List<Subject> getSubjects() {
		Session session;
		List<Subject> subjects = new ArrayList<>();
		try (SessionFactory sessionFactory = HibernateUtil.getSessionFactory()) {
			session = sessionFactory.getCurrentSession();
			subjects = session.createQuery("FROM Subject").list();
		} catch (Exception e) {
			System.out.println("Exception occurred while reading books");
		}
		return subjects;
	}

}
