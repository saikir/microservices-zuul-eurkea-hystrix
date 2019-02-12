package com.bookstore.dbservice.model;

import javax.persistence.*;

@Entity
@Table(name="books", catalog="mymc")
public class Books {

	@Id
	@Column(name="book_id", unique=true)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int Id;
	
	@Column(name="book_name")
	private String bookName;
	
	@Column(name="author_name")
	private String author;
	
	@Column(name="genre")
	private String genre;
	
	@Column(name="status")
	private String status;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Books(int id, String bookName, String author, String genre, String status) {
		super();
		Id = id;
		this.bookName = bookName;
		this.author = author;
		this.genre = genre;
		this.status = status;
	}

	protected Books() {
	}

	@Override
	public String toString() {
		return "Books [Id=" + Id + ", bookName=" + bookName + ", author=" + author + ", genre=" + genre + ", status="
				+ status + "]";
	}
	
	
}
