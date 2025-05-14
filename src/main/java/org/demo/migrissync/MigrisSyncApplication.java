package org.demo.migrissync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("org.demo.migrissync.dao.entity")
public class MigrisSyncApplication {

  public static void main(String[] args) {
    SpringApplication.run(MigrisSyncApplication.class, args);
  }
}
