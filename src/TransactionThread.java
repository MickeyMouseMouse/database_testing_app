import java.sql.Connection;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class TransactionThread extends Thread {
    private final Connection db;
    private final int transactionsNumber;
    private final TransactionsEnum transactionsType;
    private final ArrayList<Owner> owners;
    private final CountDownLatch initLatch;
    private final CountDownLatch beginLatch;
    private final CountDownLatch endLatch;

    private ArrayList<Long> timeResult;

    public TransactionThread(Connection db, int transactionsNumber,
                             TransactionsEnum transactionsType,
                             ArrayList<Owner> owners, CountDownLatch initLatch,
                             CountDownLatch beginLatch, CountDownLatch endLatch) {
        this.db = db;
        this.transactionsNumber = transactionsNumber;
        this.transactionsType = transactionsType;
        this.owners = owners;
        this.initLatch = initLatch;
        this.beginLatch = beginLatch;
        this.endLatch = endLatch;
    }

    public ArrayList<Long> getTimeResult() {return timeResult; }

    public void run() {
        initLatch.countDown();
        try {
            beginLatch.await();
            switch (transactionsType) {
                case SELECT -> timeResult = Transactions.selectOwners(db, transactionsNumber);
                case INSERT -> timeResult = Transactions.insertOwners(db, transactionsNumber, owners);
                case UPDATE -> timeResult = Transactions.updateOwners(db, transactionsNumber);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            endLatch.countDown();
        }
    }
}
