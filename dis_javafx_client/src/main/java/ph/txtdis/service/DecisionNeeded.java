package ph.txtdis.service;

import static java.util.stream.Collectors.toList;
import static ph.txtdis.util.SpringUtil.username;
import static ph.txtdis.util.TextUtils.blankIfNull;

import java.time.ZonedDateTime;
import java.util.List;

import ph.txtdis.dto.EntityDecisionNeeded;
import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.RestException;
import ph.txtdis.exception.StoppedServerException;
import ph.txtdis.info.SuccessfulSaveInfo;
import ph.txtdis.type.ScriptType;

public interface DecisionNeeded extends GetSet<Long> {

	default <T extends EntityDecisionNeeded<Long>> List<T> approve(List<T> list, Boolean isValid, String remarks) {
		return list.stream().map(d -> updateDecisionStatus(d, isValid, remarks)).collect(toList());
	}

	boolean canApprove();

	default boolean closeAppIfInvalid() {
		return false;
	}

	String getDecidedBy();

	ZonedDateTime getDecidedOn();

	Boolean getIsValid();

	String getRemarks();

	ScriptService getScriptService();

	<T extends EntityDecisionNeeded<Long>> ScriptType getScriptType(T d);

	void save() throws SuccessfulSaveInfo, NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException;

	default <T extends EntityDecisionNeeded<Long>> T updateDecisionStatus(T d, Boolean isValid, String remarks) {
		if (isValid != null) {
			d.setIsValid(isValid);
			d.setRemarks(blankIfNull(d.getRemarks()) + blankIfNull(remarks));
			d.setDecidedBy(username());
			d.setDecidedOn(ZonedDateTime.now());
			String script = d.getId() + "|" + isValid + "|" + remarks + "|" + username() + "|" + ZonedDateTime.now();
			getScriptService().set(getScriptType(d), script);
		}
		return d;
	}

	default void updatePerValidity(Boolean isValid, String remarks) {
		if (isValid == null)
			return;
		updateDecisionStatus(get(), isValid, remarks);
	}
}
