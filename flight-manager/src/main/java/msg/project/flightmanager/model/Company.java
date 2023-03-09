package msg.project.flightmanager.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "Company")
@Table(name = "company")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties("hibernateLazyInitializer")
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "idCompany")
	private Long idCompany;

	@Column(name = "name", length = 30, nullable = false)
	@Length(max = 30)
	@NotNull
	private String name;

	@Column(name = "phone_number", unique = true, nullable = false)
	@NotNull
	private String phoneNumber;

	@Column(name = "email", length = 40, unique = true, nullable = false)
	@Length(max = 40)
	@NotNull
	private String email;

	@Column(name = "foundedIn")
	private LocalDate foundedIn;

	@OneToOne
	@JoinColumn(name = "address_id")
	private Address address;

	@ManyToMany(mappedBy = "companiesCollab", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Builder.Default
	private Set<Airport> airportsCollab = new HashSet<>();
	
	public void removeCollab(Airport airport) {
		this.airportsCollab.remove(airport);
		airport.getCompaniesCollab().remove(this);
	}
	
	public void addCollab(Airport airport) {
		this.airportsCollab.add(airport);
		airport.getCompaniesCollab().add(this);
	}

	@OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	@Builder.Default
	private Set<Plane> planes = new HashSet<>();
	
	public void addPlanes(Plane plane) {
		this.planes.add(plane);
		plane.setCompany(this);
	}
	
	public void removePlanes(Plane plane) {
		this.planes.remove(plane);
		plane.setCompany(null);
	}

	@OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
	@Builder.Default
	private Set<User> employees = new HashSet<>();
	
	public void addEmployee(User user) {
		this.employees.add(user);
		user.setCompany(this);
	}
	
	public void removeEmployee(User user) {
		this.employees.remove(user);
		user.setCompany(null);
	}

}
