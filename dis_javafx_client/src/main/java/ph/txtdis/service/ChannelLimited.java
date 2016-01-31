package ph.txtdis.service;

import java.util.ArrayList;
import java.util.List;

import ph.txtdis.dto.Channel;
import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.RestException;
import ph.txtdis.exception.StoppedServerException;

public interface ChannelLimited {

	default Channel getChannelForAll() {
		return new Channel("ALL");
	}

	ChannelService getChannelService();

	default List<Channel> listAllChannels() throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		List<Channel> l = new ArrayList<>();
		l.add(getChannelForAll());
		l.addAll(getChannelService().list());
		return l;
	}

	default Channel nullIfAll(Channel f) {
		return f.equals(getChannelForAll()) ? null : f;
	}
}
