package domain.entity;

public enum DeliveryStatus {
    FINDING_DELIVERY ("در جست و جوی پیک"),
    DELIVERING ("پیک در مسیر"),
    DONE ("مشاهده فاکتور"),
    NOT_FINALIZING ("ثبت نشده");


    private final String name;

    private DeliveryStatus(String s) {
        name = s;
    }


    public String toString() {
        return this.name;
    }
}
