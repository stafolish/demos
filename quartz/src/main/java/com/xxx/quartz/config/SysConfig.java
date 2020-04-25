package com.xxx.quartz.config;

import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Configuration
@EnableScheduling
public class SysConfig {
    @Value("${spring.datasource.password}")
    private String password;

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void init() throws Exception {
        System.out.println(password);
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        // ResultSet resultSet = statement.executeQuery("desc  LG_LiveConsume");
        for (String sql : sqls) {
            statement.execute(sql);
        }
        // while (resultSet.next()) {
        //     System.out.println(resultSet.getString(1));
        // }
    }

    private static final String[] sqls = {
            "DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS",
            "DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS",
            "DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE",
            "DROP TABLE IF EXISTS QRTZ_LOCKS",
            "DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS",
            "DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS",
            "DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS",
            "DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS",
            "DROP TABLE IF EXISTS QRTZ_TRIGGERS",
            "DROP TABLE IF EXISTS QRTZ_JOB_DETAILS",
            "DROP TABLE IF EXISTS QRTZ_CALENDARS",
            "CREATE TABLE QRTZ_JOB_DETAILS" +
                    "  (" +
                    "    SCHED_NAME VARCHAR(120) NOT NULL," +
                    "    JOB_NAME  VARCHAR(200) NOT NULL," +
                    "    JOB_GROUP VARCHAR(200) NOT NULL," +
                    "    DESCRIPTION VARCHAR(250) NULL," +
                    "    JOB_CLASS_NAME   VARCHAR(250) NOT NULL," +
                    "    IS_DURABLE VARCHAR(1) NOT NULL," +
                    "    IS_NONCONCURRENT VARCHAR(1) NOT NULL," +
                    "    IS_UPDATE_DATA VARCHAR(1) NOT NULL," +
                    "    REQUESTS_RECOVERY VARCHAR(1) NOT NULL," +
                    "    JOB_DATA BLOB NULL," +
                    "    PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4",
            "CREATE TABLE QRTZ_TRIGGERS" +
                    "  (" +
                    "    SCHED_NAME VARCHAR(120) NOT NULL," +
                    "    TRIGGER_NAME VARCHAR(200) NOT NULL," +
                    "    TRIGGER_GROUP VARCHAR(200) NOT NULL," +
                    "    JOB_NAME  VARCHAR(200) NOT NULL," +
                    "    JOB_GROUP VARCHAR(200) NOT NULL," +
                    "    DESCRIPTION VARCHAR(250) NULL," +
                    "    NEXT_FIRE_TIME BIGINT(13) NULL," +
                    "    PREV_FIRE_TIME BIGINT(13) NULL," +
                    "    PRIORITY INTEGER NULL," +
                    "    TRIGGER_STATE VARCHAR(16) NOT NULL," +
                    "    TRIGGER_TYPE VARCHAR(8) NOT NULL," +
                    "    START_TIME BIGINT(13) NOT NULL," +
                    "    END_TIME BIGINT(13) NULL," +
                    "    CALENDAR_NAME VARCHAR(200) NULL," +
                    "    MISFIRE_INSTR SMALLINT(2) NULL," +
                    "    JOB_DATA BLOB NULL," +
                    "    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)," +
                    "    FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)" +
                    "        REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4",
            "CREATE TABLE QRTZ_SIMPLE_TRIGGERS" +
                    "  (" +
                    "    SCHED_NAME VARCHAR(120) NOT NULL," +
                    "    TRIGGER_NAME VARCHAR(200) NOT NULL," +
                    "    TRIGGER_GROUP VARCHAR(200) NOT NULL," +
                    "    REPEAT_COUNT BIGINT(7) NOT NULL," +
                    "    REPEAT_INTERVAL BIGINT(12) NOT NULL," +
                    "    TIMES_TRIGGERED BIGINT(10) NOT NULL," +
                    "    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)," +
                    "    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)" +
                    "        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4",
            "CREATE TABLE QRTZ_CRON_TRIGGERS" +
                    "  (" +
                    "    SCHED_NAME VARCHAR(120) NOT NULL," +
                    "    TRIGGER_NAME VARCHAR(200) NOT NULL," +
                    "    TRIGGER_GROUP VARCHAR(200) NOT NULL," +
                    "    CRON_EXPRESSION VARCHAR(200) NOT NULL," +
                    "    TIME_ZONE_ID VARCHAR(80)," +
                    "    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)," +
                    "    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)" +
                    "        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4",
            "CREATE TABLE QRTZ_SIMPROP_TRIGGERS" +
                    "  (" +
                    "    SCHED_NAME VARCHAR(120) NOT NULL," +
                    "    TRIGGER_NAME VARCHAR(200) NOT NULL," +
                    "    TRIGGER_GROUP VARCHAR(200) NOT NULL," +
                    "    STR_PROP_1 VARCHAR(512) NULL," +
                    "    STR_PROP_2 VARCHAR(512) NULL," +
                    "    STR_PROP_3 VARCHAR(512) NULL," +
                    "    INT_PROP_1 INT NULL," +
                    "    INT_PROP_2 INT NULL," +
                    "    LONG_PROP_1 BIGINT NULL," +
                    "    LONG_PROP_2 BIGINT NULL," +
                    "    DEC_PROP_1 NUMERIC(13,4) NULL," +
                    "    DEC_PROP_2 NUMERIC(13,4) NULL," +
                    "    BOOL_PROP_1 VARCHAR(1) NULL," +
                    "    BOOL_PROP_2 VARCHAR(1) NULL," +
                    "    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)," +
                    "    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)" +
                    "    REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4",
            "CREATE TABLE QRTZ_BLOB_TRIGGERS" +
                    "  (" +
                    "    SCHED_NAME VARCHAR(120) NOT NULL," +
                    "    TRIGGER_NAME VARCHAR(200) NOT NULL," +
                    "    TRIGGER_GROUP VARCHAR(200) NOT NULL," +
                    "    BLOB_DATA BLOB NULL," +
                    "    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)," +
                    "    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)" +
                    "        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4",
            "CREATE TABLE QRTZ_CALENDARS" +
                    "  (" +
                    "    SCHED_NAME VARCHAR(120) NOT NULL," +
                    "    CALENDAR_NAME  VARCHAR(200) NOT NULL," +
                    "    CALENDAR BLOB NOT NULL," +
                    "    PRIMARY KEY (SCHED_NAME,CALENDAR_NAME)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4",
            "CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS" +
                    "  (" +
                    "    SCHED_NAME VARCHAR(120) NOT NULL," +
                    "    TRIGGER_GROUP  VARCHAR(200) NOT NULL," +
                    "    PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4",
            "CREATE TABLE QRTZ_FIRED_TRIGGERS" +
                    "  (" +
                    "    SCHED_NAME VARCHAR(120) NOT NULL," +
                    "    ENTRY_ID VARCHAR(95) NOT NULL," +
                    "    TRIGGER_NAME VARCHAR(200) NOT NULL," +
                    "    TRIGGER_GROUP VARCHAR(200) NOT NULL," +
                    "    INSTANCE_NAME VARCHAR(200) NOT NULL," +
                    "    FIRED_TIME BIGINT(13) NOT NULL," +
                    "    SCHED_TIME BIGINT(13) NOT NULL," +
                    "    PRIORITY INTEGER NOT NULL," +
                    "    STATE VARCHAR(16) NOT NULL," +
                    "    JOB_NAME VARCHAR(200) NULL," +
                    "    JOB_GROUP VARCHAR(200) NULL," +
                    "    IS_NONCONCURRENT VARCHAR(1) NULL," +
                    "    REQUESTS_RECOVERY VARCHAR(1) NULL," +
                    "    PRIMARY KEY (SCHED_NAME,ENTRY_ID)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4",
            "CREATE TABLE QRTZ_SCHEDULER_STATE" +
                    "  (" +
                    "    SCHED_NAME VARCHAR(120) NOT NULL," +
                    "    INSTANCE_NAME VARCHAR(200) NOT NULL," +
                    "    LAST_CHECKIN_TIME BIGINT(13) NOT NULL," +
                    "    CHECKIN_INTERVAL BIGINT(13) NOT NULL," +
                    "    PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4",
            "CREATE TABLE QRTZ_LOCKS" +
                    "  (" +
                    "    SCHED_NAME VARCHAR(120) NOT NULL," +
                    "    LOCK_NAME  VARCHAR(40) NOT NULL," +
                    "    PRIMARY KEY (SCHED_NAME,LOCK_NAME)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4"
    };

    @Scheduled(cron = "0/1 * * * * ?")
    public void deal() {
        System.out.println("deal-----");
    }

    @Autowired
    private JobFactory jobFactory;

    @Autowired
    private AutowireCapableBeanFactory capableBeanFactory;

    /**
     * 当触发器触发时，与之关联的任务被Scheduler中配置的JobFactory实例化，也就是每触发一次，就会创建一个任务的实例化对象
     * (如果缺省)则调用Job类的newInstance方法生成一个实例
     * (这里选择自定义)并将创建的Job实例化交给IoC管理
     * @return
     */
    @Bean
    public JobFactory jobFactory() {
        return new AdaptableJobFactory() {
            @Override
            protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
                Object jobInstance = super.createJobInstance(bundle);
                capableBeanFactory.autowireBean(jobInstance);
                return jobInstance;
            }
        };
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(jobFactory);
        //延迟启动
        schedulerFactoryBean.setStartupDelay(1);
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setConfigLocation(new ClassPathResource("/quartz.properties"));
        return schedulerFactoryBean;
    }

    @Bean
    public Scheduler scheduler() {
        return schedulerFactoryBean().getScheduler();
    }
}
