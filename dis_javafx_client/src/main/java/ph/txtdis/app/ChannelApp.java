package ph.txtdis.app;

import org.springframework.stereotype.Component;

import ph.txtdis.dto.Channel;
import ph.txtdis.fx.table.ChannelTable;
import ph.txtdis.service.ChannelService;

@Component("channelApp")
public class ChannelApp extends AbstractTableApp<ChannelTable, ChannelService, Channel> {
}
