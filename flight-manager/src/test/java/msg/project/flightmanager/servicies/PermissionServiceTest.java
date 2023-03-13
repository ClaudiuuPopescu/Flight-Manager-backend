package msg.project.flightmanager.servicies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import msg.project.flightmanager.enums.PermissionEnum;
import msg.project.flightmanager.exceptions.FlightManagerException;
import msg.project.flightmanager.model.Permission;
import msg.project.flightmanager.repository.PermissionRepository;
import msg.project.flightmanager.service.PermissionService;

@ExtendWith(MockitoExtension.class)
public class PermissionServiceTest {

	@InjectMocks
	private PermissionService permissionService;
	@Mock
	private PermissionRepository permissionRepository;

	@Test
	void getAll_returnsPermissionList() {
		Permission permission_import = new Permission(1L, PermissionEnum.ADMINISTRATOR_MANAGEMENT, "import_data",
				Collections.emptySet());
		Permission permission2_export = new Permission(2L, PermissionEnum.ADMINISTRATOR_MANAGEMENT, "export_Data",
				Collections.emptySet());

		List<Permission> permissions = Arrays.asList(permission_import, permission2_export);

		Mockito.when(this.permissionRepository.findAll()).thenReturn(permissions);

		assertEquals(permissions, this.permissionService.getAll());
	}

	@Test
	void addPermission_throwsFlightManagerException_whenPermissionEnumNotFound() {
		String permissionEnumLabel = "invalidEnumLabel";

		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.permissionService.addPermission(permissionEnumLabel));

		assertEquals("Invalid permission label: " + permissionEnumLabel, thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.permissionService.addPermission(permissionEnumLabel));
	}

	@Test
	void addPermission_throwsFlightManagerException_whenPermissionAlreadyExistis() {
		String permissionEnumName = "import_data";

		Optional<Permission> optionalPermission = Optional.of(new Permission(1L,
				PermissionEnum.ADMINISTRATOR_MANAGEMENT, permissionEnumName, Collections.emptySet()));

		Mockito.lenient().when(this.permissionRepository.findByTitle(permissionEnumName))
				.thenReturn(optionalPermission);

		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.permissionService.addPermission(permissionEnumName));

		assertEquals(MessageFormat.format("Permission [{0}] already exists", permissionEnumName), thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.permissionService.addPermission(permissionEnumName));
	}

	@Test
	void addPermission_returnsTrue_whenAllConditionsPass() {
		String permissionEnumName = "import_data";

		assertTrue(this.permissionService.addPermission(permissionEnumName));
	}

	@Test
	void getPermissionByTitle_throwsFlightManagerException_whenPermissionWithTitleDoesntExist() {
		String permissionTitle = "IMPORT_DATA";

		Mockito.lenient().when(this.permissionRepository.findByTitle(permissionTitle)).thenReturn(Optional.empty());

		FlightManagerException thrown = assertThrows(FlightManagerException.class,
				() -> this.permissionService.getPermissionByTitle(permissionTitle));

		assertEquals(MessageFormat.format("Permission with title [{0}] not found", permissionTitle),
				thrown.getMessage());
		assertThrows(FlightManagerException.class, () -> this.permissionService.getPermissionByTitle(permissionTitle));
	}

	@Test
	void getPermissionByTitle_returnsPermission_whenPermissionWithTitleExists() {
		String permissionTitle = "IMPORT_DATA";

		Permission permission = new Permission(1L, PermissionEnum.ADMINISTRATOR_MANAGEMENT, permissionTitle,
				Collections.emptySet());
		Optional<Permission> optionalPermission = Optional.of(permission);

		Mockito.lenient().when(this.permissionRepository.findByTitle(permissionTitle)).thenReturn(optionalPermission);

		assertEquals(permission, this.permissionService.getPermissionByTitle(permissionTitle));
	}

}
