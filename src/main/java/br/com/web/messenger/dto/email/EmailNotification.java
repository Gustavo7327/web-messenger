package br.com.web.messenger.dto.email;

import br.com.web.messenger.constants.EmailType;

public record EmailNotification(String toEmail, String userName, int code, EmailType emailType) {
}
