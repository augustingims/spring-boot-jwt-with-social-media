package cm.skysoft.app.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by francis on 3/3/21.
 */

@Entity
@Table(name = "settings")
public class Settings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
