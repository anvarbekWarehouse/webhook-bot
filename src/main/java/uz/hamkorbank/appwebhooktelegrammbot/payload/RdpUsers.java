package uz.hamkorbank.appwebhooktelegrammbot.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RdpUsers {
    private Integer id;

    private Integer chatId;

    private String username;
    private boolean status;

    private String fullName;

}
