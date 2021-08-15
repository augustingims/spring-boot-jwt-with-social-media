package cm.skysoft.app.service;

import cm.skysoft.app.domain.Sms;

import java.util.List;

public interface SmsService {

	public Sms save(Sms sms);

	public Sms findOne(Long id);

	public List<Sms> getListeSmsPending();
}
