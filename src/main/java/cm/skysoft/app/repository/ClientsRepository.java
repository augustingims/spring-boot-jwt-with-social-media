package cm.skysoft.app.repository;

import cm.skysoft.app.domain.Clients;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by francis on 2/12/21.
 */
public interface ClientsRepository extends JpaRepository<Clients, Long>{
    Clients findByIdClient(Long idClient);

    Clients findClientsById(Long id);

    Clients findClientsByCodeClient(String codeClient);
}
