package ph.txtdis.fx.tab;

import static java.util.Arrays.asList;
import static javafx.geometry.Pos.CENTER;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import ph.txtdis.fx.table.VolumeDiscountTable;
import ph.txtdis.service.ItemService;

@Scope("prototype")
@Component("volumeDiscountTab")
public class VolumeDiscountTab extends AbstractTab {

	@Autowired
	private ItemService service;

	@Autowired
	private VolumeDiscountTable volumeDiscountTable;

	public VolumeDiscountTab() {
		super("Volume Discount");
	}

	@Override
	public void refresh() {
		volumeDiscountTable.items(service.get().getVolumeDiscounts());
	}

	@Override
	public void save() {
		service.get().setVolumeDiscounts(volumeDiscountTable.getItems());
	}

	@Override
	protected VBox mainVerticalPane() {
		VBox vbox = super.mainVerticalPane();
		vbox.setAlignment(CENTER);
		return vbox;
	}

	@Override
	protected List<Node> mainVerticalPaneNodes() {
		return asList(box.forHorizontalPane(volumeDiscountTable.build()));
	}
}
