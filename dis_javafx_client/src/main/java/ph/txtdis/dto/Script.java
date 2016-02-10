package ph.txtdis.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ph.txtdis.type.ScriptType;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Script extends AbstractId<Long> {

	private ScriptType type;

	private String script;

	private boolean sent;

	public Script(ScriptType type, String script) {
		this.type = type;
		this.script = script;
	}
}
