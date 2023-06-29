package mock.ports.out.dto;

import com.totem.food.application.ports.out.dtos.EmailNotificationDto;

public class EmailNotificationDtoMock {

    public static EmailNotificationDto getMock(String id) {
        var emailNotificationDto = new EmailNotificationDto();
        emailNotificationDto.setEmail("customer@gmail.com");
        emailNotificationDto.setMessage(String.format("Pedido %s acabou de ser finalizado pela cozinha, em instantes o atendente ira chama-lo!", id));
        emailNotificationDto.setSubject(String.format("[%s] Pedido %s", "Totem Food Service", id));
        return emailNotificationDto;
    }
}
