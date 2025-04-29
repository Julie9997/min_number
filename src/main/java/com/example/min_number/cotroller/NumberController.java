package com.example.min_number.cotroller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.PriorityQueue;

@RestController
@RequestMapping("/api/numbers")
@Tag(name = "Number Controller", description = "API для поиска N-ного минимального числа в XLSX файле")
public class NumberController {

    @PostMapping(value = "/find-nth-min", consumes = "multipart/form-data")
    @Operation(summary = "Найти N-ное минимальное число")
    public ResponseEntity<?> findNthMin(
            @Parameter(description = "XLSX файл с числами в первом столбце", required = true)
            @RequestParam("file") MultipartFile file,

            @Parameter(description = "Порядковый номер минимального числа (N > 0)", required = true)
            @RequestParam("n") int n) {

        try {
            // Валидация входных данных
            if (n <= 0) {
                return ResponseEntity.badRequest().body("N должно быть больше 0");
            }

            if (file.isEmpty() || !file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
                return ResponseEntity.badRequest().body("Неверный формат файла");
            }

            // обработка файла
            int result = processXlsxFile(file.getInputStream(), n);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    private int processXlsxFile(InputStream inputStream, int n) throws Exception {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b, a));

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell cell = row.getCell(0);
                if (cell == null || cell.getCellType() != CellType.NUMERIC) continue;

                int num = (int) cell.getNumericCellValue();
                processNumber(num, n, maxHeap);
            }
        }

        if (maxHeap.size() < n) {
            throw new IllegalArgumentException("В файле недостаточно чисел");
        }

        return maxHeap.peek();
    }

    private void processNumber(int num, int n, PriorityQueue<Integer> maxHeap) {
        if (maxHeap.size() < n) {
            maxHeap.offer(num);
        } else if (num < maxHeap.peek()) {
            maxHeap.poll();
            maxHeap.offer(num);
        }
    }
}