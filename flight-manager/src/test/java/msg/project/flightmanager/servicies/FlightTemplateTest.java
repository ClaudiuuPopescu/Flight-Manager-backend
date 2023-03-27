package msg.project.flightmanager.servicies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import msg.project.flightmanager.converter.FlightTemplateConverter;
import msg.project.flightmanager.dto.FlightTemplateDto;
import msg.project.flightmanager.exceptions.ErrorCode;
import msg.project.flightmanager.exceptions.FlightTemplateException;
import msg.project.flightmanager.model.Address;
import msg.project.flightmanager.model.Company;
import msg.project.flightmanager.model.Flight;
import msg.project.flightmanager.model.FlightTemplate;
import msg.project.flightmanager.repository.FlightTemplateRepository;
import msg.project.flightmanager.service.FlightService;
import msg.project.flightmanager.service.FlightTemplateService;

@ExtendWith(MockitoExtension.class)
public class FlightTemplateTest {
	
	@Mock
	private FlightTemplateConverter flightTemplateConverter;

	@Mock
	private FlightTemplateRepository flightTemplateRepository;

	@Mock
	private FlightService flightService;
	
	@InjectMocks
	private FlightTemplateService flightTemplateService;
	
	List<FlightTemplate> flightsTemplates;
	
	@BeforeEach
	public void init() {
		this.flightsTemplates = new ArrayList<FlightTemplate>();
		FlightTemplate flightTemplate = new FlightTemplate(1L, false, false, false, false, false, false, false, false, null);
		FlightTemplate flightTemplate2 = new FlightTemplate(2L, true, true, true, true, true, true, true, false, null);
		Collections.addAll(this.flightsTemplates, flightTemplate, flightTemplate2);
	}

	
	@Test
	void addFlightTemplate_throwsFlightTemplateException_whenThatIdIsAlreadyUsed() {
		
		FlightTemplateDto flightTemplateDto = mock(FlightTemplateDto.class);
		FlightTemplate flightTemplate = mock(FlightTemplate.class);
		flightTemplateDto.setIdFlightTemplate(1L);
		Mockito.when(this.flightTemplateRepository.findById(flightTemplateDto.getIdFlightTemplate())).thenReturn(Optional.of(flightTemplate));
		
		FlightTemplateException exception = assertThrows(FlightTemplateException.class,
				() -> this.flightTemplateService.addFlightTemplate(flightTemplateDto));

		assertEquals("A Flight Template with this ID exists!", exception.getMessage());
		assertEquals(ErrorCode.OBJECT_WITH_THIS_ID_EXISTS_IN_THE_DB, exception.getErrorCode());
	
	}
	
	@Test
	void addFlightTemplate_throwsFlightTemplateException_whenThereIsAlreadyAFlightTemplateLikeThatOne() {
		
		FlightTemplateDto flightTemplateDto = mock(FlightTemplateDto.class);
		FlightTemplate flightTemplate = mock(FlightTemplate.class);
		flightTemplateDto.setIdFlightTemplate(3L);
		
		Mockito.when(this.flightTemplateRepository.findById(flightTemplateDto.getIdFlightTemplate())).thenReturn(null);
		Mockito.when(this.flightTemplateConverter.convertToEntity(flightTemplateDto)).thenReturn(flightTemplate);
		flightTemplateDto.setIdFlightTemplate(4L);
		flightsTemplates.add(flightTemplate);
		
		Mockito.when(this.flightTemplateRepository.findAll()).thenReturn(flightsTemplates);
		FlightTemplateException exception = assertThrows(FlightTemplateException.class,
				() -> this.flightTemplateService.addFlightTemplate(flightTemplateDto));

		assertEquals("A Flight Template like the given one already exists!", exception.getMessage());
		assertEquals(ErrorCode.EXISTING_TEMPLATE_LIKE_THE_GIVEN_ONE, exception.getErrorCode());
	
	}
	
	@Test
	void addFlightTemplate_throwsNothing_whenTheFlightTemplateIsAddes() throws FlightTemplateException {
		
		FlightTemplateDto flightTemplateDto = mock(FlightTemplateDto.class);
		FlightTemplate flightTemplate = mock(FlightTemplate.class);
		flightTemplateDto.setIdFlightTemplate(3L);
		Mockito.when(this.flightTemplateRepository.findById(flightTemplateDto.getIdFlightTemplate())).thenReturn(null);
		Mockito.when(this.flightTemplateConverter.convertToEntity(flightTemplateDto)).thenReturn(flightTemplate);
		Mockito.when(this.flightTemplateRepository.findAll()).thenReturn(flightsTemplates);
		this.flightTemplateService.addFlightTemplate(flightTemplateDto);
	}
	
	@Test
	void getAllFlightTemplates_returnsEmptyList_whenThereAreNoFlightTemplatesInTheDB() {
		
		List<FlightTemplate> emptyList = new ArrayList<>();
		Mockito.when(this.flightTemplateRepository.findAll()).thenReturn(emptyList);
		List<FlightTemplate> returnedList = this.flightTemplateService.getAllFlightTemplates();
		assertEquals(emptyList.size(), returnedList.size());

	}
	
	@Test
	void getAllFlightTemplates_returnsNoEmptyList_whenThereAreFlightTemplatesInTheDB() {
		
		Mockito.when(this.flightTemplateRepository.findAll()).thenReturn(this.flightsTemplates);
		List<FlightTemplate> returnedList = this.flightTemplateService.getAllFlightTemplates();
		assertEquals(this.flightsTemplates.size(), returnedList.size());

	}
	
	@Test
	void deleteFlightTemplate_throwsFlightTemplateException_whenThereIsNoFlightTemplateWithTheGivenId() {
		
		FlightTemplateDto flightTemplateDto = mock(FlightTemplateDto.class);
		flightTemplateDto.setIdFlightTemplate(3L);
		Mockito.when(this.flightTemplateRepository.findById(flightTemplateDto.getIdFlightTemplate())).thenReturn(Optional.ofNullable(null));
		FlightTemplateException exception = assertThrows(FlightTemplateException.class,
				() -> this.flightTemplateService.deleteFlightTemplate(flightTemplateDto.getIdFlightTemplate()));

		assertEquals("A Flight Template with this ID does not exists!", exception.getMessage());
		assertEquals(ErrorCode.NOT_AN_EXISTING_ID_IN_THE_DB, exception.getErrorCode());
	}
	
	@Test
	void deleteFlightTemplate_throwsNothing_whenThereIsAFlightTemplateWithTheGivenId() throws FlightTemplateException {

		FlightTemplate flightTemplate = mock(FlightTemplate.class);
		flightTemplate.setIdFlightTemplate(3L);
		Mockito.when(this.flightTemplateRepository.findById(flightTemplate.getIdFlightTemplate())).thenReturn(Optional.of(flightTemplate));
		
		Flight flight1 = new Flight();
		flight1.setFlightTemplate(flightTemplate);
		Flight flight2 = new Flight();
		flight2.setFlightTemplate(flightTemplate);
		List<Flight> flightsWithTheGivenTemplate = new ArrayList<>();
		Collections.addAll(flightsWithTheGivenTemplate, flight1, flight2);
		Mockito.when(this.flightService.getFlightsByFlightTemplate(flightTemplate)).thenReturn(flightsWithTheGivenTemplate);
		
		this.flightTemplateService.deleteFlightTemplate(flightTemplate.getIdFlightTemplate());
	}
	
	@Test
	void updateFlightTemplate_throwsFlightTemplateException_whenThereIsNoFlightTemplateWithTheGivenId() {
		
		FlightTemplateDto flightTemplateDto = mock(FlightTemplateDto.class);
		flightTemplateDto.setIdFlightTemplate(3L);
		Mockito.when(this.flightTemplateRepository.findById(flightTemplateDto.getIdFlightTemplate())).thenReturn(Optional.ofNullable(null));
		FlightTemplateException exception = assertThrows(FlightTemplateException.class,
				() -> this.flightTemplateService.updateFlightTemplate(flightTemplateDto));

		assertEquals("A Flight Template with this ID does not exists!", exception.getMessage());
		assertEquals(ErrorCode.NOT_AN_EXISTING_ID_IN_THE_DB, exception.getErrorCode());
	}
	
	@Test
	void updateFlightTemplate_throwsFlightTemplateException_whenThereIsAFlightTemplateLikeTheGivenOne() {
		
		FlightTemplateDto flightTemplateDto = mock(FlightTemplateDto.class);
		FlightTemplate flightTemplate = mock(FlightTemplate.class);
		flightTemplateDto.setIdFlightTemplate(3L);
		
		Mockito.when(this.flightTemplateRepository.findById(flightTemplateDto.getIdFlightTemplate())).thenReturn(Optional.of(flightTemplate));
		Mockito.when(this.flightTemplateConverter.convertToEntity(flightTemplateDto)).thenReturn(flightTemplate);
		flightTemplateDto.setIdFlightTemplate(4L);
		flightsTemplates.add(flightTemplate);
		
		Mockito.when(this.flightTemplateRepository.findAll()).thenReturn(flightsTemplates);
		FlightTemplateException exception = assertThrows(FlightTemplateException.class,
				() -> this.flightTemplateService.updateFlightTemplate(flightTemplateDto));

		assertEquals("A Flight Template like the given one already exists!", exception.getMessage());
		assertEquals(ErrorCode.EXISTING_TEMPLATE_LIKE_THE_GIVEN_ONE, exception.getErrorCode());

	}
	
	@Test
	void updateFlightTemplate_throwsNothing_whenTheFlightTemplateIsUpdated() throws FlightTemplateException {
		
		FlightTemplateDto flightTemplateDto = mock(FlightTemplateDto.class);
		FlightTemplate flightTemplate = mock(FlightTemplate.class);
		
		Mockito.when(this.flightTemplateRepository.findById(flightTemplateDto.getIdFlightTemplate())).thenReturn(Optional.of(flightTemplate));
		Mockito.when(this.flightTemplateConverter.convertToEntity(flightTemplateDto)).thenReturn(flightTemplate);
		Mockito.when(this.flightTemplateRepository.findAll()).thenReturn(flightsTemplates);
		
		this.flightTemplateService.updateFlightTemplate(flightTemplateDto);

	}
	
}
