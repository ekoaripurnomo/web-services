package id.co.hanoman.training.webservices.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Table(name="alamat")
@Data
public class Alamat {
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	
	@Column(nullable=false)
	private String jalan;
	
	@Column(nullable=false)
	private String kota;
	
	@Column(nullable=false)
	private String propinsi;
	
	@Column(nullable=false, length=5)
	private int kodepos;
}
