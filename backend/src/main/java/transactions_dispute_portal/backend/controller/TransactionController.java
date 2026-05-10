package transactions_dispute_portal.backend.controller;

import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import transactions_dispute_portal.backend.dto.TransactionDTO;

import transactions_dispute_portal.backend.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = {"http://localhost:3000"})
public class TransactionController {

    private final TransactionService service;

    public TransactionController(
            TransactionService service
    ) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<TransactionDTO>>
    getTransactions(

            @RequestParam(required = false)
            Long userId,

            @RequestParam(required = false)
            Boolean disputed,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        Page<TransactionDTO> transactions =
                service.getTransactions(
                        userId,
                        disputed,
                        page,
                        size
                );

        return ResponseEntity.ok(transactions);
    }

    @PutMapping("/{id}/dispute")
    public ResponseEntity<TransactionDTO>
    disputeTransaction(
            @PathVariable Long id
    ) {

        TransactionDTO disputedTransaction =
                service.dispute(id);

        return ResponseEntity.ok(
                disputedTransaction
        );
    }
}