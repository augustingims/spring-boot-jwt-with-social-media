package cm.skysoft.app.service.impl;

import cm.skysoft.app.domain.User;
import cm.skysoft.app.domain.UserAfbs;
import cm.skysoft.app.dto.UserAfbDTO;
import cm.skysoft.app.mapper.UserAfbMapper;
import cm.skysoft.app.repository.UserAfbsRepository;
import cm.skysoft.app.repository.UserRepository;
import cm.skysoft.app.service.UserAfbsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by francis on 2/13/21.
 */
@Service
public class UserAfbsServiceImpl implements UserAfbsService {

    private final Logger LOGGER = LoggerFactory.getLogger(ClientsServiceImpl.class);

    private final UserAfbsRepository userAfbsRepository;
    private final UserRepository userRepository;

    public UserAfbsServiceImpl(UserAfbsRepository userAfbsRepository, UserRepository userRepository) {
        this.userAfbsRepository = userAfbsRepository;
        this.userRepository = userRepository;
    }

    /**
     * save userAfb.
     *
     * @param userAfb the userAfb
     * @return list of userAfb found.
     */
    @Transactional
    @Override
    public UserAfbs save(UserAfbs userAfb) {
        LOGGER.debug("REQ - save userAfb");
        return userAfbsRepository.save(userAfb);
    }

    /**
     * update userAfb.
     *
     * @param userAfb the userAfb
     */
    @Transactional
    @Override
    public void update(UserAfbDTO userAfb) {
        LOGGER.debug("REQ - update userAfb");
        if(userAfb!=null){
            if(getUserAfbById(userAfb.getId()).isPresent()){
                userAfbsRepository.save(UserAfbMapper.INSTANCE.toUserAfb(userAfb));
            }
        }
    }

    /**
     * get userAfb by id.
     *
     * @param id the id
     * @return userAfb found.
     */
    @Transactional
    @Override
    public Optional<UserAfbDTO> getUserAfbById(Long id) {
        LOGGER.debug("REQ - get userAfb by id");
        return userAfbsRepository.findById(id).map(UserAfbMapper.INSTANCE::toUserAfbDTO);
    }

    /**
     * get userAfb by id.
     *
     * @param idUserAfb the idUserAfb
     * @return userAfb found.
     */
    @Override
    public User getUserAfbByIdUserAfb(Long idUserAfb) {
        return userRepository.findUserById(idUserAfb);
    }

    @Override
    public User getUserById(String idUser) {
        return userRepository.findUsersByUserCode(idUser);
    }


    /**
     * get all userAfb.
     *
     * @param pageable the pageable
     * @return list of userAfb found.
     */
    @Transactional
    @Override
    public Page<UserAfbDTO> getUserAfbById(Pageable pageable) {
        LOGGER.debug("REQ - get all userAfb page");
        Page<UserAfbDTO> userAfbDTOs = userAfbsRepository.findAll(pageable).map(UserAfbMapper.INSTANCE::toUserAfbDTO);
        if(!userAfbDTOs.isEmpty()){
            return userAfbDTOs;
        } else {
            return new PageImpl<>(new ArrayList<>());
        }
    }

    /**
     * get all userAfbs.
     *
     * @return list of userAfb found.
     */
    @Transactional
    @Override
    public List<UserAfbDTO> getAllUserAfbs() {
        LOGGER.debug("REQ - get all userAfbs");
        List<UserAfbDTO> userAfbDTOs = userAfbsRepository.findAll()
                .stream()
                .map(UserAfbMapper.INSTANCE::toUserAfbDTO)
                .collect(Collectors.toList());
        if (!userAfbDTOs.isEmpty()) {
            return userAfbDTOs;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * delete userAfb.
     *
     * @param userAfb the userAfb
     */
    @Transactional
    @Override
    public void delete(UserAfbDTO userAfb) {
        LOGGER.debug("REQ - delete visit");
        userAfbsRepository.delete(UserAfbMapper.INSTANCE.toUserAfb(userAfb));
    }

    /**
     * delete userAfb by Id.
     *
     * @param id the id
     */
    @Transactional
    @Override
    public void deleteById(Long id) {
        LOGGER.debug("REQ - delete userAfb by id");
        userAfbsRepository.deleteById(id);
    }
}
