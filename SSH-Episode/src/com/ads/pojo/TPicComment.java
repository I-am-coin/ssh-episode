package com.ads.pojo;
// Generated 2018-5-10 21:48:33 by Hibernate Tools 5.2.3.Final

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * TPicComment generated by hbm2java
 */
@Entity
@Table(name = "t_pic_comment", catalog = "episode")
public class TPicComment implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer commentId;
	private TPicture TPicture;
	private TUser TUser;
	private String commentContent;
	private Integer commentGood;
	private Set<TUser> TUsers = new HashSet<TUser>(0);

	public TPicComment() {
	}

	public TPicComment(TPicture TPicture, TUser TUser, String commentContent, Integer commentGood) {
		this.TPicture = TPicture;
		this.TUser = TUser;
		this.commentContent = commentContent;
		this.commentGood = commentGood;
	}

	public TPicComment(TPicture TPicture, TUser TUser, String commentContent, Integer commentGood, Set<TUser> TUsers) {
		this.TPicture = TPicture;
		this.TUser = TUser;
		this.commentContent = commentContent;
		this.commentGood = commentGood;
		this.TUsers = TUsers;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "comment_id", unique = true, nullable = false)
	public Integer getCommentId() {
		return this.commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "picture_id", nullable = false)
	public TPicture getTPicture() {
		return this.TPicture;
	}

	public void setTPicture(TPicture TPicture) {
		this.TPicture = TPicture;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	public TUser getTUser() {
		return this.TUser;
	}

	public void setTUser(TUser TUser) {
		this.TUser = TUser;
	}

	@Column(name = "comment_content", nullable = false, length = 1000)
	public String getCommentContent() {
		return this.commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	@Column(name = "comment_good")
	public Integer getCommentGood() {
		return this.commentGood;
	}

	public void setCommentGood(Integer commentGood) {
		this.commentGood = commentGood;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "TPicComments_1")
	public Set<TUser> getTUsers() {
		return this.TUsers;
	}

	public void setTUsers(Set<TUser> TUsers) {
		this.TUsers = TUsers;
	}

}
