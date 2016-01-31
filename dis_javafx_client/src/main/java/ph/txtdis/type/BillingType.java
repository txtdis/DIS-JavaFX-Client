package ph.txtdis.type;

public enum BillingType {
	DELIVERY {
		@Override
		public String toString() {
			return "D/R";
		}
	}, //
	INVOICE {
		@Override
		public String toString() {
			return "S/I";
		}
	};
}
