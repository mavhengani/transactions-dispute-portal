import {
  BrowserRouter,
  Routes,
  Route,
} from "react-router-dom";

import Login from "./pages/Login";
import Register from "./pages/Register";
import Home from "./pages/Home";
import About from "./pages/About";

import { AuthProvider } from "./context/AuthContext";

import ProtectedLayout from "./components/ProtectedLayout";
import ErrorBoundary from "./components/ErrorBoundary";
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
