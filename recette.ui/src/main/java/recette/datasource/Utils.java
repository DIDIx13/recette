package recette.datasource;

import core.datasource.TransactionManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 *
 * @author dominique huguenin <dominique.huguenin at rpn.ch>
 */
public final class Utils {

    private static final Logger LOG = Logger.getLogger(Utils.class.getName());

    private Utils() {
    }

    public static TransactionManager getTransactionManager(
            final ServletContext context)
            throws ServletException {
        try {
            String factoryClassName = context
                    .getInitParameter("transactionManagerFactoryClass");

            Class clazz = Class.forName(factoryClassName);
            Method method = clazz.getMethod("getInstance");
            TransactionManager tm
                    = (TransactionManager) method.invoke(null, (Object[]) null);

            return tm;
        } catch (ClassNotFoundException
                | NoSuchMethodException
                | SecurityException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new ServletException(ex);
        }
    }

}
