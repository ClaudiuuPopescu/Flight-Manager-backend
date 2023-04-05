package msg.project.flightmanager.service.utils;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

public class IllegalCharsProcessor extends CellProcessorAdaptor{

	 public IllegalCharsProcessor() {
	        super();
	    }

	    public IllegalCharsProcessor(CellProcessor next) {
	        super(next);
	    }

	    @SuppressWarnings("unchecked")
		@Override
	    public Object execute(Object value, CsvContext context) {
	        validateInputNotNull(value, context);

	        String stringValue = value.toString();
	        if (!stringValue.matches("^[A-Za-z0-9]+([- ][A-Za-z0-9]+)*$")) {
	            throw new SuperCsvCellProcessorException("Illegal characters found in: " + stringValue, context, this);
	        }

	        return this.next.execute(value, context);
	    }

}
