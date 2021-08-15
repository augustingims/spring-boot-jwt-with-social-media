package cm.skysoft.app.web;

import cm.skysoft.app.domain.Clients;
import cm.skysoft.app.service.ClientsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
public class ClientResource {

    private final ClientsService clientsService;

    public ClientResource(ClientsService clientsService) {
        this.clientsService = clientsService;
    }

    /**
     *
     * @param codeClient by codeClient
     * @return customer by codeClient
     */
    @GetMapping("/{codeClient}")
    public ResponseEntity<Clients> findClientsByCodeClient(@PathVariable String codeClient){
        return ResponseEntity.ok().body(clientsService.findClientsByCodeClient(codeClient));
    }
}
