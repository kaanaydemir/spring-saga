package com.kaanaydemir.userservice.projection;

import com.kaanaydemir.commonservice.model.CardDetail;
import com.kaanaydemir.commonservice.model.User;
import com.kaanaydemir.commonservice.query.GetUserPaymentDetailsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UserProjection {

    @QueryHandler
    public User getUserPaymentDetails(GetUserPaymentDetailsQuery query) {
        //Ideally Get the details from the DB
        CardDetail cardDetails
                = CardDetail.builder()
                .name("Shabbir Dawoodi")
                .validUntilYear("2022")
                .validUntilMonth("01")
                .cardNumber("123456789")
                .cvv("111")
                .build();

        return User.builder()
                .userId(query.getUserId())
                .firstName("Shabbir")
                .lastName("Dawoodi")
                .cardDetail(cardDetails)
                .build();
    }
}
