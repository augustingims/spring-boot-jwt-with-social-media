package cm.skysoft.app.service;

import cm.skysoft.app.domain.Clients;
import cm.skysoft.app.dto.ClientDTO;

/**
 * Created by francis on 2/12/21.
 */
public interface ClientsService {

    /**
     * save client.
     * @param client the client
     * @return client save.
     */
    Clients save(Clients client);

    /**
     * update client.
     * @param client the client
     */
    void update(ClientDTO client);

    /**
     * get client by id.
     * @param idClient the idClient
     * @return client found.
     */
    Clients getClientByIdClient(Long idClient);

    /**
     * get client by id.
     * @param idClient the idClient
     * @return client found.
     */
    Clients getClientById(Long idClient);


    /**
     * delete client.
     * @param client the client
     */
    void delete(ClientDTO client);

    /**
     * delete client by Id.
     * @param id the id
     */
    void deleteById(Long id);

    /**
     * find client by code client
     * @param codeClient of client
     * @return client
     */
    Clients findClientsByCodeClient(String codeClient);
}
