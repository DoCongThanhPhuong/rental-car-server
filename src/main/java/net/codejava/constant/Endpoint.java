package net.codejava.constant;

public class Endpoint {
    public static final class V1 {
        public static final String PREFIX = "/api/v1";

        public static final class Auth {
            public static final String BASE = PREFIX + "/auth";
            public static final String LOGIN = BASE + "/login";
            public static final String REGISTER = BASE + "/register";
            public static final String CHANGE_PASSWORD = BASE + "/change-password";
            public static final String FORGET_PASSWORD = BASE + "/forget-password";
        }

        public static final class User {
            public static final String BASE = PREFIX + "/user";
            public static final String GET_DETAIL = BASE + "/my-profile";
            public static final String UPDATE = BASE + "/update";
            public static final String GET_MONEY_IN_WALLET = BASE + "/wallet";
        }

        public static final class Car {
            public static final String BASE = PREFIX + "/car";
            public static final String GET_LIST_FOR_OWNER = BASE + "/my-car";
            public static final String GET_DETAIL = BASE + "/{id}";
            public static final String GET_DETAIL_FOR_OWNER = BASE + "/owner/{id}";
            public static final String ADD_CAR = BASE + "/add";
            public static final String UPDATE = BASE + "/update/{id}";
            public static final String STOP_RENTING_CAR = BASE + "/stop-renting-car/{id}";
            public static final String SEARCH_CAR = BASE + "/search";
            public static final String UPDATE_RATING = BASE + "/rating";
        }

        public static final class Booking {
            public static final String BASE = PREFIX + "/booking";
            public static final String GET_LIST_FOR_USER = BASE + "/my-booking";
            public static final String GET_LIST_BY_CAR = BASE + "/my-booking/{carId}";
            public static final String GET_DETAIL = BASE + "/{id}";
            public static final String ADD_BOOKING = BASE + "/add";
            public static final String UPDATE = BASE + "/update/{id}";
            public static final String CONFIRM_DEPOSIT = BASE + "/confirm-deposit/{id}";
            public static final String CONFIRM_PICK_UP = BASE + "/confirm-pickup/{id}";
            public static final String CONFIRM_PAYMENT = BASE + "/confirm-payment/{id}";
            public static final String CANCELLED_BOOKING = BASE + "/cancel/{id}";
            public static final String RETURN_CAR = BASE + "/return-car/{id}";
        }

        public static final class Transaction {
            public static final String BASE = PREFIX + "/transaction";
            public static final String GET_LIST = BASE;
        }

        public static final class Feedback {
            public static final String BASE = PREFIX + "/feedback";
            public static final String GET_LIST_FOR_OWNER = BASE + "/my-feedback";
            public static final String ADD_FEEDBACK = BASE + "/{bookingId}";
            public static final String GET_RATING = BASE + "/rating";
        }

        public static final class Test {
            public static final String BASE = PREFIX + "/test";
            public static final String TEST01 = BASE + "/test01";
            public static final String TEST02 = BASE + "/test02";
        }
    }
}
