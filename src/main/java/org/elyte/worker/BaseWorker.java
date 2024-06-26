package org.elyte.worker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.elyte.booking.BookingHandler;
import org.elyte.config.AppConfig;
import org.elyte.enums.State;
import org.elyte.enums.WorkerType;
import org.elyte.queue.Queue;
import org.elyte.queue.QueueItem;
import org.elyte.search.SearchHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import com.rabbitmq.client.DeliverCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Setter
@Getter
@AllArgsConstructor
public class BaseWorker extends AppConfig implements Runnable {

    private WorkerType workerType;
    private String QUEUE_NAME;
    private String EXCHANGE_NAME;
    private String ROUTING_KEY_NAME;
    

    private static final Logger log = LoggerFactory.getLogger(BaseWorker.class);

    public BaseWorker() {

    }

    public BaseWorker(WorkerType workerType, String QUEUE_NAME, String ROUTING_KEY_NAME) {
        this.workerType = workerType;
        this.QUEUE_NAME = QUEUE_NAME;
        this.ROUTING_KEY_NAME = ROUTING_KEY_NAME;
        this.EXCHANGE_NAME = this.getConfigValue("EXCHANGE_NAME");

    }

    public CreateWorker createWorker() {
        return new CreateWorker(this.generateUuidString(), this.timeNow(),
                this.workerType,
                this.QUEUE_NAME);
    }

    private final Connection dbConnection() {
        
        try {
            Class.forName(this.getConfigValue("JDBC_DRIVER"));
        } catch (final ClassNotFoundException e) {
            log.error("[x] ClassNotFoundException " + e.getCause());
        }
        try {
            return DriverManager.getConnection(this.getConfigValue("SPRING_DATASOURCE_URL"));
        } catch (SQLException ex) {
            log.error("[x] SQLException: " + ex.getMessage());
            return null;
        } 
    }
    

    public void insertWorkerToDb(CreateWorker worker) {
        String sql = " insert into workers (worker_id, created, worker_type, queue_name)"
                + " values (?, ?, ?, ?)";
        Connection connect = dbConnection();
        try {
            assert connect != null;
            try (PreparedStatement preparedStmt = connect.prepareStatement(sql)) {
                preparedStmt.setString(1, worker.getWid());
                preparedStmt.setString(2, worker.getCreated());
                preparedStmt.setString(3, worker.getWorkerType().name());
                preparedStmt.setString(4, worker.getQueueName());
                preparedStmt.executeUpdate();
                log.info(worker.getWorkerType().name() + " WORKER CREATED");
            }
        } catch (Exception e) {
            log.error("[x] COULD NOT CREATE WORKER " + e.getCause());
        }finally {
            this.close(connect);
        }
    }

    public void updateONGoingTaskStatusInDb(String tid) throws Exception {
        String sql = "UPDATE tasks SET state=?,started_at=? WHERE task_id=?";
        Connection connect = dbConnection();
        try {
            assert connect != null;
            try (PreparedStatement preparedStmt = connect.prepareStatement(sql)) {
                preparedStmt.setString(1, State.PENDING.name());
                preparedStmt.setString(2, this.timeNow());
                preparedStmt.setString(3, tid);
                preparedStmt.executeUpdate();

            }
        } finally {
            this.close(connect);
        }

    }

    public void updateFinishedTaskInDb(WorkResult result) {
        String sql = "UPDATE tasks SET finished=?,state=?,ended_at=?,successful=?,result=? WHERE task_id=?";
        Connection connect = dbConnection();
        try {
            assert connect != null;
            try (PreparedStatement preparedStmt = connect.prepareStatement(sql)) {
                preparedStmt.setBoolean(1, true);
                preparedStmt.setString(2, State.FINISHED.name());
                preparedStmt.setString(3, this.timeNow());
                preparedStmt.setBoolean(4, result.isSuccess());
                preparedStmt.setString(5, this.convertObjectToJson(result.getResult()));
                preparedStmt.setString(6, result.getTid());
                preparedStmt.executeUpdate();

            }
        } catch (Exception e) {
            log.error("[x] UPDATING FINISHED TASK ERROR " + e.getLocalizedMessage());
        }finally {
            this.close(connect);
        }

    }

    @Override
    public void run() {
        Queue queue = new Queue();
        queue.createExchangeQueue(QUEUE_NAME, this.EXCHANGE_NAME, "direct", ROUTING_KEY_NAME);
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String queueItemStr = new String(delivery.getBody(), "UTF-8");
            QueueItem queueItem = this.mapper.readValue(queueItemStr, QueueItem.class);
            try {
                doWork(queueItem, queue);
            } finally {
                log.info(" [x] Done");
                queue.getChannel().basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }

        };
        this.insertWorkerToDb(createWorker());
        queue.listenToQueue(QUEUE_NAME, deliverCallback);

    }
   
    private void doWork(QueueItem queueItem, Queue queue) {
        WorkResult result = new WorkResult(queueItem.getTask().getTid(), false, null);
        try {
            updateONGoingTaskStatusInDb(result.getTid());

            result = switch (queueItem.getJob().getJobType()) {
                case BOOKING -> {
                    BookingHandler bookingHandler = new BookingHandler();
                    yield bookingHandler.createBooking(queueItem, dbConnection());
                }
                case SEARCH -> {
                    SearchHandler searchhandler = new SearchHandler();
                    yield searchhandler.search(queueItem, dbConnection());
                }
                default -> throw new Exception("[x] Unknown job type :" + queueItem.getJob().getJobType());
            };

        } catch (Exception e) {
            queue.createExchangeQueue(this.getConfigValue("LOST_QUEUE_NAME"), this.EXCHANGE_NAME, "direct",
                    this.getConfigValue("LOST_ROUTING_KEY"));
            String message = this.convertObjectToJson(queueItem);
            queue.sendMessage(this.EXCHANGE_NAME, this.getConfigValue("LOST_ROUTING_KEY"), message);
            log.error(" [x] Error, Item sent to dead letter queue :"+ e.getLocalizedMessage());

        } finally {
            updateFinishedTaskInDb(result);
        }

    }

}
