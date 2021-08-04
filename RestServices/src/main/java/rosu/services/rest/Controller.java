package rosu.services.rest;

import domain.Joc;
import domain.Runda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rosu.persistence.JocRepository;
import rosu.persistence.RundaRepository;

import java.util.List;
import java.util.stream.StreamSupport;

@CrossOrigin
@RestController
@RequestMapping("/rosu")
public class Controller {
    @Autowired
    private JocRepository repositoryJ;
    @Autowired
    private RundaRepository rundaRepository;

    @RequestMapping(value = "/carti/joc/{id}", method = RequestMethod.GET)
    public Joc[] findCarti(@PathVariable Integer id) {
        int size = (int) StreamSupport.stream(repositoryJ.findByJocID(id).spliterator(), false).count();
        System.out.println(size);
        Joc[] result = new Joc[size];
        result = ((List<Joc>) repositoryJ.findByJocID(id)).toArray(result);
        return result;
    }

    @RequestMapping(value = "/carti/jucator/{id}/joc/{idJoc}", method = RequestMethod.GET)
    public Runda[] findNotePart(@PathVariable String id, @PathVariable Integer idJoc) {
        int size = (int) StreamSupport.stream(rundaRepository.findByPlayerGame(id,idJoc).spliterator(), false).count();
        Runda[] result = new Runda[size];
        result = ((List<Runda>) rundaRepository.findByPlayerGame(id,idJoc)).toArray(result);
        return result;
    }



    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String Error(Exception e) {
        return e.getMessage();
    }
}
