import {
  BrowserRouter,
  Routes,
  Route,
} from "react-router-dom";

import Login from "/Users/londolanindou/Projects /rotondwa/transactions-dispute-portal/frontend/transactions-dispute-frontend/src/pages/Login";
import Register from "/Users/londolanindou/Projects /rotondwa/transactions-dispute-portal/frontend/transactions-dispute-frontend/src/pages/Register";
import Home from "/Users/londolanindou/Projects /rotondwa/transactions-dispute-portal/frontend/transactions-dispute-frontend/src/pages/Home";
import About from "/Users/londolanindou/Projects /rotondwa/transactions-dispute-portal/frontend/transactions-dispute-frontend/src/pages/About";

import { AuthProvider } from "/Users/londolanindou/Projects /rotondwa/transactions-dispute-portal/frontend/transactions-dispute-frontend/src/context/AuthContext";

import ProtectedLayout from "/Users/londolanindou/Projects /rotondwa/transactions-dispute-portal/frontend/transactions-dispute-frontend/src/components/ProtectedLayout";

import ErrorBoundary from "/Users/londolanindou/Projects /rotondwa/transactions-dispute-portal/frontend/transactions-dispute-frontend/src/components/ErrorBoundary";

export default function App() {

  return (

    <ErrorBoundary>

      <AuthProvider>

        <BrowserRouter>

          <Routes>

            <Route
              path="/"
              element={<Login />}
            />

            <Route
              path="/register"
              element={<Register />}
            />

            {/* PROTECTED ROUTES */}

            <Route element={<ProtectedLayout />}>

              <Route
                path="/home"
                element={<Home />}
              />

              <Route
                path="/about"
                element={<About />}
              />

            </Route>

          </Routes>

        </BrowserRouter>

      </AuthProvider>

    </ErrorBoundary>
  );
}