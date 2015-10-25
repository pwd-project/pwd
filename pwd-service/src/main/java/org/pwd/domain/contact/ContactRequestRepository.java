package org.pwd.domain.contact;

import org.springframework.data.jpa.repository.JpaRepository;
/**
 * @author Sławomir Mikulski
 */
public interface ContactRequestRepository extends JpaRepository<ContactRequest, Integer> {
}
