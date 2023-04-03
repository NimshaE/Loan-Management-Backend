package com.inn.loan.service;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface DashboardService {

    ResponseEntity<Map<String, Object>> getCount();
}
