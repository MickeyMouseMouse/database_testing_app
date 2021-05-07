import java.sql.Connection;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class Main {
    public static void start(int transactionsNumber, int isolationLevel) {
        CountDownLatch initLatch = new CountDownLatch(3);
        CountDownLatch beginLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(3);

        Connection dbSelect = Tools.connect(isolationLevel);
        Connection dbInsert = Tools.connect(isolationLevel);
        Connection dbUpdate = Tools.connect(isolationLevel);

        ArrayList<Owner> owners = new OwnersGenerator().generate(transactionsNumber);

        TransactionThread selectThread = new TransactionThread(dbSelect, transactionsNumber,
                TransactionsEnum.SELECT, null, initLatch, beginLatch, endLatch);
        TransactionThread insertThread = new TransactionThread(dbInsert, transactionsNumber,
                TransactionsEnum.INSERT, owners, initLatch, beginLatch, endLatch);
        TransactionThread updateThread = new TransactionThread(dbUpdate, transactionsNumber,
                TransactionsEnum.UPDATE, null, initLatch, beginLatch, endLatch);

        selectThread.start();
        insertThread.start();
        updateThread.start();

        try {
            initLatch.await();
            beginLatch.countDown();
            endLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<Long> resultSelect = selectThread.getTimeResult();
        ArrayList<Long> resultInsert = insertThread.getTimeResult();
        ArrayList<Long> resultUpdate = updateThread.getTimeResult();

        Tools.disconnect(dbSelect);
        Tools.disconnect(dbInsert);
        Tools.disconnect(dbUpdate);

        final String str;
        switch (isolationLevel) {
            case 2 -> str = "READ_COMMITTED_";
            case 4 -> str = "REPEATABLE_READ_";
            default -> str = "SERIALIZABLE_";
        }

        Tools.writeTimeResultsToFile(str + "SELECT.txt", resultSelect);
        Tools.writeTimeResultsToFile(str + "INSERT.txt", resultInsert);
        Tools.writeTimeResultsToFile(str + "UPDATE.txt", resultUpdate);
    }

    public static void main(String[] args) {
        // READ_COMMITTED = 2
        // REPEATABLE_READ = 4
        // SERIALIZABLE = 8
        start(1000, 8);
    }
}
