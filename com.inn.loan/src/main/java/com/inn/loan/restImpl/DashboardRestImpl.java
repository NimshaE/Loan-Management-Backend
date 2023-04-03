package com.inn.loan.restImpl;

import com.inn.loan.rest.DashboardRest;
import com.inn.loan.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DashboardRestImpl implements DashboardRest {

    @Autowired
    DashboardService dashboardService;

    @Override
    public ResponseEntity<Map<String, Object>> getDetails() {
        return dashboardService.getCount();
    }
}
