package com.example.demo.controller;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.service.DatabaseExportService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DatabaseExportService databaseExportService;

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportDatabase() {
        String sql = databaseExportService.exportDatabaseToSql();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"products.sql\"")
                .contentType(MediaType.parseMediaType("application/sql;charset=UTF-8"))
                .body(sql.getBytes(StandardCharsets.UTF_8));
    }
}
