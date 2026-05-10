import { useEffect, useState, useCallback } from "react";

import { Link } from "react-router-dom";

import { useAuth } from "/Users/londolanindou/Projects /rotondwa/transactions-dispute-portal/frontend/transactions-dispute-frontend/src/context/AuthContext";

import api from "/Users/londolanindou/Projects /rotondwa/transactions-dispute-portal/frontend/transactions-dispute-frontend/src/api/axios";

import "/Users/londolanindou/Projects /rotondwa/transactions-dispute-portal/frontend/transactions-dispute-frontend/src/home.css";
import TransactionCard from "/Users/londolanindou/Projects /rotondwa/transactions-dispute-portal/frontend/transactions-dispute-frontend/src/components/TransactionCard";
import TransactionModal from "/Users/londolanindou/Projects /rotondwa/transactions-dispute-portal/frontend/transactions-dispute-frontend/src/components/TransactionModal";

const FILTERS = [
  { label: "All", value: "ALL" },
  { label: "Disputed", value: "DISPUTED" },
  { label: "Non-Disputed", value: "NON_DISPUTED" },
];

const PAGE_SIZE = 6;

export default function Home() {
  const { user, logout } = useAuth();

  const [transactions, setTransactions] = useState([]);
  const [selectedTransaction, setSelectedTransaction] = useState(null);
  const [filter, setFilter] = useState("ALL");
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchTransactions = useCallback(async () => {
    setLoading(true);
    setError(null);

    try {
      const params = new URLSearchParams({
        userId: user.id,
        page,
        size: PAGE_SIZE,
      });

      if (filter === "DISPUTED") params.set("disputed", "true");
      if (filter === "NON_DISPUTED") params.set("disputed", "false");

      const { data } = await api.get(`/transactions?${params}`);
      setTransactions(data.content);
      setTotalPages(data.totalPages);
    } catch (err) {
      console.error(err);
      setError("Failed to load transactions. Please try again.");
    } finally {
      setLoading(false);
    }
  }, [user.id, page, filter]);

  useEffect(() => {
    fetchTransactions();
  }, [fetchTransactions]);

  // Reset to page 0 whenever the filter changes
  const handleFilterChange = (value) => {
    setFilter(value);
    setPage(0);
  };

  return (
    <div className="home-page">
      {/* NAVBAR */}
      <nav className="navbar">
        <div className="logo">
          TD<span>Bank</span>
        </div>
        <div className="nav-links">
          <Link to="/home">Home</Link>
          <Link to="/about">About</Link>
          <button className="logout-btn" onClick={logout}>
            Logout
          </button>
        </div>
      </nav>

      {/* HERO */}
      <div className="hero-card">
        <h1>Welcome back, {user?.firstName || "User"}</h1>
        <p>Manage your banking disputes securely.</p>
        {user?.balance != null && (
          <div>
            <h3>Current Balance</h3>
            <h2>R {user.balance.toLocaleString()}</h2>
          </div>
        )}
      </div>

      {/* FILTERS */}
      <div className="filter-section">
        {FILTERS.map(({ label, value }) => (
          <button
            key={value}
            className={filter === value ? "active-filter" : ""}
            onClick={() => handleFilterChange(value)}
          >
            {label}
          </button>
        ))}
      </div>

      {/* TRANSACTIONS */}
      <div className="transactions-container">
        <h2>Recent Transactions</h2>
        {error && <p className="error-message">{error}</p>}
        {loading ? (
          <p>Loading transactions...</p>
        ) : (
          <div className="transactions-grid">
            {transactions.map((tx) => (
              <div key={tx.id} onClick={() => setSelectedTransaction(tx)}>
                <TransactionCard tx={tx} />
              </div>
            ))}
          </div>
        )}
      </div>

      {/* MODAL */}
      {selectedTransaction && (
        <TransactionModal
          tx={selectedTransaction}
          close={() => setSelectedTransaction(null)}
          refresh={fetchTransactions}
        />
      )}
    </div>
  );
}
