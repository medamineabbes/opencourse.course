package com.opencourse.course.apis;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opencourse.course.dtos.ChargeRequestDto;
import com.opencourse.course.services.SubscriptionService;
import com.stripe.param.SetupIntentConfirmParams.PaymentMethodOptions.AcssDebit.Currency;

import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/api/v1/subscription")
@AllArgsConstructor
public class SubscriptionController {
    
    private final SubscriptionService subscriptionService;

    //only authentic users
    @PostMapping("/subscrib")
    public ResponseEntity<Object> charge(ChargeRequestDto chargeRequest,Model model){

        Map<String,Object> chargeParam=new HashMap<>();
        chargeParam.put("amount", chargeRequest.getAmount());
        chargeParam.put("currency", Currency.USD);
        chargeParam.put("description", "testing the payment methode");
        chargeParam.put("source", chargeRequest.getStripeToken());
        return ResponseEntity.ok().build();
    }

    //only other services
    @PostMapping("/")
    public ResponseEntity<Boolean> userHasAccess(@RequestBody(required = true) Long sectionId,@RequestBody(required = true) Long userId){
        return ResponseEntity.ok()
        .body(subscriptionService.userHasAccessToSection(sectionId, userId));
    }

}
