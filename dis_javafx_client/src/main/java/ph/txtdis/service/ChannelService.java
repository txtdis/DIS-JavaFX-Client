package ph.txtdis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.Channel;
import ph.txtdis.exception.DuplicateException;

@Service
public class ChannelService implements Listed<Channel>, SavedByName<Channel>, UniquelyNamed {

	private static final String CHANNEL = "channel";

	@Autowired
	private SavingService<Channel> savingService;

	@Autowired
	private ReadOnlyService<Channel> readOnlyService;

	@Override
	public void confirmUniqueness(String name) throws Exception {
		if (readOnlyService.module(CHANNEL).getOne("/" + name) != null)
			throw new DuplicateException(name);
	}

	@Override
	public List<Channel> list() throws Exception {
		return readOnlyService.module(CHANNEL).getList();
	}

	@Override
	public Channel save(String name) throws Exception {
		Channel entity = new Channel();
		entity.setName(name);
		return savingService.module(CHANNEL).save(entity);
	}
}