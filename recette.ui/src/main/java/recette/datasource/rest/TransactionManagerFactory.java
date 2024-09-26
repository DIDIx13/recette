package recette.datasource.rest;

import core.datasource.TransactionManager;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author dominique huguenin (dominique.huguenin@rpn.ch)
 */
public final class TransactionManagerFactory {

    private TransactionManagerFactory() {
    }

    protected static String getRestServiceBaseUrl() throws NamingException {
        InitialContext initCtx = new InitialContext();
        Context envCtx = (Context) initCtx.lookup("java:comp/env");
        return (String) envCtx
                .lookup("restServiceBaseUrl");

    }

    public static TransactionManager getInstance() throws NamingException {

        return RestTransactionManagerImpl.getInstance(getRestServiceBaseUrl());
    }

}
