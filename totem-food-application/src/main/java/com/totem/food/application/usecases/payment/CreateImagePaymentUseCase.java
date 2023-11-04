package com.totem.food.application.usecases.payment;

import com.totem.food.application.ports.in.dtos.payment.PaymentDto;
import com.totem.food.application.ports.in.mappers.payment.IPaymentMapper;
import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.application.ports.out.web.ISendImageRequestPort;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.ICreateImageUseCase;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@UseCase
public class CreateImagePaymentUseCase implements ICreateImageUseCase<PaymentDto, byte[]> {

    private final ISendImageRequestPort<PaymentModel> iSendRequest;
    private final IPaymentMapper iPaymentMapper;

    @Override
    public byte[] createImage(PaymentDto paymentDto) {
        var paymentModel = iPaymentMapper.toModel(paymentDto);

        return iSendRequest.sendRequest(paymentModel);
    }
}
