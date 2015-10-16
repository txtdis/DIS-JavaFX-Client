package ph.txtdis.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Channel extends AbstractTrackedId<Long>implements Comparable<Channel> {

	private String name;

	@Override
	public int compareTo(Channel o) {
		if (o == null)
			return 1;
		return toString().compareTo(o.toString());
	}

	@Override
	public String toString() {
		return name;
	}
}
