package market.known.agent.finance.Repository;

import market.known.agent.finance.Entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, String> {

    Credential findCredentialByCorpId(String corpId);

    void deleteCredentialByCorpId(String corpId);

}
