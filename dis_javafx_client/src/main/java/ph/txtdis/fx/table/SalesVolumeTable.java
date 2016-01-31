package ph.txtdis.fx.table;

import static ph.txtdis.type.SalesVolumeReportType.ITEM;
import static ph.txtdis.type.Type.ENUM;
import static ph.txtdis.type.Type.QUANTITY;
import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TWOPLACE;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.SalesVolume;
import ph.txtdis.type.SalesVolumeReportType;
import ph.txtdis.type.UomType;

@Lazy
@Component("salesVolumeTable")
public class SalesVolumeTable extends AppTable<SalesVolume> {

	@Autowired
	private Column<SalesVolume, String> seller;

	@Autowired
	private Column<SalesVolume, String> channel;

	@Autowired
	private Column<SalesVolume, String> customer;

	@Autowired
	private Column<SalesVolume, String> category;

	@Autowired
	private Column<SalesVolume, String> prodLine;

	@Autowired
	private Column<SalesVolume, String> item;

	@Autowired
	private Column<SalesVolume, UomType> uom;

	@Autowired
	private Column<SalesVolume, BigDecimal> vol;

	@Autowired
	private Column<SalesVolume, BigDecimal> qty;

	@Autowired
	private SalesVolumeContextMenu menu;

	public void setTableColumnVisibility(SalesVolumeReportType t) {
		hideGroupColumns();
		switch (t) {
			case CATEGORY:
				category.setVisible(true);
				break;
			case PRODUCT_LINE:
				prodLine.setVisible(true);
				break;
			case SELLER:
				seller.setVisible(true);
				category.setVisible(true);
				break;
			case CHANNEL:
				channel.setVisible(true);
				category.setVisible(true);
				break;
			case ITEM:
				item.setVisible(true);
			default:
		}
		setMinWidth(width());
		refresh();
	}

	private void hideGroupColumns() {
		seller.setVisible(false);
		channel.setVisible(false);
		customer.setVisible(false);
		category.setVisible(false);
		prodLine.setVisible(false);
		item.setVisible(false);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(//
				seller.ofType(TEXT).width(100).build("Seller", "seller"), //
				channel.ofType(TEXT).width(180).build("Channel", "channel"), //
				customer.ofType(TEXT).width(300).build("Customer", "customer"), //
				category.ofType(TEXT).width(120).build("Category", "category"), //
				prodLine.ofType(TEXT).width(120).build("Product Line", "productLine"), //
				item.ofType(TEXT).width(420).build("Item", "item"), //
				vol.ofType(TWOPLACE).build("Volume", "vol"), //
				uom.ofType(ENUM).width(60).build("UOM", "uom"), //
				qty.ofType(QUANTITY).width(90).build("Qty(PC)", "qty") //
		);
	}

	@Override
	protected void addProperties() {
		setTableColumnVisibility(ITEM);
		menu.setMenu();
	}
}
