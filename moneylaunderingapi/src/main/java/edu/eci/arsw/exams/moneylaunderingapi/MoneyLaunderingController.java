package edu.eci.arsw.exams.moneylaunderingapi;


import edu.eci.arsw.exams.moneylaunderingapi.model.SpringException;
import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class MoneyLaunderingController {

    @Autowired
    @Qualifier("MoneyLaunderingService")
    MoneyLaunderingService moneyLaunderingService;


    @RequestMapping( value = "/fraud-bank-accounts", method = GET)
    public ResponseEntity<?> offendingAccounts() {
        try{
            List<SuspectAccount> archivos = moneyLaunderingService.getSuspectAccounts();
            return new ResponseEntity<>(archivos, HttpStatus.OK);
        } catch (Exception e){
            Logger.getLogger(MoneyLaunderingController.class.getName()).log(Level.SEVERE,null,e);
            return new ResponseEntity<>("El recurso no se ha encontrado",HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping( value = "/fraud-bank-accounts", method = POST)
    @ResponseBody
    public ResponseEntity<?> guardarOffendingAccounts(@RequestBody SuspectAccount suspectAccount) {
        try{
            moneyLaunderingService.agregarAccountStatus(suspectAccount);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e){
            Logger.getLogger(MoneyLaunderingController.class.getName()).log(Level.SEVERE,null,e);
            return new ResponseEntity<>("El recurso no se ha podido crear",HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping( value = "/fraud-bank-accounts/{accountid}", method = GET)
    public ResponseEntity<?> getOnlySuspectAccount(@PathVariable String accountid){
        try {
            SuspectAccount suspectAccount = moneyLaunderingService.getAccountStatus(accountid) ;
            return new ResponseEntity<>(suspectAccount,HttpStatus.OK);
        } catch (SpringException e) {
            Logger.getLogger(MoneyLaunderingController.class.getName()).log(Level.SEVERE,null,e);
            return new ResponseEntity<>("El recurso no se ha encontrado",HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping( value = "/fraud-bank-accounts/{accountId}", method = PUT)
    public ResponseEntity<?> actualizarOnlySuspectAccount(@PathVariable String accountId){
        try {
            SuspectAccount temporal = moneyLaunderingService.getAccountStatus(accountId);
            moneyLaunderingService.updateAccountStatus(temporal);
            return new ResponseEntity<>(temporal,HttpStatus.ACCEPTED);
        } catch (SpringException e) {
            Logger.getLogger(MoneyLaunderingController.class.getName()).log(Level.SEVERE,null,e);
            return new ResponseEntity<>("El recurso no se ha podido actualizar",HttpStatus.NOT_FOUND);
        }


    }



    //TODO
}
