# web-services
Create web use Rest

* Instal Gradle
* Go to https://gradle.org/
* Download https://services.gradle.org/distributions/gradle-3.5-bin.zip
* Create Folder c:\Gradle
* Extract gradle-3.5-bin.zip copy content ke folder C:\Gradle
* Setting Path pada Environment Variables untuk folder bin content Gradle, contoh C:\Gradle\bin atau C:\Gradle\gradle-3.5\bin Save
* buka command promp ketik ..\gradle -v
------------------------------------------------------------
Gradle 3.5
------------------------------------------------------------

Build time:   2017-04-10 13:37:25 UTC
Revision:     b762622a185d59ce0cfc9cbc6ab5dd22469e18a6

Groovy:       2.4.10
Ant:          Apache Ant(TM) version 1.9.6 compiled on June 29 2015
JVM:          1.8.0_101 (Oracle Corporation 25.101-b13)
OS:           Windows 7 6.1 amd64

* Gradle telah berhasil di instal

* Install Buildship Gradle Integration to Eclipse from Eclipse Marketplace Help-Eclipse Marketplace
* Restart eclipse

* Check Gradle sudah terpasang di eclipse
	Create new gradle project
	File-New-Other Project-Gradle Project

* Create Database latihan
* create mysql gradle depedencies
* generate springboot app dari https://start.spring.io/
	Generate Gradle Project, Java Spring Boot 1.5.3
	Group : id.co.hanoman.training.webservices
	Artifact : web-services
	Name : web-services
	Description : Aplikasi Web Services
	Package name : id.co.hanoman.training.webservices
	Packaging : Jar
	Java Version : 1.8
	Dependency : MySQL , yang lainnya di tambahkan sambil jalan
* Create main java application, biasanya klo generate dari https://start.spring.io/ sudah ada file java nya
* import hasil generate start.spring.io dengan folder web-services sebagai Existing Gradle Project
* create package id.co.hanoman.training.webservices.entity yang didalam nya akan di buat file entity java (contoh: Alamat.java)
* create Pojo Alamat.java di dalam package id.co.hanoman.training.entity
	
	package id.co.hanoman.training.webservices.entity;

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
	
* file entity gunakan library lombok untuk generate getter setter etc, 
* setting dependency lombok pada gradle, jika tidak bisa gunakan java - jar lombok.jar /path/to/eclipse agar config.ini eclipse berubah
* klo masih belum lombok terpasang di project, add dependency di gradle compileOnly "org.projectlombok:lombok:1.16.16"
* add config di application.properties agar terhubung ke database
	
	spring.datasource.url=jdbc:mysql://192.168.227.133:3306/latihan
	spring.datasource.username=root
	spring.datasource.password=123456
	spring.datasource.driver-class-name=com.mysql.jdbc.Driver	
	
* add script to application.properties agar table alamat hasil dari entity Alamat.java dapat di buat

	spring.jpa.generate-ddl=true

* create data init untuk mengisi table alamat dengan membuat file import.sql di src/main/resources
	
	insert into alamat (id, jalan, kota, propinsi, kodepos) values ('1b','Jalan Penggilingan','Cakung','Jakarta Timur',13940);
	insert into alamat (id, jalan, kota, propinsi, kodepos) values ('2b','Jalan Komarudin','Cakung','Jakarta Timur',13940);
	insert into alamat (id, jalan, kota, propinsi, kodepos) values ('3b','Jalan Duku','Kebon Jeruk','Jakarta Pusat',57890);
	insert into alamat (id, jalan, kota, propinsi, kodepos) values ('4b','Jalan Kelapa','Jagakarsa','Jakarta Selatan',23956);
	insert into alamat (id, jalan, kota, propinsi, kodepos) values ('5b','Jalan Patriot','Bekasi Barat','Bekasi',47863);

* add script to application.properties agar data di import.sql masuk kedalam table alamat

	spring.jpa.hibernate.ddl-auto=create	

* create package id.co.hanoman.training.webservices.dao untuk lokasi file interface dao data acces object
* create interface AlamatDao.java dengan class nya di extends PagingAndSortingRepository<Alamat, String>

	public interface AlamatDao extends PagingAndSortingRepository<Alamat, String>{
		
	}
	
	Alamat refer ke entity Alamat.java sedang string refer ke type id dari entity yaitu String
	(interface = java yang tidak punya methode konkrit, jadi methode nya abstrak)

* add depedencies compile('org.springframework.boot:spring-boot-starter-web') untuk aplikasi web pada springboot di build.gradle (biasakan refresh gradle project sehabis add dependencies)

* gradle bootRun, web telah aktif di port 8080 akses di http://localhost:8080

* create package untuk id.co.hanoman.training.webservices.controller, lalu buat file controllernya (contoh=AlamatController.java)
	create rest untuk mendapatkan semua record pada table database

	@RestController
	@RequestMapping("/api")
	public class AlamatController {

		@Autowired
		private AlamatDao ad;
		
		@RequestMapping(value="/alamat", method=RequestMethod.GET)	
		public Page<Alamat> daftarAlamat(Pageable page){
			return ad.findAll(page);
		}
	}
	
* pada console log ada Warn
	WARN: Establishing SSL connection without server's identity verification is not recommended. According to MySQL 5.5.45+
	untuk perbaiki hasil log tambahkan ?useSSL=false pada spring.datasource.url=jdbc:mysql://192.168.227.133:3306/latihan menjadi
	spring.datasource.url=jdbc:mysql://192.168.227.133:3306/latihan?useSSL=false	
	
* add line spring.jackson.serialization.indent_output=true untuk format data tampilan di web agar rapih
	
* add spring.jpa.show-sql=true to application.properties untuk memunculkan sql di console
	
* add line spring.jpa.properties.hibernate.format_sql=true di application.properties untuk format sql pada console

* create rest untuk mendapatkan record berdasarkan id pada table database
	tambahkan script berikut ke AlamatController.java

	@RequestMapping(value="/alamat/{id}", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Alamat> cariAlamatById(@PathVariable("id") String id){
		Alamat hasil = ad.findOne(id);
		if(hasil==null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(hasil, HttpStatus.OK);
	}
	
* Test fungsi get menggunakan Chrome Apps : REST Console
	Methode Get All
	masukan di Target Request Url : http://localhost:8080/api/alamat
	Tekan button GET
	
	{
    "content": [{
        "id": "1b",
        "jalan": "Jalan Penggilingan",
        "kota": "Cakung",
        "propinsi": "Jakarta Timur",
        "kodepos": 13940
    },
		.......
		}],
			"last": true,
			"totalElements": 5,
			"totalPages": 1,
			"size": 20,
			"number": 0,
			"sort": null,
			"first": true,
			"numberOfElements": 5
	}
	
	Methode Get By Id
	masukan di Target Request Url : http://localhost:8080/api/alamat/3b
	Tekan button GET
	
	{
			"id": "3b",
			"jalan": "Jalan Duku",
			"kota": "Kebon Jeruk",
			"propinsi": "Jakarta Pusat",
			"kodepos": 57890
	}
	
* create Rest untuk POST table database
	tambahkan script berikut ke AlamatController.java
	
	@RequestMapping(value="/alamat", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void insertAlamatBaru(@RequestBody @Valid Alamat a){
		ad.save(a);
	}
	
* Test POST menggunakan Rest Console
	masukan di Target Request Url : http://localhost:8080/api/alamat
	Body Content Header Content Type : application/json
	Request Payload Raw Body :
		{
			"jalan": "Jalan Duren Runtuh",
			"kota": "Duren Kuning",
			"propinsi": "Buah Duren",
			"kodepos": 88888
		}
	Tekan button POST	
	didapat Response Headers 
	
		Status Code: 201
		Date: Thu, 01 Jun 2017 09:34:54 GMT
		Content-Length: 0
		
	Status Code : 201 artinya Created, berarti data berhasil dibuat di table database
	atau bisa di check dengan method GET, silahkan check sendiri sesuai dengan method get di atas
	
* create Rest untuk PUT table database
	tambahkan script berikut ke AlamatController.java
	
	@RequestMapping(value="/alamat/{id}", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void updateAlamat(@PathVariable("id") String id, @RequestBody @Valid Alamat a){
		a.setId(id);
		ad.save(a);
	}

* Test POST menggunakan Rest Console
	masukan di Target Request Url : http://localhost:8080/api/alamat/3b
	Body Content Header Content Type : application/json
	Request Payload Raw Body :
		{
			"jalan" : "Jalan Duku Tuh",
			"kota" : "Kebon Jeruk Tuh",
			"propinsi" : "Jakarta Pusat Tuh",
			"kodepos" : 57897
		}
	Tekan button PUT
	didapat Response Header 
	
		Status Code: 200
		Date: Thu, 01 Jun 2017 09:48:55 GMT
		Content-Length: 0
		
	Status Code : 200 artinya Ok, berarti data berhasil diubah di table database
	atau bisa di check dengan method GET, silahkan check sendiri sesuai dengan method get di atas
	
* create Rest untuk DELETE table database
	tambahkan script berikut ke AlamatController.java

	@RequestMapping(value="/alamat/{id}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void hapusAlamat(@PathVariable("id") String id){
		ad.delete(id);
	}
	
* Test DELETE menggunakan Rest Console
	masukan di Target Request Url : http://localhost:8080/api/alamat/3b
	Tekan button DELETE
	didapat Response Header
	
	Status Code: 200
	Date: Thu, 01 Jun 2017 09:57:25 GMT
	Content-Length: 0
	
	Status Code : 200 artinya Ok, berarti data berhasil dihapus di table database
	atau bisa di check dengan method GET, silahkan check sendiri sesuai dengan method get di atas	
	
* dibawah ini contoh cara menggunakan SOAPUI untuk melakukan test RestAPI	
=========================================================================
* Test RestAPI menggunakan SOAPUI
* create method daftarAlamat di AlamatController.java dengan method Get
		@RequestMapping(value="/alamat", method=RequestMethod.GET)	
		public Page<Alamat> daftarAlamat(Pageable page){
			return ad.findAll(page);
		}
* Test menggunakan SOAPUI dengan Endpoint http://localhost:8080 dan Resource /api/alamat
* method GET tekan Submit akan menghasilkan data JSON

* create method insertAlamatBaru di AlamatController.java dengan method POST
		@RequestMapping(value="/alamat", method=RequestMethod.POST)
		@ResponseStatus(HttpStatus.CREATED)
		public void insertAlamatBaru(@RequestBody @Valid Alamat a){
			ad.save(a);
		}
* Test menggunakan SOAPUI dengan Endpoint http://localhost:8080 dan Resource /api/alamat
* method POST tekan Submit akan menghasilkan data JSON

* begitu selanjutnya untuk method DELETE, PUT	
==========================================================================

*** Bagian create RestAPI telah selesai di buat ***



* Membuat halaman view dengan angularjs

* create file WebConfiguration.java yang di extend from WebMvcConfigurerAdapter di package 

	@Configuration
	public class WebConfiguration extends WebMvcConfigurerAdapter {

		@Override
		public void addViewControllers(ViewControllerRegistry registry) {
			registry.addViewController("/alamat/list").setViewName("/alamat/list");
		}
	}
	
* add dependency spring-boot-starter-thymeleaf to gradle
	compile('org.springframework.boot:spring-boot-starter-thymeleaf')
	
* create folder templates.alamat di src/main/resources
* letakan file list.html didalam folder templates.alamat tsb
* create list.html untuk data daftar alamat
	<!DOCTYPE html>
	<html>
	<head>
	<meta charset="ISO-8859-1" />
	<title>Daftar Alamat</title>
	</head>
	<body>
		<h1>Daftar Alamat</h1>
	</body>
	</html>

* add spring.thymeleaf.cache=false to application.properties for relauch page

	-Development profile with templates and static resources reloading
	
	-Path to project
	project.base-dir=file:///D:/Data/Training/web-services

	-Templates reloading during development
	spring.thymeleaf.prefix=${project.base-dir}/src/main/resources/templates/
	spring.thymeleaf.cache=false

	-Static resources reloading during development
	spring.resources.static-locations=${project.base-dir}/src/main/resources/static/
	spring.resources.cache-period=0


* add <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js" /> to list html
* create app.js di folder /static/js/
* add <script src="/js/app.js" />

* add script to app.js
	var appLatihan = angular.module('appLatihan', []);

* add script to list.html
	<body ng-app="appLatihan">
	
* create table for get api data from angularjs di file list.html

	ng-repeat="a in dataAlamat.content" untuk mengisi data pada table

	<div ng-controller="AlamatController">	
		<table border="1">
			<thead>
				<tr>
					<th>Jalan</th>
					<th>Kota</th>
					<th>Propinsi</th>
					<th>Kode Pos</th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="a in dataAlamat.content">
					<td>{{a.jalan}}</td>	
					<td>{{a.kota}}</td>
					<td>{{a.propinsi}}</td>	
					<td>{{a.kodepos}}</td>			
				</tr>
			</tbody>	
		</table>
	</div>
	
*	script berikut untuk menampilkan data pada table dengan ng-controller="AlamatController" untuk scope didalam AlamatController pada file app.js
	
	appLatihan.controller('AlamatController', function($http, $scope){
		$scope.dataAlamat = {};
		$scope.updateDataAlamat = function(){
			$http.get('/api/alamat').then(sukses, gagal);
			function sukses (response){
				$scope.dataAlamat = response.data;
				console.log(response);
			};
			function gagal (response){
				console.log(response);
				alert('Error = '+ response);
			};
		};
		$scope.updateDataAlamat();
	});

* create button Hapus di table alamat pada list.html

	<div ng-controller="AlamatController">
		<table border="1">
			<thead>
				<tr>
					<th>Jalan</th>
					<th>Kota</th>
					<th>Propinsi</th>
					<th>Kode Pos</th>
					<th>&nbsp;</th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="a in dataAlamat.content">
					<td>{{a.jalan}}</td>	
					<td>{{a.kota}}</td>
					<td>{{a.propinsi}}</td>	
					<td>{{a.kodepos}}</td>
					<td><button ng-click="hapusAlamat(a)">hapus</button></td>				
				</tr>
			</tbody>	
		</table>
	</div>
		
* create script ng-click="hapusAlamat(a)" di latihan.js

	$scope.hapusAlamat = function(x){
		$http.delete('/api/alamat/'+x.id).then(sukses, gagal);
		function sukses(response){
			$scope.updateDataAlamat();
		};
		function gagal(response){
			console.log(response);
			alert('Error : '+response)
		};
	};
	
* add semantic.min.css to folder /static/css/
	<link rel="stylesheet" href="/css/semantic.min.css" />


* add semantic.min.js to folder /static/js/
	<script src="/js/semantic.min.js" />
	
* script table sesudah di berikan fitur semantic-ui

	<table class="ui celled table">
		<thead>
			<tr>
				<th>Jalan</th>
				<th>Kota</th>
				<th>Propinsi</th>
				<th>Kode Pos</th>
				<th>&nbsp;</th>
			</tr>
		</thead>
		<tbody>
			<tr ng-repeat="a in dataAlamat.content">
				<td>{{a.jalan}}</td>	
				<td>{{a.kota}}</td>
				<td>{{a.propinsi}}</td>	
				<td>{{a.kodepos}}</td>
				<td><button ng-click="hapusAlamat(a)" class="fluid negative ui button">Hapus</button></td>				
			</tr>
		</tbody>
		<tfoot>
			<tr><th colspan="5">
				<div class="ui right floated pagination menu">
					<a class="icon item">
						<i class="left chevron icon"></i>
					</a>
					<a class="item">1</a>
					<a class="item">2</a>
					<a class="item">3</a>
					<a class="item">4</a>
					<a class="item">5</a>
					<a class="icon item">
						<i class="right chevron icon"></i>
					</a>					
				</div>
				</th>
			</tr>
		</tfoot>	
	</table>	
		
* create from input 

	<table class="ui table">
		<tr>
			<th colspan="3">
				<div class="ui top attached secondary segment">
						<b>Form Input</b>
				</div>					
				<form class="ui form attached fluid segment">
					<div class="field">
										<label>Jalan</label> <input type="text" ng-model="alamat.jalan"
											name="jalan" placeholder="Jalan" required="true" />
								</div>	
					<div class="field">
										<label>Kota</label> <input type="text" ng-model="alamat.kota"
											name="kota" placeholder="Kota" required="true" />
								</div>
								<div class="field">
										<label>Propinsi</label> <input type="text" ng-model="alamat.propinsi"
											name="propinsi" placeholder="Propinsi" required="true" />
								</div>			
					<div class="field">
										<label>Kode Pos</label> <input type="text" ng-model="alamat.kodepos"
											name="kode pos" placeholder="Kode Pos" required="true" />
								</div>				

					<div class="ui bottom attached secondary segment">
						<div class="center aligned">
						<button ng-click="simpanAlamat()">Simpan</button>
					</div>
					</div>
				</form>	
			</th>
		</tr>
	</table>
		
* create script ng-click="simpanAlamat()" di latihan js

	$scope.simpanAlamat = function(){
		$http.post('/api/alamat/', $scope.alamat).then(sukses, gagal);
		function sukses(response){
			$scope.updateDataAlamat();
		};
		function gagal(response){
			alert('gagal ya');
			console.log(response);
			alert('Error : '+response);
		};
	};
	
	<<<==============================
	
*Create security for web app

* add dependency spring boot starter security to build.gradle, Refresh Gradle Project

	compile('org.springframework.boot:spring-boot-starter-security')
	
* restart gradle clean bootRun untuk melihat perubahan
* use user = user ; password = see in console
* customize halaman login dengan user dan password sendiri menggunakan In-Memory Authentication
* create SecurityConfiguration.java 

	@Configuration
	public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication()
				.withUser("eko")
						.password("eko123")
							.roles("USER");
		}
	}
	
	compare 
	
	@Configuration
	public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication()
				.withUser("eko")
					.password("eko123")
						.roles("USER");
		}		
	}
	
* create custom halaman login
  add addViewController untuk halaman login yang telah di customize di WebConfiguration.java
	
	 registry.addViewController("/login").setViewName("/login");

* create login.html custom di folder templates dari thymeleaf
	
	<!DOCTYPE html>
	<html>
	<head>
	<meta charset="ISO-8859-1" />
	<title>Insert title here</title>
	</head>
	<body>
		<h1>Silahkan Login</h1>

	</body>
	</html>

* add script SecurityConfiguration.java

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll();			
	}
	
* perbaiki halaman login.html diatas sehingga bisa digunakan untuk login	
	
	<!DOCTYPE html>
	<html>
	<head>
	<meta charset="UTF-8" />
	<title>Please Sign In</title>
	<link rel="stylesheet" href="css/bootstrap.min.css" />
	<link rel="stylesheet" href="css/bootstrap-theme.min.css" />
	<link rel="stylesheet" href="css/app.css" />
	</head>
	<body>

		<div class="container">

				<form class="form-signin" th:action="@{/login}" method="post">
					<div th:if="${param.error}" class="alert alert-error">Invalid username and password.</div>
			<div th:if="${param.logout}" class="alert alert-success">You have been logout.</div>
			
					<h2 class="form-signin-heading">Please sign in</h2>
					<label for="username" class="sr-only">Username</label>
					<input type="text" id="username" name="username" class="form-control" placeholder="Username" required="true" autofocus="true" />
					<label for="password" class="sr-only">Password</label>
					<input type="password" id="password" name="password" class="form-control" placeholder="Password" required="true" />
					<button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
				</form>

			</div> <!-- /container -->
		
		<script src="js/bootstrap.min.js" />
	</body>
	</html>
	
* karena halaman login ini di buat menggunakan bootstrap maka siapkan lokasi untuk file css dan js jika di perlukan pada folder /static/js dan /statis/css didalam package src/main/resources
	static/css/
						bootstrap.min.css
						bootstrap-theme.min.css
						app.css
	static/js/
						bootstrap.min.js
						
* test gradle bootRun

* create Default success url add line on SecurityConfiguration.java
	.defaultSuccessUrl("/alamat/list", true);
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
			.defaultSuccessUrl("/alamat/list", true);
	}
	
* Create Log Out add line on SecurityConfiguration.java
	.and()
	.logout();
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
			.defaultSuccessUrl("/alamat/list", true)
				.and()
			.logout();		
	}	
	
* Create button Log out on html page, sample on list.html

	<div class="ui header right aligned">
		<form name="f" th:action="@{/logout}" method="post">
			<input type="submit" value="logout" />
		</form>
	</div>
	
<<<=======================

* Move Security to database
* Create entity database users

	@Entity
	@Table(name="users")
	@Data
	public class User {
		
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		@Column(name = "id")
		private int id;
		
		@Column(name="username", unique=true, nullable=false)
		private String username;
		
		@Column(name="password", nullable=false)
		private String password;
		
		@Column(name="enabled", columnDefinition="tinyint(1) default 1")
		private boolean enabled;
	}
	
* insert sample data user to table users (import.sql)

	insert into users (username, password, enabled) values ('eko','eko123',true);
	insert into users (username, password, enabled) values ('adi','adi123',true);
	insert into users (username, password, enabled) values ('edi','edi123',false);

* Create entity database Authority

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
	
* insert sample data autorities ke table Authority (import.sql)

	insert into authorities (id_user, authority) values ('1','Admin');
	insert into authorities (id_user, authority) values ('1','Operator');
	insert into authorities (id_user, authority) values ('2','Operator');
	insert into authorities (id_user, authority) values ('3','operator');
	
* add script to SecurityConfiguration.java

	@Autowired
	private DataSource dataSource;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

	auth.jdbcAuthentication().dataSource(dataSource)
			.usersByUsernameQuery("SELECT username, password, "
				+ "enabled FROM users WHERE username=?")
			.authoritiesByUsernameQuery("SELECT u.username, a.authority "
					+ "FROM users u INNER JOIN authorities a ON u.id = a.id_user WHERE u.username=?");
	}
		
*  Create CsrfAttributeToCookieFilter.java  di package id.co.hanoman.training.config supaya angular js bisa delete, post, updateDataAlamat

	public class CsrfAttributeToCookieFilter extends OncePerRequestFilter {
			@Override
			protected void doFilterInternal(HttpServletRequest request,
					HttpServletResponse response, FilterChain filterChain)
					throws ServletException, IOException {
				CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class
						.getName());
				if (csrf != null) {
					Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
					String token = csrf.getToken();
					if (cookie==null || token!=null && !token.equals(cookie.getValue())) {
						cookie = new Cookie("XSRF-TOKEN", token);
						cookie.setPath("/");
						response.addCookie(cookie);
					}
				}
				filterChain.doFilter(request, response);
			}
	}
	
* add script berikut ke method configure(HttpSecurity http) di SecurityConfiguration.java agar XSRF-TOKEN muncul di Cookies
	
	.addFilterAfter(new CsrfAttributeToCookieFilter(), CsrfFilter.class);

* belum bisa hapus atau post walaupun cookies sudah ada XSRF-TOKEN karena di server nama token nya belum X-XSRF-TOKEN
* add method ke SecurityConfiguration.java untuk membuat nama token di server X-XSRF-TOKEN, sehingga sama dengan di klien

	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}
	
* add script berikut ke methode configure(HttpSecurity http) di SecurityConfiguration.java agar nama token di server sama X-XSRF-TOKEN

	.csrf().csrfTokenRepository(csrfTokenRepository());

===================================================================
