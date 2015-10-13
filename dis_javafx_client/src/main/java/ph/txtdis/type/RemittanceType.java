package ph.txtdis.type;

public enum RemittanceType {
    CHECK, CM {
        @Override
        public String toString() {
            return "CREDIT MEMO";
        }
    },
    DEPOSIT, EWT {
        @Override
        public String toString() {
            return "W/HOLDING TAX";
        }
    },
    SAFEKEEP, SHORTAGE;
}
