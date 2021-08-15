package cm.skysoft.app.service.impl;


import cm.skysoft.app.criteria.LogsCriteria;
import cm.skysoft.app.domain.Logs;
import cm.skysoft.app.repository.LogsRepository;
import cm.skysoft.app.repository.specification.LogsSpecification;
import cm.skysoft.app.service.LogsService;
import cm.skysoft.app.utils.MethoUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class LogsServiceImpl implements LogsService {

	private final LogsRepository logsRepository;

	private final EntityManager em;

	public LogsServiceImpl(LogsRepository logsRepository, EntityManager em) {
		this.logsRepository = logsRepository;
		this.em = em;
	}

	@Override
	public Logs save(Logs logs) {
		// TODO Auto-generated method stub
		return logsRepository.save(logs);
	}

	@Override
	public Page<Logs> findAll(Pageable pageable, LogsCriteria logsCriteria) {
		// TODO Auto-generated method stub
		Page<Logs> page = logsRepository.findAll(LogsSpecification.getSpecification(logsCriteria), pageable);

		if(!page.isEmpty()){
			return page;
		}
		return new PageImpl<>(new ArrayList<>());
	}

	@Override
	public Optional<Logs> findOne(Long id) {
		// TODO Auto-generated method stub
		return logsRepository.findById(id);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		logsRepository.deleteById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Logs> getMouchard(LocalDateTime debut, LocalDateTime fin,
			String libelle) {
		// TODO Auto-generated method stub

		String query = "Select l from Logs l where l.id > 0 ";

		if (libelle != null) {

			query += " And l.libelle like :libelle ";

		}

		if (debut != null && fin != null) {

			query += " And l.dateoperation Between :datedebut And :datefin ";

		} else if (debut != null) {

			query += " And l.dateoperation >= :datedebut ";

		} else if (fin != null) {

			query += " And l.dateoperation <= :datefin ";

		}

		query += " Order by l.id Desc";

		Query q = em.createQuery(query);

		

		if (libelle != null) {

			q.setParameter("libelle", MethoUtils.formatStringToFind(libelle));
		}

		if (debut != null && fin != null) {

			q.setParameter("datedebut", debut);
			q.setParameter("datefin", fin);

		} else if (debut != null) {

			q.setParameter("datedebut", debut);

		} else if (fin != null) {

			q.setParameter("datefin", fin);

		}


		return q.getResultList();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Logs> getMouchard(Long agence, LocalDateTime debut,
			LocalDateTime fin, String libelle, Long resultatmax) {

		String query = "Select l from Logs l where l.agence.id =:agence ";

		if (libelle != null) {

			query += " And l.libelle like :libelle ";

		}

		if (debut != null && fin != null) {

			query += " And l.dateoperation Between :datedebut And :datefin ";

		} else if (debut != null) {

			query += " And l.dateoperation >= :datedebut ";

		} else if (fin != null) {

			query += " And l.dateoperation <= :datefin ";

		}

		query += " Order by l.id Desc";

		Query q = em.createQuery(query);

		if (agence != null) {

			q.setParameter("agence", agence);
		}

		if (libelle != null) {

			q.setParameter("libelle", MethoUtils.formatStringToFind(libelle));
		}

		if (debut != null && fin != null) {

			q.setParameter("datedebut", debut);
			q.setParameter("datefin", fin);

		} else if (debut != null) {

			q.setParameter("datedebut", debut);

		} else if (fin != null) {

			q.setParameter("datefin", fin);

		}

		if (resultatmax != null) {

			q.setMaxResults(resultatmax.intValue());
		}


		return q.getResultList();

	}

}
