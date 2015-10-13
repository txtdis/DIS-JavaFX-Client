package ph.txtdis.type;

public enum EndorsementType {
    INITIAL {
        @Override
        public String toString() {
            return "Initial";
        }
    },
    TRANSFER {
        @Override
        public String toString() {
            return "Transfer";
        }
    },
    FINAL {
        @Override
        public String toString() {
            return "Final";
        }
    };
}