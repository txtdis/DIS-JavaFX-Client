package ph.txtdis.type;

public enum ReceiptReferenceType {
    PO {
        @Override
        public String toString() {
            return "P/O No.";
        }
    }, 
    SO {
        @Override
        public String toString() {
            return "S/O No.";
        }
    }; 
}
