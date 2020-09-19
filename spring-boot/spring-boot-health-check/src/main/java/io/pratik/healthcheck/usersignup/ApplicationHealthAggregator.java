/**
 * 
 */
package io.pratik.healthcheck.usersignup;

import java.util.Set;

import org.springframework.boot.actuate.health.SimpleStatusAggregator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

/**
 * @author Pratik Das
 *
 */
@Component
public class ApplicationHealthAggregator extends SimpleStatusAggregator{

	@Override
	public Status getAggregateStatus(Status... statuses) {
		// TODO Auto-generated method stub
		return super.getAggregateStatus(statuses);
	}

	@Override
	public Status getAggregateStatus(Set<Status> statuses) {
		if(statuses != null) {
			for (Status status : statuses) {
				if(status.equals(Status.DOWN)) {
					
					return Status.DOWN;//
				}
			}
		}
		return Status.UP;
	}

}
