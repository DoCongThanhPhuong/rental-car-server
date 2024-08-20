package net.codejava.constant;

public class MailTemplate {
    public static final String PREFIX = "mail-template";

    public static class CANCEL_BOOKING {
        public static final String CANCEL_BOOKING_TEMPLATE = PREFIX + "/cancel-booking.html";
        public static final String CANCEL_BOOKING_SUBJECT = "A booking with your car has been cancelled";
    }

    public static class CHANGE_PASSWORD {
        public static final String CHANGE_PASSWORD_TEMPLATE = PREFIX + "/change-password.html";
        public static final String CHANGE_PASSWORD_SUBJECT = "Rent-a-car Password Reset";
    }

    public static class RENT_A_CAR {
        public static final String RENT_A_CAR_TEMPLATE = PREFIX + "/rent-a-car.html";
        public static final String RENT_A_CAR_SUBJECT = "Your car has been booked";
    }

    public static class CONFIRM_DEPOSIT {
        public static final String CONFIRM_DEPOSIT_TEMPLATE = PREFIX + "/confirm-deposit.html";
        public static final String CONFIRM_DEPOSIT_SUBJECT = "Deposit Confirmation";
    }

    public static class RETURN_A_CAR {
        public static final String RETURN_A_CAR_TEMPLATE = PREFIX + "/return-a-car.html";
        public static final String RETURN_A_CAR_SUBJECT = PREFIX + "Your car has been returned";
    }
}
