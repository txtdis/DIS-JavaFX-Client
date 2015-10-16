package ph.txtdis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.Channel;
import ph.txtdis.exception.DuplicateException;

@Service
public class ChannelService implements Listed<Channel>, SavedByName<Channel>, UniquelyNamed {

	@Autowired
	private SavingService<Channel> savingService;

	@Autowired
	private ReadOnlyService<Channel> readOnlyService;

	@Override
	public void confirmUniqueness(String name) throws Exception {
		if (readOnlyService.module(getModule()).getOne("/" + name) != null)
			throw new DuplicateException(name);
	}

	@Override
	public String getModule() {
		return "channel";
	}

	@Override
	public List<Channel> list() throws Exception {
		return readOnlyService.module(getModule()).getList();
	}

	@Override
	public Channel save(String name) throws Exception {
		Channel entity = new Channel();
		entity.setName(name);
		return savingService.module(getModule()).save(entity);
	}
}
