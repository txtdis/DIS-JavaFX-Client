package ph.txtdis.fx.table;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Channel;
import ph.txtdis.fx.dialog.ChannelDialog;

@Lazy
@Component("channelTable")
public class ChannelTable extends NameListTable<Channel, ChannelDialog> {
}
