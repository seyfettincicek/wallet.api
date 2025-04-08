/* (C)2025 */
package ing.hub.wallet.repository;

import ing.hub.wallet.entity.BaseEntity;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, UUID>, JpaSpecificationExecutor<T> {

	String FIND_BY_ID_QUERY = "SELECT t FROM #{#entityName} t WHERE t.id = ?1";

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query(FIND_BY_ID_QUERY)
	T findByIdForUpdate(UUID id);

	@Query(FIND_BY_ID_QUERY)
	Optional<T> findById(UUID id);
}
