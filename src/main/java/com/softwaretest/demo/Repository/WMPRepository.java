package com.softwaretest.demo.Repository;

import com.softwaretest.demo.Domain.WMP;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WMPRepository extends CrudRepository<WMP,Long> {

}
