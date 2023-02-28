package msg.project.flightmanager.model;

import java.sql.Date;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "Plane")
@Table(name = "plane")
@Data
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

	@Column(name = "model", nullable = false, unique = true)
	private String model;

	@Column(name = "capacity", nullable = false)
	private int capacity;

	@Column(name = "range", nullable = false)
	private Long range;

	@Column(name = "fuelTankCapacity", nullable = false)
	private int fuelTankCapacity;

	@Column(name = "manufacturingDate")
	private Date manufacturingDate;

	@Column(name = "firstFlight")
	private Date firstFlight;

	@Column(name = "lastRevision")
	private Date lastRevision;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	public void addToCompany() {
		company.getPlanes().add(this);
	}

	public void removeFRomCompany() {
		company.getPlanes().remove(this);
	}
}
