package cm.skysoft.app.repository;

import cm.skysoft.app.domain.Sms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SmsRepository extends JpaRepository<Sms, Long> {

	List<Sms> findByEnvoyerFalse();
}
