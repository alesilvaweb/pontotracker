package com.timesheetsystem.repository;

import com.timesheetsystem.domain.UserPreference;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserPreference entity.
 */
@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
    default Optional<UserPreference> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<UserPreference> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<UserPreference> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select userPreference from UserPreference userPreference left join fetch userPreference.user",
        countQuery = "select count(userPreference) from UserPreference userPreference"
    )
    Page<UserPreference> findAllWithToOneRelationships(Pageable pageable);

    @Query("select userPreference from UserPreference userPreference left join fetch userPreference.user")
    List<UserPreference> findAllWithToOneRelationships();

    @Query("select userPreference from UserPreference userPreference left join fetch userPreference.user where userPreference.id =:id")
    Optional<UserPreference> findOneWithToOneRelationships(@Param("id") Long id);
}
