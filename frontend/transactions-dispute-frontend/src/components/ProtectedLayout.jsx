import { Navigate, Outlet } from "react-router-dom";

import { useAuth } from "/Users/londolanindou/Projects /rotondwa/transactions-dispute-portal/frontend/transactions-dispute-frontend/src/context/AuthContext";

import LoadingSpinner from "/Users/londolanindou/Projects /rotondwa/transactions-dispute-portal/frontend/transactions-dispute-frontend/src/components/LoadingSpinner";

export default function ProtectedLayout() {

  const { user, loading } = useAuth();

  if (loading) {
    return <LoadingSpinner />;
  }

  if (!user) {
    return <Navigate to="/" />;
  }

  return <Outlet />;
}