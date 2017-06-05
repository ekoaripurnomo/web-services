package id.co.hanoman.training.webservices.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="authorities")
@Data
public class Authority {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name="id_user", nullable=false)
	private User idUser;
	
	@Column(name="authority", nullable=false)
	private String authority;
}