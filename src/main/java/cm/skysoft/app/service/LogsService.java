package cm.skysoft.app.service;


import cm.skysoft.app.criteria.LogsCriteria;
import cm.skysoft.app.domain.Logs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface LogsService {

	/**
	 * Save a logs.
	 *
	 * @param logs
	 *            the entity to save
	 * @return the persisted entity
	 */
	Logs save(Logs logs);

	/**
	 * Get all the logs.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	Page<Logs> findAll(Pageable pageable, LogsCriteria logsCriteria);

	/**
	 * Get the "id" logs.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	Optional<Logs> findOne(Long id);

	/**
	 * Delete the "id" logs.
	 *
	 * @param id
	 *            the id of the entity
	 */
	void delete(Long id);

	List<Logs> getMouchard(LocalDateTime debut,
						   LocalDateTime fin, String libelle);

	List<Logs> getMouchard(Long agence, LocalDateTime debut, LocalDateTime fin,
						   String libelle, Long resultatmax);
}
