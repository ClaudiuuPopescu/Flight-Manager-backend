package msg.project.flightmanager.model;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "Address")
@Table(name = "address")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties("hibernateLazyInitializer")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "idAddress")
	private Long idAddress;

	@Column(name = "country", nullable = false)
	private String country;

	@Column(name = "city", nullable = false)
	private String city;

	@Column(name = "street", nullable = false)
	private String street;

	@Column(name = "streetNumber", nullable = false)
	private int streetNumber;

	@Nullable
	private int apartment;

	@OneToMany(mappedBy = "address", orphanRemoval = true, cascade = CascadeType.PERSIST)
	@Builder.Default
	private Set<User> users = new HashSet<>();

	@OneToOne(mappedBy = "address")
	private Company company;

	@OneToOne(mappedBy = "address")
	private Airport airport;
}
