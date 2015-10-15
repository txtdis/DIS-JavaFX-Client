package ph.txtdis.app;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Channel;
import ph.txtdis.fx.table.ChannelTable;
import ph.txtdis.service.ChannelService;

@Lazy
@Component("channelApp")
public class ChannelApp extends AbstractTableApp<ChannelTable, ChannelService, Channel> {
}
