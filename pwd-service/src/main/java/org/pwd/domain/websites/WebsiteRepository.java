package org.pwd.domain.websites;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author bartosz.walacik
 */
public interface WebsiteRepository extends JpaRepository<Website, Integer> {

    /**
     * requires psql:
     * CREATE EXTENSION unaccent;
     */
    @Query(nativeQuery = true, value =
            "SELECT * from website " +
                    "where to_tsvector(unaccent( replace(url,'.', ' ') ||' '|| coalesce(city,'') ||' '|| coalesce(county,'') || ' ' || coalesce(voivodeship,'') || ' '|| coalesce(administrative_unit,''))) " +
                    "@@ to_tsquery(unaccent(:searchPhrase)) ")
    List<Website> search(@Param("searchPhrase") String searchPhrase);
}
