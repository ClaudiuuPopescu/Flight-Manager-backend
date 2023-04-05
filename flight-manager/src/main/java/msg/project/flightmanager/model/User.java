package msg.project.flightmanager.model;

import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_user")
	private Long id;

	@Column(unique = true)
	private String username;

	@Column
	private String password;

	@Column
	private String firstName;

	@Column
	private String lastName;

	@Column(unique = true)
	private String email;

	@Column(unique = true)
	private String phoneNumber;

	@Column
	private LocalDate birthDate;
	
//	@Column(name = "token")
//	private Token token;

	@Column
	@Builder.Default
	private boolean isActive = true;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "address_id")
	private Address address;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id")
	private Role role;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Company company;

	@Override
	public String toString() {
		return "User [id=" + this.id + ", username=" + this.username + ", password=" + this.password + ", firstName=" + this.firstName
				+ ", lastName=" + this.lastName + ", email=" + this.email + ", phoneNumber=" + this.phoneNumber + ", birthDate="
				+ this.birthDate + ", isActive=" + this.isActive + ", address=" + this.address + ", role=" + this.role + ", company="
				+ this.company + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.address, this.birthDate, this.company, this.email, this.firstName, this.id, this.isActive, this.lastName, this.password,
				this.phoneNumber, this.role, this.username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(this.address, other.address) && Objects.equals(this.birthDate, other.birthDate)
				&& Objects.equals(this.company, other.company) && Objects.equals(this.email, other.email)
				&& Objects.equals(this.firstName, other.firstName) && Objects.equals(this.id, other.id)
				&& this.isActive == other.isActive && Objects.equals(this.lastName, other.lastName)
				&& Objects.equals(this.password, other.password) && Objects.equals(this.phoneNumber, other.phoneNumber)
				&& Objects.equals(this.role, other.role) && Objects.equals(this.username, other.username);
	}
	
	public boolean getIsActive() {
		return this.isActive;
	}
}
