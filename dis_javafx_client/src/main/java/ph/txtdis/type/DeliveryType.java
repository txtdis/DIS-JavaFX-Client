package ph.txtdis.type;

public enum DeliveryType {
	DELIVERED, //
	PICK_UP {
		@Override
		public String toString() {
			return "PICK-UP";
		}
	};
}
