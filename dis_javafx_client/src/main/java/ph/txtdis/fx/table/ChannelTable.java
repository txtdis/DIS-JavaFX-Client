package ph.txtdis.fx.table;

import org.springframework.stereotype.Component;

import ph.txtdis.dto.Channel;
import ph.txtdis.fx.dialog.ChannelDialog;

@Component("channelTable")
public class ChannelTable extends NameListTable<Channel, ChannelDialog> {
}
