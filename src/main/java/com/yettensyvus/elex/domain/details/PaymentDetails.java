package com.yettensyvus.elex.domain.details;

import com.yettensyvus.elex.domain.constants.PAYMENT_STATUS;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails {
    private String paymentId;
    private String paymentLinkId;
    private String paymentLinkReferenceId;
    private String paymentLinkStatus;
    private String paymentIdZWSP;

    private PAYMENT_STATUS status = PAYMENT_STATUS.PENDING;
}
