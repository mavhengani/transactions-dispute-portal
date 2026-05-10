package transactions_dispute_portal.backend.mapper;

import org.springframework.stereotype.Component;

import transactions_dispute_portal.backend.dto.TransactionDTO;
import transactions_dispute_portal.backend.entity.Transaction;

@Component
public class TransactionMapper {

    public TransactionDTO toDTO(
            Transaction transaction
    ) {

        String cardNumber =
                transaction.getUser().getCardNumber();

        String maskedCard =
                "**** **** **** "
                        + cardNumber.substring(
                        cardNumber.length() - 4
                );

        return new TransactionDTO(

                transaction.getId(),

                transaction.getMerchantName(),

                transaction.getAmount(),

                transaction.getCurrency(),

                transaction.getTransactionType(),

                transaction.getStatus(),

                transaction.getReferenceNumber(),

                transaction.getPaymentMethod(),

                transaction.getLocation(),

                transaction.getDisputed(),

                transaction.getDisputeReason(),

                transaction.getTransactionDate(),

                maskedCard
        );
    }
}