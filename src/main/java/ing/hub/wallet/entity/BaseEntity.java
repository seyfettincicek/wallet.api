/* (C)2025 */
package ing.hub.wallet.entity;

import ing.hub.wallet.api.listener.AuditEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter;

@EntityListeners(AuditEntityListener.class)
@MappedSuperclass
@Data
@EqualsAndHashCode
@ToString(doNotUseGetters = true)
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = -659885063782127370L;

	private static final int COLUMN_LENGTH_38 = 38;

	@Id
	@GeneratedValue
	@UuidGenerator
	@Column(name = "id", updatable = false, nullable = false, length = COLUMN_LENGTH_38, unique = true)
	private UUID id;

	@Column(name = "version")
	@Version
	private Long version = 1L;

	@CreatedBy
	@Column(name = "created_by", nullable = false, length = COLUMN_LENGTH_38)
	private UUID createdBy;

	@Column(name = "created_time", nullable = false, updatable = false)
	@Convert(converter = LocalDateTimeConverter.class)
	@CreatedDate
	private LocalDateTime createdTime;

	@LastModifiedBy
	@Column(name = "modified_by", nullable = false, updatable = false, length = COLUMN_LENGTH_38)
	private UUID modifiedBy;

	@Column(name = "modified_time", nullable = false)
	@Convert(converter = LocalDateTimeConverter.class)
	@LastModifiedDate
	private LocalDateTime modifiedTime;
}
