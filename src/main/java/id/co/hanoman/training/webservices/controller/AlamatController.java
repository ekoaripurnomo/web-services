package id.co.hanoman.training.webservices.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import id.co.hanoman.training.webservices.dao.AlamatDao;
import id.co.hanoman.training.webservices.entity.Alamat;

@RestController
@RequestMapping("/api")
public class AlamatController {
	
	@Autowired
	private AlamatDao ad;
	
	@RequestMapping(value="/alamat", method=RequestMethod.GET)	
	public Page<Alamat> daftarAlamat(Pageable page){
		return ad.findAll(page);
	}
	
	@RequestMapping(value="/alamat/{id}", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Alamat> cariAlamatById(@PathVariable("id") String id){
		Alamat hasil = ad.findOne(id);
		if(hasil==null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(hasil, HttpStatus.OK);
	}
	
	@RequestMapping(value="/alamat", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void insertAlamatBaru(@RequestBody @Valid Alamat a){
		ad.save(a);
	}
	
	@RequestMapping(value="/alamat/{id}", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void updateAlamat(@PathVariable("id") String id, @RequestBody @Valid Alamat a){
		a.setId(id);
		ad.save(a);
	}	
	
	@RequestMapping(value="/alamat/{id}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void hapusAlamat(@PathVariable("id") String id){
		ad.delete(id);
	}	
	
}
