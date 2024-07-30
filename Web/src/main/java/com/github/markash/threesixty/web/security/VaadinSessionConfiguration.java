package com.github.markash.threesixty.web.security;

import com.vaadin.flow.server.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Configures Vaadin to work properly with sessions.<br />
 *
 * TODO Determine whether this class is necessary
 */
@Component
class VaadinSessionConfiguration implements VaadinServiceInitListener, SystemMessagesProvider, SessionDestroyListener, SessionInitListener {
    private static final Logger LOG = LoggerFactory.getLogger(VaadinSessionConfiguration.class);

    private final String relativeSessionExpiredUrl;

    VaadinSessionConfiguration(ServerProperties serverProperties) {
        relativeSessionExpiredUrl = UriComponentsBuilder.fromPath(serverProperties.getServlet().getContextPath()).path("session-expired").build().toUriString();
    }

    @Override
    public SystemMessages getSystemMessages(SystemMessagesInfo systemMessagesInfo) {
        var messages = new CustomizedSystemMessages();
        // Redirect to a specific screen when the session expires. In this particular case we don't want to logout
        // just yet. If you would like the user to be completely logged out when the session expires, this URL
        // should the logout URL.
        messages.setSessionExpiredURL(relativeSessionExpiredUrl);
        return messages;
    }

    @Override
    public void sessionDestroy(SessionDestroyEvent event) {
        // We also want to destroy the underlying HTTP session since it is the one that contains the authentication
        // token.
        try {
            event.getSession().getSession().invalidate();
        } catch (Exception ignore) {
            // Session was probably already invalidated.
        }
    }

    /**
     * Invoked when a new Vaadin service session is initialized for that
     * service.
     * <p>
     * Because of the way different service instances share the same session,
     * the listener is not necessarily notified immediately when the session is
     * created but only when the first request for that session is handled by a
     * specific service.
     *
     * @param event
     *            the initialization event
     * @throws ServiceException
     *             a problem occurs when processing the event
     */
    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException {
        LOG.info("Session Initialized {}", event);
    }


    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().setSystemMessagesProvider(this);
        event.getSource().addSessionDestroyListener(this);
    }
}