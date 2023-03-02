package msg.project.flightmanager.model;

import java.time.LocalDate;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import enums.PlaneSize;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
	@Column
	private Long idPlane;
    
	@Column(nullable = false, length = 20)
	private String model;

	@Column(nullable = false, unique = true)
    private int tailNumber;

	@Column(nullable = false)
	private int capacity;
    
	@Column(nullable = false)
	private int fuelTankCapacity;
    
	@Column(nullable = false)
	private LocalDate manufacturingDate;
    
	@Column(nullable = true)
	private LocalDate firstFlight;
    
	@Column(nullable = true)
	private LocalDate lastRevision;
    
	@Column(nullable = false)
	private PlaneSize size;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;
	
	public void addToCompany() {
		this.company.getPlanes().add(this);
	}
	
	public void removeFRomCompany() {
		this.company.getPlanes().remove(this);
	}

	@Override
	public String toString() {
		return "Plane [idPlane=" + this.idPlane + ", model=" + this.model + ", capacity=" + this.capacity
				+ ", fuelTankCapacity=" + this.fuelTankCapacity + ", manufacturingDate=" + this.manufacturingDate
				+ ", firstFlight=" + this.firstFlight + ", lastRevision=" + this.lastRevision + ", size=" + this.size
				+ ", company=" + this.company + "]";
	}
}
