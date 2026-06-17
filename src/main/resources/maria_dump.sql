CREATE DATABASE IF NOT EXISTS blip_db;

USE blip_db;

CREATE TABLE IF NOT EXISTS cron_job (
    job_id INT AUTO_INCREMENT PRIMARY KEY,
    job_name VARCHAR(255) NOT NULL,
    file_location VARCHAR(1024) NOT NULL,
    created_at DATE NOT NULL,
    updated_at DATE NOT NULL,
    scheduled_time DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS job_run (
   job_run_id INT AUTO_INCREMENT PRIMARY KEY,
   job_id INT NOT NULL,
   scheduled_at DATETIME NOT NULL,
   completed_at DATETIME NULL,
   status ENUM('FAILED','SUCCESSFUL') NOT NULL,
   worker_id INT NULL,
   CONSTRAINT fk_job_run_job
       FOREIGN KEY (job_id)
           REFERENCES cron_job(job_id)
           ON DELETE CASCADE
);
