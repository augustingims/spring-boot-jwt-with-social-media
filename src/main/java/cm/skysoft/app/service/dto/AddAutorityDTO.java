package cm.skysoft.app.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddAutorityDTO {
    private String userId;
    private List<AuthorityDTO> authorities;
}
