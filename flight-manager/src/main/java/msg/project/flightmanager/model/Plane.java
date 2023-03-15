package msg.project.flightmanager.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import msg.project.flightmanager.enums.PlaneSize;

@Entity(name = "Plane")
@Table(name = "plane")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties("hibernateLazyInitializer")
public class Plane {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "idPlane")
	private Long idPlane;

	@Column(name = "capacity", nullable = false)
	private int capacity;

	@Column(name = "fuelTankCapacity", nullable = false)
	private int fuelTankCapacity;

	@Column(name = "manufacturingDate")
	private LocalDate manufacturingDate;

	@Column(name = "firstFlight")
	private LocalDate firstFlight;

	@Column(name = "lastRevision")
	private LocalDate lastRevision;

	@Column(name = "size", nullable = false)
	private PlaneSize size;
    
	@Column(nullable = false, length = 20)
	private String model;

	@Column(nullable = false, unique = true)
    private int tailNumber;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Company company;
	
	public void addToCompany() {
		this.company.getPlanes().add(this);
	}

	public void removeFromCompany() {
		this.company.getPlanes().remove(this);
	}
	
	@OneToMany(mappedBy = "plane", fetch = FetchType.LAZY)
	@Builder.Default
	private Set<Flight> flights = new HashSet<>();
	
	public void addFlight(Flight flight) {
		this.flights.add(flight);
		flight.setPlane(this);
	}
	
	public void removeFlight(Flight flight) {
		this.flights.remove(flight);
		flight.setPlane(null);
	}

	@Override
	public String toString() {
		return "Plane [idPlane=" + this.idPlane + ", model=" + this.model + ", capacity=" + this.capacity
				+ ", fuelTankCapacity=" + this.fuelTankCapacity + ", manufacturingDate=" + this.manufacturingDate
				+ ", firstFlight=" + this.firstFlight + ", lastRevision=" + this.lastRevision + ", size=" + this.size
				+ ", company=" + this.company + "]";
	}
}
