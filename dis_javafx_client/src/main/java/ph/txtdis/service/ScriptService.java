package ph.txtdis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.Script;
import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.StoppedServerException;
import ph.txtdis.type.ScriptType;
import ph.txtdis.util.ServerUtil;

@Scope("prototype")
@Service("ScriptService")
public class ScriptService {

	@Autowired
	private ReadOnlyService<Script> readOnlyService;

	@Autowired
	private SavingService<List<Script>> savingService;

	@Autowired
	private ServerUtil serverUtil;

	private List<Script> scripts;

	public ScriptService() {
		scripts = new ArrayList<>();
	}

	public void saveScripts() throws NoServerConnectionException, StoppedServerException, FailedAuthenticationException,
			InvalidException {
		if (serverUtil.isOffSite() && !scripts.isEmpty()) {
			savingService.module("script").save(scripts);
			scripts = new ArrayList<>();
		}
	}

	public void set(ScriptType t, String script) {
		if (serverUtil.isOffSite())
			scripts.add(new Script(t, script));
	}

	public boolean unpostedTransactionsExist() {
		try {
			Script s = readOnlyService.module("script").getOne("/unposted");
			return s != null;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}
}
