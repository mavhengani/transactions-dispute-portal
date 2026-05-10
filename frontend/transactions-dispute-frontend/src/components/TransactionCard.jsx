export default function TransactionCard({
  tx,
}) {

  return (

    <div className="transaction-card">

      <div className="transaction-top">

        <div>

          <h3>
            {tx.merchantName}
          </h3>

          <p>
            {tx.transactionDate}
          </p>

        </div>

        <div className="amount">

          {tx.currency} {tx.amount}

        </div>

      </div>

      <div className="transaction-bottom">

        <span>
          {tx.maskedCardNumber}
        </span>

        <span
          className={
            tx.disputed
              ? "status disputed"
              : "status success"
          }
        >

          {
            tx.disputed
              ? "DISPUTED"
              : "SUCCESS"
          }

        </span>

      </div>

    </div>
  );
}