package pl.zajavka.business.management;

public interface Keys {

    enum InputDataGroup {
        BUY_FIRST_TIME,
        BUY_AGAIN,
        SERVICE_REQUEST,
        DO_THE_SERVICE
    }

    enum Domain {
        SALESMAN,
        MECHANIC,
        CAR,
        CUSTOMER
    }

    enum Constants {
        WHAT,
        FINISHED
    }
}
