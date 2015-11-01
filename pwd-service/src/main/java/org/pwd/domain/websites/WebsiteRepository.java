package org.pwd.domain.websites;

import org.springframework.data.jpa.repository.JpaRepository;

import java.net.URL;

/**
 * @author bartosz.walacik
 */
public interface WebsiteRepository extends JpaRepository<Website, Integer> {
    Website findOneByUrl(URL url);
}
