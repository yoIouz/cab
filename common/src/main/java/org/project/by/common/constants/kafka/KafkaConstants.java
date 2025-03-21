package org.project.by.common.constants.kafka;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaConstants {

    public final String BOOKING_RIDE_TOPIC = "booking_ride";

    public final String ACCEPTED_RIDES_TOPIC = "accepted_ride";

    public final String COMPLETED_RIDES_TOPIC = "completed_rides";

    public final String CANCELLED_RIDES_TOPIC = "cancelled_rides";

    public final String PAYMENT_TOPIC = "payment";

    public final String RATING_TOPIC = "rating";

    public final String REQUEST_TOPIC = "request";


    public final String PASSENGER_RATING_PARTITION = "0";

    public final String DRIVER_RATING_PARTITION = "1";

}
