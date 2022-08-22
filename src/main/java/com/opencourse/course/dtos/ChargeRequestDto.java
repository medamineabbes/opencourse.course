package com.opencourse.course.dtos;

import com.stripe.param.checkout.SessionCreateParams.PaymentMethodOptions.AcssDebit.Currency;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChargeRequestDto {
    private String description;
    private float amount;
    private Currency currency;
    private String stripeEmail;
    private String stripeToken;
}
