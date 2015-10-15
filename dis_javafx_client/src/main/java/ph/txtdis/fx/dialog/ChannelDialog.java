package ph.txtdis.fx.dialog;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Channel;
import ph.txtdis.service.ChannelService;

@Lazy
@Component("channelDialog")
public class ChannelDialog extends NameListDialog<Channel, ChannelService> {
}
