import api from "/Users/londolanindou/Projects /rotondwa/transactions-dispute-portal/frontend/transactions-dispute-frontend/src/api/axios";

export default function TransactionModal({
  tx,
  close,
  refresh,
}) {

  const disputeTransaction =
    async () => {

      try {

        await api.put(
          `/transactions/${tx.id}/dispute`
        );

        alert(
          "Transaction disputed"
        );

        refresh();

        close();

      } catch (error) {

        console.error(error);

        alert(
          error.response?.data ||
          "Failed to dispute transaction"
        );
      }
    };

  return (

    <div
      className="modal-overlay"
      onClick={close}
    >

      <div
        className="modal"
        onClick={(e) =>
          e.stopPropagation()
        }
      >

        <h2>
          Transaction Details
        </h2>

        <div className="detail-row">
          <strong>Merchant:</strong>
          <span>{tx.merchantName}</span>
        </div>

        <div className="detail-row">
          <strong>Amount:</strong>
          <span>
            {tx.currency} {tx.amount}
          </span>
        </div>

        <div className="detail-row">
          <strong>Type:</strong>
          <span>
            {tx.transactionType}
          </span>
        </div>

        <div className="detail-row">
          <strong>Status:</strong>
          <span>{tx.status}</span>
        </div>

        <div className="detail-row">
          <strong>Reference:</strong>
          <span>
            {tx.referenceNumber}
          </span>
        </div>

        <div className="detail-row">
          <strong>Payment:</strong>
          <span>
            {tx.paymentMethod}
          </span>
        </div>

        <div className="detail-row">
          <strong>Location:</strong>
          <span>{tx.location}</span>
        </div>

        <div className="detail-row">
          <strong>Card:</strong>
          <span>
            {tx.maskedCardNumber}
          </span>
        </div>

        <div className="detail-row">
          <strong>Date:</strong>
          <span>
            {tx.transactionDate}
          </span>
        </div>

        <div className="detail-row">
          <strong>Disputed:</strong>
          <span>
            {
              tx.disputed
                ? "YES"
                : "NO"
            }
          </span>
        </div>

        {
          !tx.disputed && (

            <button
              className="dispute-btn"
              onClick={
                disputeTransaction
              }
            >
              Mark As Disputed
            </button>
          )
        }

        <button
          className="close-btn"
          onClick={close}
        >
          Close
        </button>

      </div>

    </div>
  );
}