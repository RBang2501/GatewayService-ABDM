package HAD.project.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import HAD.project.backend.Model.TokenToLink;
import HAD.project.backend.Service.TokenToLinkService;

@RestController
@RequestMapping("/medicalRecordLink")
public class TokenToLinkController {

    @Autowired
    private TokenToLinkService tokenToLinkService;

    @PostMapping("/create-token")
    public TokenToLink saveToken(@RequestBody TokenToLink token) {
        return tokenToLinkService.saveToken(token);
    }

    @GetMapping("/get-token/{patientAbhaAddress}")
    public TokenToLink getTokenByPatientAbhaAddress(@PathVariable String patientAbhaAddress) {
        return tokenToLinkService.getTokenByPatientAbhaAddress(patientAbhaAddress);
    }

    @PutMapping("/update-token")
    public TokenToLink updateToken(@RequestBody TokenToLink token) {
        return tokenToLinkService.updateToken(token);
    }

    @DeleteMapping("/delete-token/{patientAbhaAddress}")
    public void deleteToken(@PathVariable String patientAbhaAddress) {
        tokenToLinkService.deleteToken(patientAbhaAddress);
    }
}
