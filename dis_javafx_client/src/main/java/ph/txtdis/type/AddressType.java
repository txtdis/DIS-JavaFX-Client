package ph.txtdis.type;

public enum AddressType {
    // @formatter:off
    HOME,
	WORK,
	MAIN,
	DELIVERY,
	PICK_UP,
	INVOICING,
	PAYMENT,
	NONE;
    // @formatter:on

    @Override
    public String toString() {
        return name().replace("_", "-");
    }
}
