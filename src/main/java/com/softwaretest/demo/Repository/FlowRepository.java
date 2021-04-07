package com.softwaretest.demo.Repository;

import com.softwaretest.demo.Domain.Flow;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowRepository extends CrudRepository<Flow,Long> {
}
