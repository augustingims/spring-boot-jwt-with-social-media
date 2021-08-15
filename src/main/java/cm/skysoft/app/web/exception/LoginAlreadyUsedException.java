package cm.skysoft.app.web.exception;

import cm.skysoft.app.utils.ErrorConstantsUtils;

public class LoginAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public LoginAlreadyUsedException() {
        super(ErrorConstantsUtils.LOGIN_ALREADY_USED_TYPE, "Nom de connexion déjà utilisé!", "userManagement", "userexists");
    }
}
