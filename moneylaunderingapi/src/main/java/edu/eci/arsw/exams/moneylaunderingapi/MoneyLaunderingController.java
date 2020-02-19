package edu.eci.arsw.exams.moneylaunderingapi;


import edu.eci.arsw.exams.moneylaunderingapi.model.SpringException;
import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class MoneyLaunderingController {

    @Autowired
    @Qualifier("MoneyLaunderingService")
    MoneyLaunderingService moneyLaunderingService;

    @RequestMapping( value = "/fraud-bank-accounts", method = GET)
    public List<SuspectAccount> offendingAccounts() {
        return moneyLaunderingService.getSuspectAccounts();
    }

    @RequestMapping( value = "/fraud-bank-accounts", method = POST)
    @ResponseBody
    public void guardarOffendingAccounts(@RequestBody SuspectAccount suspectAccount) {
        moneyLaunderingService.agregarAccountStatus(suspectAccount);
    }

    @RequestMapping( value = "/fraud-bank-account/{accountid}", method = GET)
    public SuspectAccount getOnlySuspectAccount(@PathVariable String accountid){
        SuspectAccount suspectAccount = null;
        try {
            suspectAccount = moneyLaunderingService.getAccountStatus(accountid);
        } catch (SpringException e) {
            e.printStackTrace();
        }
        return suspectAccount;
    }

    @RequestMapping( value = "/fraud-bank-account/{accountId}", method = PUT)
    public void actualizarOnlySuspectAccount(@PathVariable String accountId){
        try {
            SuspectAccount temporal = moneyLaunderingService.getAccountStatus(accountId);
            moneyLaunderingService.updateAccountStatus(temporal);
        } catch (SpringException e) {
            e.printStackTrace();
        }


    }



    //TODO
}
