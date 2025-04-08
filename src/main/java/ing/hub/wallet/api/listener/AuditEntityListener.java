/* (C)2025 */
package ing.hub.wallet.api.listener;

import ing.hub.wallet.api.security.CustomTokenAuthentication;
import ing.hub.wallet.entity.BaseEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditEntityListener {

	private static final UUID SYSTEM_USER = UUID.randomUUID();

	@PrePersist
	public void setCreatedFields(final BaseEntity entity) {
		final var now = LocalDateTime.now();
		final var user = getLogedUserId();

		entity.setCreatedTime(now);
		entity.setCreatedBy(user);
		entity.setModifiedTime(now);
		entity.setModifiedBy(user);
	}

	@PreUpdate
	public void setModifiedFields(final BaseEntity entity) {
		entity.setModifiedTime(LocalDateTime.now());
		entity.setModifiedBy(getLogedUserId());
	}

	private UUID getLogedUserId() {
		final var auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.isAuthenticated() && auth instanceof CustomTokenAuthentication) {
			return ((CustomTokenAuthentication)
							SecurityContextHolder.getContext().getAuthentication())
					.getLoggedUser()
					.getCustomerId();
		} else {
			return SYSTEM_USER;
		}
	}
}
