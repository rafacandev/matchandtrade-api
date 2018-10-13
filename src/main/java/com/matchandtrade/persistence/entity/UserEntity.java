package com.matchandtrade.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name = "user_tb") // 'user' is a reserved word in most databases, hence we are sufixing it with '_tb' 
public class UserEntity implements com.matchandtrade.persistence.entity.Entity {
	
	public enum Role {
		ADMINISTRATOR, USER
	}

	private Set<ArticleEntity> articles = new HashSet<>();
	private Integer userId;
	private String email;
	private String name;
	private Role role = Role.USER;

	@OneToMany
	@JoinTable(name="user_to_article",
		joinColumns = @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name="user_to_article_user_id_fk")),
		inverseJoinColumns = @JoinColumn(name="article_id", foreignKey=@ForeignKey(name="user_to_article_article_id_fk")))
	public Set<ArticleEntity> getArticles() {
		return articles;
	}
	
	@Column(name = "email", length = 500, nullable = false, unique = true)
	public String getEmail() {
		return email;
	}

	@Column(name = "name", length = 150, nullable = false, unique = false)
	public String getName() {
		return name;
	}

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getUserId() {
		return userId;
	}
	
	public void setArticles(Set<ArticleEntity> articles) {
		this.articles = articles;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name="role", nullable=false, unique=false, length=100)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
}
