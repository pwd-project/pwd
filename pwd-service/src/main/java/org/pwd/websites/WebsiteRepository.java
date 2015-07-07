package org.pwd.websites;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author bartosz.walacik
 */
public interface WebsiteRepository extends JpaRepository<Website, Integer> {
}
