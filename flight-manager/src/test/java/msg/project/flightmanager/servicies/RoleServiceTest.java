package msg.project.flightmanager.servicies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import msg.project.flightmanager.enums.PermissionEnum;
import msg.project.flightmanager.enums.RoleEnum;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.model.Permission;
import msg.project.flightmanager.model.Role;
import msg.project.flightmanager.modelHelper.AddPermissionToRoleModel;
import msg.project.flightmanager.repository.PermissionRepository;
import msg.project.flightmanager.repository.RoleRepository;
import msg.project.flightmanager.service.PermissionService;
import msg.project.flightmanager.service.RoleService;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
	@InjectMocks
	private RoleService roleService;
	@Mock
	private RoleRepository roleRepository;
	@Mock
	private PermissionService permissionService;
	@Mock
	private PermissionRepository permissionRepository;

	@Test
	void getAll_returnsRoleList() {
		Role administrator = new Role(1L, RoleEnum.ADM, "administrator", Collections.emptySet(),
				Collections.emptySet());
		Role company_manager = new Role(2L, RoleEnum.CM, "company_manager", Collections.emptySet(),
				Collections.emptySet());
		Role flight_manager = new Role(3L, RoleEnum.FM, "flight_manager", Collections.emptySet(),
				Collections.emptySet());
		Role crew = new Role(3L, RoleEnum.CREW, "crew", Collections.emptySet(), Collections.emptySet());

		List<Role> roles = Arrays.asList(administrator, company_manager, flight_manager, crew);

		Mockito.when(this.roleService.getAll()).thenReturn(roles);

		assertEquals(roles, this.roleService.getAll());
	}

	@Test
	void addRole_throwsFlightManagerException_whenRoleEnumNotFound() {
		String roleEnumLabel = "invalidEnumLabel";

		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.roleService.addRole(roleEnumLabel));

		assertEquals("Invalid role label: " + roleEnumLabel, thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.roleService.addRole(roleEnumLabel));
	}

	@Test
	void addRole_throwsFlightManagerException_whenRoleAlreadyExistis() {
		String roleEnumLabel = "crew";

		Optional<Role> optionalRole = Optional
				.of(new Role(1L, RoleEnum.ADM, "administrator", Collections.emptySet(), Collections.emptySet()));

		Mockito.lenient().when(this.roleRepository.findByTitle(roleEnumLabel)).thenReturn(optionalRole);

		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.roleService.addRole(roleEnumLabel));

		assertEquals(MessageFormat.format("Role [{0}] already exists", roleEnumLabel), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.roleService.addRole(roleEnumLabel));
	}

	@Test
	void addRole_returnsTrue_whenAllConditionsPass() {
		String roleEnumLabel = "crew";

		assertTrue(this.roleService.addRole(roleEnumLabel));
	}

	@Test
	void addPermissionToRole_throwsFlightManagerException_whenPermissionNotFound() {
		String permissionTitle = "invalidPermissionTitle";

		Mockito.lenient().when(this.permissionRepository.findByTitle(permissionTitle)).thenReturn(Optional.empty());

		AddPermissionToRoleModel addModel = new AddPermissionToRoleModel("crew", permissionTitle);

		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.roleService.addPermissionToRole(addModel));

		assertEquals(MessageFormat.format("Adding permission to role failed. Permission with title [{0}] not found",
				permissionTitle), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.roleService.addPermissionToRole(addModel));
	}

	@Test
	void addPermissionToRole_throwsFlightManagerException_whenRoleNotFound() {
		String roleTitle = "invalidRoleTitle";

		Optional<Permission> optionalPermission = Optional
				.of(new Permission(1L, PermissionEnum.ADMINISTRATOR_MANAGEMENT, "IMPORT_DATA", Collections.emptySet()));

		Mockito.lenient().when(this.permissionRepository.findByTitle("IMPORT_DATA")).thenReturn(optionalPermission);
		Mockito.lenient().when(this.roleRepository.findByTitle(roleTitle)).thenReturn(Optional.empty());

		AddPermissionToRoleModel addModel = new AddPermissionToRoleModel(roleTitle, "import_data");

		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.roleService.addPermissionToRole(addModel));

		assertEquals(MessageFormat
				.format("Adding permission to role failed. Can not find role by title [{0}] not found", roleTitle),
				thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.roleService.addPermissionToRole(addModel));
	}

	@Test
	void addPermissionRole_returnsTrue_whenAllConditionsPass() {
		Optional<Permission> optionalPermission = Optional
				.of(new Permission(1L, PermissionEnum.ADMINISTRATOR_MANAGEMENT, "IMPORT_DATA", new HashSet<>()));
		
		Optional<Role> optionalRole = Optional
				.of(new Role(1L, RoleEnum.ADM, "ADMINISTRATOR", new HashSet<>(), new HashSet<>()));

		Mockito.lenient().when(this.permissionRepository.findByTitle("IMPORT_DATA")).thenReturn(optionalPermission);
		Mockito.lenient().when(this.roleRepository.findByTitle("ADMINISTRATOR")).thenReturn(optionalRole);
		
		AddPermissionToRoleModel addModel = new AddPermissionToRoleModel("administrator", "import_data");
		
		assertTrue(this.roleService.addPermissionToRole(addModel));
	}

	@Test
	void getRoleByTitle_throwsFlightManagerException_whenRoleWithTitleDoesntExist() {
		String roleTitle = "CREW";

		Mockito.lenient().when(this.roleRepository.findByTitle(roleTitle)).thenReturn(Optional.empty());

		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.roleService.getRoleByTitle(roleTitle));

		assertEquals(MessageFormat.format("Can not find role by title [{0}] not found", roleTitle),
				thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.roleService.getRoleByTitle(roleTitle));
	}

	@Test
	void getRoleByTitle_returnsPermission_whenPermissionWithTitleExists() {
		String roleTitle = "CREW";

		Role role = new Role(1L, RoleEnum.ADM, "administrator", Collections.emptySet(), Collections.emptySet());
		Optional<Role> optionalRole = Optional.of(role);

		Mockito.lenient().when(this.roleRepository.findByTitle(roleTitle)).thenReturn(optionalRole);

		assertEquals(role, this.roleService.getRoleByTitle(roleTitle));
	}

}
