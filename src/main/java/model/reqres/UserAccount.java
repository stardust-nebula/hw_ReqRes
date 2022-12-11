package model.reqres;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAccount {
    private String email;
    private String password;
}
