package HAD.project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import HAD.project.backend.DAO.TokenToLinkDAO;
import HAD.project.backend.Model.TokenToLink;

@Service
public class TokenToLinkService {

    @Autowired
    private TokenToLinkDAO tokenToLinkDAO;

    public TokenToLink saveToken(TokenToLink token) {
        return tokenToLinkDAO.save(token);
    }

    public TokenToLink getTokenByPatientAbhaAddress(String patientAbhaAddress) {
        return tokenToLinkDAO.findByPatientAbhaAddress(patientAbhaAddress);
    }

    public List<TokenToLink> getAllTokens() {
        return tokenToLinkDAO.findAll();
    }

    public TokenToLink updateToken(TokenToLink token) {
        return tokenToLinkDAO.update(token);
    }

    public void deleteToken(String patientAbhaAddress) {
        tokenToLinkDAO.deleteById(patientAbhaAddress);
    }
}
