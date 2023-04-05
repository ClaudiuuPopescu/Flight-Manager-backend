package msg.project.flightmanager.service.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.util.CsvContext;

public class LocalDateProcessor extends CellProcessorAdaptor {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public LocalDateProcessor() {
        super();
    }

    public LocalDateProcessor(CellProcessor next) {
        super(next);
    }

    @SuppressWarnings("unchecked")
	@Override
    public Object execute(Object value, CsvContext context) throws SuperCsvException {
    	validateInputNotNull(value, context);

        if (!(value instanceof String)) {
            throw new SuperCsvCellProcessorException(String.class, value, context, this);
        }
        
        String stringValue = value.toString(); 
        if(!stringValue.matches("^\\d{4}/\\d{2}/\\d{2}$")) {
            throw new SuperCsvCellProcessorException("Date not following the format [yyyy/MM/dd]: " + stringValue, context, this);

        }

        LocalDate localDate;
        try {
            localDate = LocalDate.parse((String) value, DATE_FORMAT);
        } catch (Exception e) {
            throw new SuperCsvCellProcessorException("Failed to parse LocalDate", context, this);
        }

        return this.next.execute(localDate, context);
    }
}
