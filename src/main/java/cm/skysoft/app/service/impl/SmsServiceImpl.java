package cm.skysoft.app.service.impl;

import cm.skysoft.app.domain.Sms;
import cm.skysoft.app.repository.SmsRepository;
import cm.skysoft.app.service.SmsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SmsServiceImpl implements SmsService {

	private final SmsRepository smsRepository;

	public SmsServiceImpl(SmsRepository smsRepository) {
		this.smsRepository = smsRepository;
	}

	@Override
	public Sms save(Sms sms) {
		// TODO Auto-generated method stub
		return smsRepository.save(sms);
	}

	@Override
	public Sms findOne(Long id) {
		// TODO Auto-generated method stub
		return smsRepository.getOne(id);
	}

	@Override
	public List<Sms> getListeSmsPending() {
		// TODO Auto-generated method stub
		return smsRepository.findByEnvoyerFalse();
	}

}
