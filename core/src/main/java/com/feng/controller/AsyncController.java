package com.feng.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

@RestController
@RequestMapping("/async")
public class AsyncController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final TransferQueue<DeferredResult<ResponseEntity<?>>> TRANSFER_QUEUE = new LinkedTransferQueue<>();

    {
        Thread thread = new Thread(() ->
        {
            while (true) {
                try {
                    DeferredResult<ResponseEntity<?>> take = TRANSFER_QUEUE.take();
                    final String sql = "SELECT * FROM sys_users";
                    List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
                    take.setResult(ResponseEntity.ok(maps));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        System.out.println("start...");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final String sql = "SELECT * FROM sys_users";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
        return ResponseEntity.ok(maps);
    }

    @GetMapping("/allAsync")
    public Callable<ResponseEntity<?>> getAllAsync(){
        System.out.println("start...");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Callable<ResponseEntity<?>> callable = () -> {
            final String sql = "SELECT * FROM sys_users";
            List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
            return ResponseEntity.ok(maps);
        };

        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
        return callable;
    }

    @GetMapping("/allAsyncDeferred")
    public DeferredResult<ResponseEntity<?>> getAllByDeferred(){
        System.out.println("start...");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();
        try {
            TRANSFER_QUEUE.transfer(deferredResult);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
        return deferredResult;
    }

}
