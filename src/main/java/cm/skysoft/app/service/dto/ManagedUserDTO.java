package cm.skysoft.app.service.dto;

import lombok.Data;

@Data
public class ManagedUserDTO extends UserDTO {

    private String password;

    public ManagedUserDTO() {
        // Empty constructor needed for Jackson.
    }

}
