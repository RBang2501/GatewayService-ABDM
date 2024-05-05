package HAD.project.backend.Controller;

import HAD.project.backend.Model.Temp;
import HAD.project.backend.Service.TempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/temp")
public class TempController {

    @Autowired
    private TempService tempService;

    @PostMapping("/create")
    public ResponseEntity<Temp> addTemp(@RequestBody Temp temp) {
        Temp newTemp = tempService.save(temp);
        return new ResponseEntity<>(newTemp, HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Temp> getTempById(@PathVariable String id) {
        return tempService.findById(id)
                .map(temp -> new ResponseEntity<>(temp, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Temp>> getAllTemp() {
        return new ResponseEntity<>(tempService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTempById(@PathVariable String id) {
        tempService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<Void> deleteAllTemp() {
        tempService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
