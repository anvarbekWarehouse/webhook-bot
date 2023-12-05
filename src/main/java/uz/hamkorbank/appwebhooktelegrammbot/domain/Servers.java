package uz.hamkorbank.appwebhooktelegrammbot.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "servers")
@Builder
public class Servers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String chatId;

    private boolean status;

    private int tryCount;

    private String username;

    private Date last_date;

}
