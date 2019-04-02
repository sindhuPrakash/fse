import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class menu {

	private static Scanner scanner = new Scanner(System.in);
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/library";

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
				System.out.println(readObject().toString());
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

	private static List<Subject> getSubjects() {
		String searchSubjectWithSubjectTitleQuery = "select * from library.subject";
		ResultSet result = null;
		List<Subject> subjectList = new ArrayList<>();
		try (Connection connection = getJDBCConnectionStatement();) {
			Statement statement = connection.createStatement();
			result = statement.executeQuery(searchSubjectWithSubjectTitleQuery);
			while (result.next()) {
				subjectList.add(new Subject(result.getLong("subjectId"), result.getString("subtitle"),
						result.getInt("durationInHours"), new HashSet<>()));
			}
			connection.close();
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Exception occurred while saving subject to DB: " + e.getMessage());
		}
		return subjectList;

	}

	private static void sortBookByTitle() {
		getBooks().stream().sorted((Book b1, Book b2) -> b1.getTitle().compareTo(b2.getTitle()))
				.forEach(System.out::println);
	}

	private static List<Book> getBooks() {
		String searchBookWithBookIdQuery = "select * from library.book";
		ResultSet result = null;
		List<Book> bookList = new ArrayList<>();
		try (Connection connection = getJDBCConnectionStatement();) {
			Statement statement = connection.createStatement();
			result = statement.executeQuery(searchBookWithBookIdQuery);
			while (result.next()) {
				bookList.add(new Book(result.getLong("bookId"), result.getString("title"), result.getDouble("price"),
						result.getInt("volume"), LocalDate.parse(result.getString("publishDate"))));
			}
			connection.close();
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Exception occurred while saving subject to DB: " + e.getMessage());
		}
		return bookList;
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
			List<Subject> filteredSubject = searchSubjectWithSubjectTitle(subjectName);
			filteredSubject.forEach(subject -> System.out.println(subject.toString()));
			break;
		}
		default:
			System.out.println("Invalid selection");
			break;
		}
	}

	private static List<Subject> searchSubjectWithSubjectTitle(String subjectName) {
		String searchSubjectWithSubjectTitleQuery = "select * from library.subject where subtitle like '" + subjectName
				+ "%'";
		ResultSet result = null;
		List<Subject> subjectList = new ArrayList<>();
		try (Connection connection = getJDBCConnectionStatement();) {
			Statement statement = connection.createStatement();
			result = statement.executeQuery(searchSubjectWithSubjectTitleQuery);
			while (result.next()) {
				subjectList.add(new Subject(result.getLong("subjectId"), result.getString("subtitle"),
						result.getInt("durationInHours"), new HashSet<>()));
			}
			connection.close();
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Exception occurred while saving subject to DB: " + e.getMessage());
		}
		return subjectList;
	}

	private static SubjectList readObject() {
		SubjectList subjectList = new SubjectList();
		String readObjectSql = "select * from library.book b inner join library.subject s on b.subjectId = s.subjectId;";
		try {
			Connection connection = getJDBCConnectionStatement();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(readObjectSql);
			Set<Long> subjectIdSet = new HashSet<>();
			while (result.next()) {
				long subjectId = result.getLong("subjectId");
				if (subjectIdSet.contains(subjectId)) {
					if (null != subjectList.getSubjectList() && !subjectList.getSubjectList().isEmpty()) {
						for (Subject subject : subjectList.getSubjectList()) {
							if (subjectId == subject.getSubjectId()) {
								if (null != subject.getReferences() && !subject.getReferences().isEmpty()) {
									subject.getReferences()
											.add(new Book(result.getLong("bookId"), result.getString("title"),
													result.getInt("price"), result.getInt("volume"),
													LocalDate.parse(result.getString("publishDate"))));
								} else {
									Set<Book> books = new HashSet<Book>();
									books.add(new Book(result.getLong("bookId"), result.getString("title"),
											result.getInt("price"), result.getInt("volume"),
											LocalDate.parse(result.getString("publishDate"))));
									subject.setReferences(books);
								}
							}
						}
					}
				} else {
					Set<Book> books = new HashSet<Book>();
					books.add(new Book(result.getLong("bookId"), result.getString("title"), result.getInt("price"),
							result.getInt("volume"), LocalDate.parse(result.getString("publishDate"))));
					Subject subject = new Subject(subjectId, result.getString("subtitle"),
							result.getInt("durationInHours"), books);
					if (null != subjectList.getSubjectList() && !subjectList.getSubjectList().isEmpty()) {
						subjectList.getSubjectList().add(subject);
					} else {
						List<Subject> subjects = new ArrayList<>();
						subjects.add(subject);
						subjectList.setSubjectList(subjects);
					}
					subjectIdSet.add(subjectId);
				}

			}
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Failed to read object from data file");
		}
		return subjectList;
	}

	private static void searchBook() {
		System.out.println("Search Book\n1.Search by Book Id.\n2.Search by Book title");
		switch (scanner.nextLine()) {
		case "1": {
			System.out.println("Enter Book Id:");
			long bookId = Long.parseLong(scanner.nextLine());
			Book filteredBook = getBook(bookId);
			System.out.println(filteredBook.toString());
			break;
		}
		case "2": {
			System.out.println("Enter Book Title:");
			String bookTitle = scanner.nextLine();
			List<Book> filteredBooks = searchBookWithBookTitle(bookTitle);
			filteredBooks.forEach(book -> System.out.println(book.toString()));
			break;
		}

		default:
			System.out.println("Invalid selection");
			break;
		}
	}

	private static List<Book> searchBookWithBookTitle(String bookTitle) {
		String searchBookWithBookIdQuery = "select * from library.book where title like '" + bookTitle + "%'";
		ResultSet result = null;
		List<Book> bookList = new ArrayList<>();
		try (Connection connection = getJDBCConnectionStatement();) {
			Statement statement = connection.createStatement();
			result = statement.executeQuery(searchBookWithBookIdQuery);
			while (result.next()) {
				bookList.add(new Book(result.getLong("bookId"), result.getString("title"), result.getDouble("price"),
						result.getInt("volume"), LocalDate.parse(result.getString("publishDate"))));
			}
			connection.close();
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Exception occurred while saving subject to DB: " + e.getMessage());
		}
		return bookList;
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
		String deleteBookQuery = "delete from library.book where bookId = " + bookId;
		int result = 0;
		try (Connection connection = getJDBCConnectionStatement();) {
			Statement statement = connection.createStatement();
			result = statement.executeUpdate(deleteBookQuery);
			connection.close();
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Exception occurred while saving subject to DB: " + e.getMessage());
		}
		return result > 0;
	}

	private static Book getBook(long bookId) {
		String getSubjectQuery = "select * from library.book where bookId = " + bookId;
		ResultSet result = null;
		Book book = null;
		try (Connection connection = getJDBCConnectionStatement();) {
			Statement statement = connection.createStatement();
			result = statement.executeQuery(getSubjectQuery);
			while (result.next()) {
				book = new Book(result.getLong("bookId"), result.getString("title"), result.getDouble("price"),
						result.getInt("volume"), LocalDate.parse(result.getString("publishDate")));
			}
			connection.close();
		} catch (ClassNotFoundException | SQLException e) {
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
		String deleteBooksForSubjectIdQuery = "delete from library.book where subjectId = " + subjectId;
		String deleteSubjectQuery = "delete from library.subject where subjectId = " + subjectId;
		int result = 0;
		try (Connection connection = getJDBCConnectionStatement();) {
			Statement statement = connection.createStatement();
			statement.executeUpdate(deleteBooksForSubjectIdQuery);
			result = statement.executeUpdate(deleteSubjectQuery);
			connection.close();
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Exception occurred while saving subject to DB: " + e.getMessage());
		}
		return result > 0;
	}

	private static void addSubject() throws ClassNotFoundException {
		try {
			System.out.println("Enter subject title");
			String subjectName = scanner.nextLine();
			System.out.println("Enter subject Id");
			long subjectId = Long.parseLong(scanner.nextLine());
			System.out.println("Enter Duration");
			int duration = Integer.parseInt(scanner.nextLine());
			if (addSubjectToDb(subjectName, subjectId, duration)) {
				System.out.println("Subject Added Successfully");
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
					System.out.println(addBookToSubject(bookId, bookName, bookPublishDate, volume, price, subjectId)
							? "Book Added to Subject Successfully"
							: "failed to add book to subject");
				}
			} else {
				System.out.println("Failed to add Subject");
			}
		} catch (NumberFormatException e1) {
			System.out.println("Failed to add subject Try Again");
		}
	}

	private static boolean addBookToSubject(long bookId, String bookName, LocalDate bookPublishDate, int volume,
			double price, long subjectId) {
		String addBookQuery = "insert into library.book (bookId,title,price,volume,publishDate,subjectId) values ('"
				+ bookId + "','" + bookName + "','" + price + "'," + volume + ",'" + bookPublishDate + "','" + subjectId
				+ "')";
		int result = 0;
		try (Connection connection = getJDBCConnectionStatement();) {
			Statement statement = connection.createStatement();
			result = statement.executeUpdate(addBookQuery);
			connection.close();
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Exception occurred while saving book to DB: " + e.getMessage());
		}
		return result > 0;

	}

	private static boolean addSubjectToDb(String subjectName, long subjectId, int duration) {
		String addSubjectQuery = "insert into library.subject (subjectId,subtitle,durationInHours) values (" + subjectId
				+ ",'" + subjectName + "'," + duration + ");";
		int result = 0;
		boolean status = false;
		try (Connection connection = getJDBCConnectionStatement();) {
			Statement statement = connection.createStatement();
			result = statement.executeUpdate(addSubjectQuery);
			if (result > 0) {
				status = true;
			}
			connection.close();
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Exception occurred while saving subject to DB: " + e.getMessage());
		}
		return status;
	}

	private static void addBook() {
		try {
			System.out.println("Enter the subject Id of the book");
			long subjectId = Long.parseLong(scanner.nextLine());
			if (getSubject(subjectId) != null) {
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
				addBookToSubject(bookId, bookName, bookPublishDate, volume, price, subjectId);
			} else {
				System.out.println("No records found First add subject then add book");
			}
		} catch (NumberFormatException e) {
			System.out.println("Exception occurred while adding book: " + e.getMessage());
		}
	}

	private static Subject getSubject(long subjectId) {
		String getSubjectQuery = "select * from library.subject where subjectId = " + subjectId;
		ResultSet result = null;
		Subject subject = null;
		try (Connection connection = getJDBCConnectionStatement();) {
			Statement statement = connection.createStatement();
			result = statement.executeQuery(getSubjectQuery);
			while (result.next()) {
				subject = new Subject(result.getLong("subjectId"), result.getString("subtitle"),
						result.getInt("durationInHours"), new HashSet<>());
			}
			connection.close();
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Exception occurred while saving subject to DB: " + e.getMessage());
		}
		return subject;
	}

	public static Connection getJDBCConnectionStatement() throws SQLException, ClassNotFoundException {
		Class.forName(JDBC_DRIVER);
		return DriverManager.getConnection(DB_URL, "root", "pass@word1");
	}

}
