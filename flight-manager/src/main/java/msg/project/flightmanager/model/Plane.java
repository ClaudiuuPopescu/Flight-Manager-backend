package msg.project.flightmanager.model;

import java.sql.Date;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Plane")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plane {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@Column
	private String model;

	@Column
	private int capacity;

	@Column
	private Long range;

	@Column
	private int fuelTankCapacity;

	@Column
	private Date manufacturingDate;

	@Column
	private Date firstFlight;

	@Column
	private Date lastRevision;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;
}
