package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SpringException;
import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import org.springframework.stereotype.Service;
import org.springframework.boot.context.config.*;

import java.util.ArrayList;
import java.util.List;

@Service("MoneyLaunderingService")
public class MoneyLaunderingServiceStub implements MoneyLaunderingService {
    ArrayList<SuspectAccount> suspectAccounts = new ArrayList<SuspectAccount>();
    @Override
    public void updateAccountStatus(SuspectAccount suspectAccount) {
        try {
            SuspectAccount suspectAccountTemporal = getAccountStatus(suspectAccount.accountId);
            suspectAccountTemporal.actualizar();
        } catch (SpringException e) {
            e.printStackTrace();
        }
    }

    public void agregarAccountStatus(SuspectAccount suspectAccount){
        suspectAccounts.add(suspectAccount);
    }

    @Override
    public SuspectAccount getAccountStatus(String accountId) throws SpringException {
        SuspectAccount suspectAccount = null;

        for (int i = 0; i<suspectAccounts.size();i++){
            if (suspectAccounts.get(i).accountId.equals(accountId)){

                suspectAccount = suspectAccounts.get(i);
            }
        }
        if (suspectAccount == null){
            throw new SpringException(SpringException.RecursoNoEncontrado);
        }
        return suspectAccount;
    }

    @Override
    public List<SuspectAccount> getSuspectAccounts() {
        //TODO
        return suspectAccounts;

    }
}
