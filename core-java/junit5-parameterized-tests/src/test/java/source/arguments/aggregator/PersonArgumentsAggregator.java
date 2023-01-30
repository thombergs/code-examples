package source.arguments.aggregator;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

public class PersonArgumentsAggregator implements ArgumentsAggregator {

	@Override
	public Object aggregateArguments(ArgumentsAccessor arguments, ParameterContext context)
			throws ArgumentsAggregationException {
		return new Person(arguments.getString(0), arguments.getInteger(1));
	}

}
