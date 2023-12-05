package uz.hamkorbank.appwebhooktelegrammbot.payload;

import jdk.jfr.Timestamp;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FidoRdp {

    private Integer id;

    private String chatId;

    private String name;

    private boolean status;

    private Integer tryCount;

    private String username;

    private Date last_date;


}
