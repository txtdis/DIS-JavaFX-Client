package ph.txtdis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.Channel;
import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.StoppedServerException;
import ph.txtdis.type.BillingType;

@Service
public class ChannelService implements Listed<Channel>, UniquelyNamed<Channel> {

	@Autowired
	private SavingService<Channel> savingService;

	@Autowired
	private ReadOnlyService<Channel> readOnlyService;

	@Override
	public String getModule() {
		return "channel";
	}

	@Override
	public ReadOnlyService<Channel> getReadOnlyService() {
		return readOnlyService;
	}

	public List<Channel> listVisitedChannels() {
		try {
			return readOnlyService.module(getModule()).getList("/visited");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Channel save(String name, BillingType type, boolean visited) throws NoServerConnectionException,
			StoppedServerException, FailedAuthenticationException, InvalidException {
		Channel entity = new Channel();
		entity.setName(name);
		entity.setType(type);
		entity.setVisited(visited);
		return savingService.module(getModule()).save(entity);
	}
}
