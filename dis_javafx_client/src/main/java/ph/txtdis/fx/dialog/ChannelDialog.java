package ph.txtdis.fx.dialog;

import org.springframework.stereotype.Component;

import ph.txtdis.dto.Channel;
import ph.txtdis.service.ChannelService;

@Component("channelDialog")
public class ChannelDialog extends NameListDialog<Channel, ChannelService> {
}
