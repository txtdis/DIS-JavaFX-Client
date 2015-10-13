package ph.txtdis.dto;

import lombok.Data;

@Data
public abstract class AbstractId<PK> implements Keyed<PK> {

	private PK id;
}