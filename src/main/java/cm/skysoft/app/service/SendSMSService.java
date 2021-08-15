package cm.skysoft.app.service;

import cm.skysoft.app.domain.ConfigApplication;
import cm.skysoft.app.domain.ConfigSms;
import cm.skysoft.app.domain.Sms;
import cm.skysoft.app.service.dto.ReponseSendSms;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class SendSMSService {

	private int NOMBReCHIFFRePHONeVALIDE = 9;

	public static String ENCODAGE = "UTF-8";

	private static String INTERNATIONAlPHONE = "00";

	private static String INDICATIfPAYsCAMEROUN = "237";

	private static final String CHECKSOLDeSKYSOFT = "http://137.74.43.214:9000/api/nosProducts/soldesms?";

	private static final String SENDSMsSKYSOFT = "http://137.74.43.214:9000/api/nosProducts/sendsms/v1/output=json?";

	static final String TARIFAIReSKYSOFT = "http://137.74.43.214:9000/api/nosProducts/getListeTarifaires?";

	static final String OPERATEURsMONEY = "http://137.74.43.214:9000/api/nosProducts/getListeOperateursMoney?";

	static final String PLACEMENtRECHARGE = "http://137.74.43.214:9000/api/nosProducts/demandeRecharge?";


	private final SmsService smsService;
	private final ConfigSmsService configSmsService;

	public SendSMSService(SmsService smsService, ConfigSmsService configSmsService) {
		super();
		this.smsService = smsService;
		this.configSmsService = configSmsService;
	}

	public void send(ConfigSms configSms, String message, String phone) {

		NOMBReCHIFFRePHONeVALIDE = configSms.getNombreChiffreTelephone()
				.intValue();

		int count = 0;

		List<String> phones = getPhoneNumber(phone);

		for (String p : phones) {

			count += saveSms(configSms, message, p);
		}

	}

	public List<String> getListePhones(ConfigApplication configApplication, String phone) {

		NOMBReCHIFFRePHONeVALIDE = configApplication.getNombreChiffreTelephone()
				.intValue();

		return getPhoneNumber(phone);
	}

	public ReponseSendSms soldeSms(ConfigApplication configApplication) {

		ReponseSendSms result = null;

		final String uri = CHECKSOLDeSKYSOFT + "user={userParam}"
				+ "&password={passwordParam}";

		try {

			Map<String, String> params = new HashMap<>();
			params.put("userParam", configApplication.getUserSms());
			params.put("passwordParam", configApplication.getPasswordSms());

			RestTemplate restTemplate = new RestTemplate();

			result = restTemplate.getForObject(uri, ReponseSendSms.class,
					params);

		} catch (Exception ignored) {

		}
		return result;

	}

	public int sendSmsold(ConfigApplication configApplication, String message, String phone) {

		ReponseSendSms result;

		final String uri = SENDSMsSKYSOFT + "user={userParam}"
				+ "&password={passwordParam}" + "&sender={senderParam}"
				+ "&phone={phoneParam}" + "&message={messageParam}";

		phone = formatTelephone(phone);

		Sms sms = new Sms();

		sms.setCout(0);

		sms.setDateEnregistrement(LocalDateTime.now());

		sms.setDestinataire(phone);
		sms.setMessage(message);
		sms.setPage(getNombrePage(message));
		sms.setCout(sms.getPage());

		String agent = "serveursms";

		sms.setAgent(agent);

		sms.setSender(configApplication.getSenderSms());

		sms.setEnvoyer(Boolean.FALSE);

		sms.setCout(0);

		sms = smsService.save(sms);

		try {

			Map<String, String> params = new HashMap<>();
			params.put("userParam", configApplication.getUserSms());
			params.put("passwordParam", configApplication.getPasswordSms());
			params.put("senderParam", configApplication.getSenderSms());
			params.put("phoneParam", sms.getDestinataire());
			params.put("messageParam", sms.getMessage());

			RestTemplate restTemplate = new RestTemplate();

			result = restTemplate.getForObject(uri, ReponseSendSms.class,
					params);

			if(result != null) {
				resultSms(sms, result);
			}

		} catch (Exception ignored) {

		}

		return sms.getCout();

	}

	public int sendSmsFree(String message, String phone) {

		ReponseSendSms result;

		final String uri = SENDSMsSKYSOFT + "user={userParam}"
				+ "&password={passwordParam}" + "&sender={senderParam}"
				+ "&phone={phoneParam}" + "&message={messageParam}";

		phone = formatTelephone(phone);

		Sms sms = new Sms();

		sms.setCout(0);

		sms.setDateEnregistrement(LocalDateTime.now());

		sms.setDestinataire(phone);
		sms.setMessage(message);
		sms.setPage(getNombrePage(message));
		sms.setCout(sms.getPage());

		String agent = "skyapp-sms";

		sms.setAgent(agent);
		String senderDefault = "SkyApp";
		sms.setSender(senderDefault);

		sms.setEnvoyer(Boolean.FALSE);

		sms.setCout(0);

		try {

			Map<String, String> params = new HashMap<>();
			String USER_DEFAULT = "skyapp";
			params.put("userParam", USER_DEFAULT);
			String PWD_DEFAULT = "skyapp";
			params.put("passwordParam", PWD_DEFAULT);
			params.put("senderParam", senderDefault);
			params.put("phoneParam", sms.getDestinataire());
			params.put("messageParam", sms.getMessage());

			RestTemplate restTemplate = new RestTemplate();

			result = restTemplate.getForObject(uri, ReponseSendSms.class,
					params);

			if(result != null) {
				resultSms(sms, result);
			}

		} catch (Exception ignored) {

		}

		return sms.getCout();

	}

	private int saveSms(ConfigSms configSms, String message, String phone) {

		phone = formatTelephone(phone);

		Sms sms = new Sms();

		sms.setDateEnregistrement(LocalDateTime.now());

		sms.setDestinataire(phone);
		sms.setMessage(message);
		sms.setPage(getNombrePage(message));

		String agent = "serveursms";

		sms.setAgent(agent);

		sms.setSender(configSms.getSenderSms());

		sms.setEnvoyer(Boolean.FALSE);

		sms.setCout(0);

		sms = smsService.save(sms);

		return sms.getPage();

	}

	private void resendSms(Sms sms) {

		ConfigSms configSms = configSmsService.getOne();

		ReponseSendSms result;

		final String uri = SENDSMsSKYSOFT + "user={userParam}"
				+ "&password={passwordParam}" + "&sender={senderParam}"
				+ "&phone={phoneParam}" + "&message={messageParam}";

		try {

			Map<String, String> params = new HashMap<>();
			params.put("userParam", configSms.getUserSms());
			params.put("passwordParam", configSms.getPasswordSms());
			params.put("senderParam", sms.getSender());
			params.put("phoneParam", sms.getDestinataire());
			params.put("messageParam", sms.getMessage());

			RestTemplate restTemplate = new RestTemplate();

			result = restTemplate.getForObject(uri, ReponseSendSms.class,
					params);

			assert result != null;
			resultSms(sms, result);

		} catch (Exception ignored) {

		}

	}

	private void resultSms(Sms sms, ReponseSendSms result) {
		if (result.isSuccess()) {

			sms.setEnvoyer(Boolean.TRUE);
			sms.setCout(result.getCout().intValue());

			sms.setOperator(result.getOperator());

			sms.setDateEnvoi(LocalDateTime.now());

			sms.setSsidSms(result.getSsidSms());

			sms.setQuantiteFin(result.getSolde().intValue());

			sms.setQuantiteInitiale(result.getSolde().intValue()
					+ result.getCout().intValue());

			smsService.save(sms);

		}
	}

	private int getNombrePage(String message) {

		int nombreCaractereSms = 160;
		if (message.length() <= nombreCaractereSms) {

			return 1;
		} else {

			return 1 + message.length() / nombreCaractereSms;
		}
	}

	private String formatTelephone(String phone) {

		if (phone.startsWith(INTERNATIONAlPHONE)) {

			return phone.substring(INTERNATIONAlPHONE.length());

		} else {

			if (!phone.startsWith(INDICATIfPAYsCAMEROUN)) {

				phone = INDICATIfPAYsCAMEROUN + phone;
			}

		}

		return phone;

	}

	private ArrayList<String> getPhoneNumber(String text) {

		ArrayList<String> number = new ArrayList<>();

		int oldIndex = -1;

		String t;

		text = epuresimple(text);

		for (int i = 0; i < text.length(); i++) {

			if (isSeparator(text.charAt(i))) {

				t = text.substring(oldIndex + 1, i);

				number.addAll(decomposeNumber(t));

				oldIndex = i;

			}

		}

		if (oldIndex == -1) {

			number.addAll(decomposeNumber(text));

		} else {

			t = text.substring(oldIndex + 1);

			number.addAll(decomposeNumber(t));

		}

		return number;

	}

	private ArrayList<String> decomposeNumber(String text) {

		ArrayList<String> number_ = new ArrayList<>();

		text = epure(text);

		int oldIndex = 0;

		String t = "";

		if (text.startsWith(INTERNATIONAlPHONE)) {

			number_.add(text);

		} else {

			int quotien = text.length() / NOMBReCHIFFRePHONeVALIDE;

			for (int i = 1; i <= quotien; i++) {

				t = text.substring(oldIndex, (i * NOMBReCHIFFRePHONeVALIDE));

				number_.add(t);

				oldIndex = (i * NOMBReCHIFFRePHONeVALIDE);

			}
		}

		return number_;
	}

	private String epure(String text) {

		StringBuilder out = new StringBuilder();

		text = text.trim();

		for (int i = 0; i < text.length(); i++) {

			if (isNumerique(text.charAt(i))) {

				out.append(text.charAt(i));
			}
		}

		out = new StringBuilder(unFormatTelephone(out.toString()));

		return out.toString();
	}

	private String epuresimple(String text) {

		StringBuilder out = new StringBuilder();

		text = text.trim();

		for (int i = 0; i < text.length(); i++) {

			if (isNumerique(text.charAt(i)) || isSeparator(text.charAt(i))) {

				out.append(text.charAt(i));
			}
		}

		out = new StringBuilder(unFormatTelephone(out.toString()));

		return out.toString();
	}

	private static String unFormatTelephone(String phone) {

		if (phone.startsWith(INTERNATIONAlPHONE)) {

			return phone
					.substring((INTERNATIONAlPHONE + INDICATIfPAYsCAMEROUN)
							.length());

		} else {

			if (phone.startsWith(INDICATIfPAYsCAMEROUN)) {

				return phone.substring(INDICATIfPAYsCAMEROUN.length());

			} else {
				return phone;
			}

		}

	}

	private boolean isSeparator(char c) {

		char sep1 = '-';
		char sep3 = '/';
		char sep2 = ';';
		return c == sep1 || c == sep2 || c == sep3;

	}

	private boolean isNumerique(char c) {

		String charNumerique = "0123456789";
		return charNumerique.indexOf(c) >= 0;
	}

	@Scheduled(cron = "0 0/10 * * * *")
	public void relanceSMS() {

		List<Sms> listeSmsPending;

		listeSmsPending = smsService.getListeSmsPending();

		listeSmsPending.forEach(this::resendSms);

	}
}
