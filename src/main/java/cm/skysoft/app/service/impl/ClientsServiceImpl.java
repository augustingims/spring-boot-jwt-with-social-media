package cm.skysoft.app.service.impl;

import cm.skysoft.app.domain.Clients;
import cm.skysoft.app.dto.ClientDTO;
import cm.skysoft.app.mapper.ClientsMapper;
import cm.skysoft.app.repository.ClientsRepository;
import cm.skysoft.app.service.ClientsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by francis on 2/12/21.
 */
@Service
public class ClientsServiceImpl implements ClientsService {

    private final Logger logger = LoggerFactory.getLogger(ClientsServiceImpl.class);

    private final ClientsRepository clientsRepository;
    private final ClientsMapper clientsMapper;

    public ClientsServiceImpl(ClientsRepository clientsRepository, ClientsMapper clientsMapper) {
        this.clientsRepository = clientsRepository;
        this.clientsMapper = clientsMapper;
    }

    /**
     * save client.
     *
     * @param client the client.
     * @return client found.
     */
    @Transactional
    @Override
    public Clients save(Clients client) {
        logger.debug("REQ - save agency");
        return clientsRepository.save(client);
    }

    /**
     * update client.
     *
     * @param client the client.
     * @return client found.
     */
    @Transactional
    @Override
    public void update(ClientDTO client) {
        logger.debug("REQ - update client");
        if(client!=null){
            clientsRepository.save(clientsMapper.clientsDTOToclients(client));
        }
    }
    /**
     * get client by idClient.
     *
     * @param idClient the idClient
     * @return client found.
     */
    @Override
    public Clients getClientByIdClient(Long idClient) {
        return clientsRepository.findByIdClient(idClient);
    }

    /**
     * get client by id.
     *
     * @param idClient the id
     * @return client found.
     */
    @Override
    public Clients getClientById(Long idClient) {
        return clientsRepository.findClientsById(idClient);
    }

    /**
     * delete client.
     *
     * @param client the client
     */
    @Transactional
    @Override
    public void delete(ClientDTO client) {
        logger.debug("REQ - delete client");
        clientsRepository.delete(clientsMapper.clientsDTOToclients(client));
    }

    /**
     * delete client by Id.
     *
     * @param id the id.
     */
    @Transactional
    @Override
    public void deleteById(Long id) {
        logger.debug("REQ - delete client by id");
        clientsRepository.deleteById(id);
    }

    @Override
    public Clients findClientsByCodeClient(String codeClient) {
        return clientsRepository.findClientsByCodeClient(codeClient);
    }
}
