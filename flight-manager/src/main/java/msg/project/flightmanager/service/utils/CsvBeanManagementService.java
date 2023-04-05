package msg.project.flightmanager.service.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.StrRegEx;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import msg.project.flightmanager.exceptions.FlightManagerException;

@Component
public class CsvBeanManagementService {
	
	public final String[] csvHeaderUser = {"USERNAME", "FIRST NAME", "LAST NAME", "EMAIL", "PHONE NUMBER", "BIRTH DATE", "ACTIVE"};
	public final String[] fieldMappingUser = {"username", "firstName", "lastName", "email", "phoneNumber", "birthDate", "isActive"};
	
	public final String[] csvHeaderPlane = {"TAIL NUMBER", "MODEL", "CAPACITY", "FUEL TANK CAPACITY", "MANUFACTURING DATE", "FIRST FLIGHT", "LAST REVISION", "SIZE"};
	public final String[] fieldMappingPlane = {"tailNumber", "model", "capacity", "fuelTankCapacity", "manufacturingDate", "firstFlight", "lastRevistion", "size"};
	
	public final String[] csvHeaderAirport = {"NAME", "CODE IDENTIFIER", "RUN WAYS", "GATE WAYS"};
	public final String[] fieldMappingAirport = {"airportName", "codeIdentifier", "runWays", "gateWays"};
	
	public final String[] csvHeaderCompany = {"NAME", "PHONE NUMBER", "EMAIL", "FOUNDED IN", "ACTIVE"};
	public final String[] fieldMappingCompany ={"name", "phoneNumber", "email", "foundedIn", "isActive"};

	public CsvBeanWriter getCsvBeanWriter(PrintWriter writer) {
		return new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);
	}
	
	public CsvBeanReader getCsvBeanReader(MultipartFile file) {
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
			CsvBeanReader reader = new CsvBeanReader(inputStreamReader, CsvPreference.STANDARD_PREFERENCE);
			return reader;
		} catch (IOException e) {
			e.printStackTrace();
			throw new FlightManagerException(
					HttpStatus.EXPECTATION_FAILED,
					"Could not get the input stream out of the file");
		}
	}
	
	public CellProcessor[] getUserProcessor() {
		CellProcessor[] processors = new CellProcessor[] {
				new NotNull(new StrRegEx("^[^,;.:\\\"'\\\\s\\\\p{So}:]*$")), // PASSWORD
	            new IllegalCharsProcessor(new StrRegEx("^[A-Za-z]+([ -]*[A-Za-z]+)*$")), // FIRST NAME
	            new IllegalCharsProcessor(new StrRegEx("^[A-Za-z]+([ -]*[A-Za-z]+)*$")), // LAST NAME
	            new NotNull(new StrRegEx("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@airline.com$")), // EMAIL
	            new IllegalCharsProcessor(new StrRegEx("^(\\+4|)?(07[0-8]{1}[0-9]{1}|02[0-9]{2}|03[0-9]{2}){1}?(\\s|\\.|\\-)?([0-9]{3}(\\s|\\.|\\-|)){2}$")), // PHONE NUMBER
	            new LocalDateProcessor(), // BIRTH DATE
	            new IllegalCharsProcessor(new StrRegEx("^[A-Za-z]+$")), // ROLE TITLE
	            // address dto fields
	            new IllegalCharsProcessor(new StrRegEx("^[A-Za-z]+([ ]*[A-Za-z]+)*$")), // COUNTRY
	            new IllegalCharsProcessor(new StrRegEx("^[A-Za-z]+([ -]*[A-Za-z]+)*$")), // CITY
	            new IllegalCharsProcessor(new StrRegEx("^[A-Za-z]+([ -]*[A-Za-z]+)*$")), // STREET
	            new IllegalCharsProcessor(new ParseInt()), // STREET NO
	            new Optional(new ParseInt()), // APARTMENT
		};
		return processors;
	}
	
	public CellProcessor[] getPlaneProcessor() {
		CellProcessor[] processors = new CellProcessor[] {
				new IllegalCharsProcessor(), // MODEL
				new IllegalCharsProcessor(new ParseInt()), // TAIL NUMBER
	            new IllegalCharsProcessor(new ParseInt()), // CAPACITY
	            new IllegalCharsProcessor(new ParseInt()), // FUEL TANK CAPACITY
	            new LocalDateProcessor(), // MANUFACTURING DATE
	            new IllegalCharsProcessor(new StrRegEx("^[a-z]+$")), // SIZE
		};
		return processors;
	}
	
	public CellProcessor[] getAirportProcessor() {
		CellProcessor[] processors = new CellProcessor[] {
				new IllegalCharsProcessor(new StrRegEx("^[A-Za-z]+$")), // AIRPORT NAME
	            new IllegalCharsProcessor(new ParseInt()), // RUN WAYS
	            new IllegalCharsProcessor(new ParseInt()), // GATE WAYS
	            // address dto fields
	            new IllegalCharsProcessor(new StrRegEx("^[A-Za-z]+([ ]*[A-Za-z]+)*$")), // COUNTRY
	            new IllegalCharsProcessor(new StrRegEx("^[A-Za-z]+([ -]*[A-Za-z]+)*$")), // CITY
	            new IllegalCharsProcessor(new StrRegEx("^[A-Za-z]+([ -]*[A-Za-z]+)*$")), // STREET
	            new IllegalCharsProcessor(new ParseInt()), // STREET NO
	            new Optional(new ParseInt()), // APPARTMENT
		};
		return processors;
	}
	
	public CellProcessor[] getCompanyProcessor() {
		CellProcessor[] processors = new CellProcessor[] {
				new IllegalCharsProcessor(new StrRegEx("^[A-Za-z]+$")), // NAME
	            new IllegalCharsProcessor(), // PHONE NUMBER
	            new NotNull(new StrRegEx("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@airline.com$")), // EMAIL
	            new LocalDateProcessor(), // FOUNDED IN // LocalDate
	            // address dto fields
	            new IllegalCharsProcessor(new StrRegEx("^[A-Za-z]+([ ]*[A-Za-z]+)*$")), // COUNTRY
	            new IllegalCharsProcessor(new StrRegEx("^[A-Za-z]+([ -]*[A-Za-z]+)*$")), // CITY
	            new IllegalCharsProcessor(new StrRegEx("^[A-Za-z]+([ -]*[A-Za-z]+)*$")), // STREET
	            new IllegalCharsProcessor(new ParseInt()), // STREET NO
	            new Optional(new ParseInt()), // APPARTMENT
		};
		return processors;
	}
}
