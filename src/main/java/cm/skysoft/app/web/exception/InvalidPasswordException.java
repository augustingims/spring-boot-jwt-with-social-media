package cm.skysoft.app.web.exception;

import cm.skysoft.app.utils.ErrorConstantsUtils;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class InvalidPasswordException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public InvalidPasswordException() {
        super(ErrorConstantsUtils.INVALID_PASSWORD_TYPE, "Mot de passe incorrect", Status.BAD_REQUEST);
    }
}
