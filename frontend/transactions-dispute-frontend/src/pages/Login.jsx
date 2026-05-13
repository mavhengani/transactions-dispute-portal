import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../api/axios";
import "../pages/Login.css";
import { useAuth } from "../context/AuthContext";

export default function Login() {
  const navigate = useNavigate();
  const { login } = useAuth();

  const [form, setForm] = useState({ username: "", password: "" });
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      const { data } = await api.post("/auth/login", form);
      login(data);
      navigate("/home");
    } catch (err) {
      console.error(err);
      // Extract error message from response object or string
      let errorMessage = "Invalid username or password";

      if (err.response?.data) {
        const data = err.response.data;
        // Handle both object responses and string responses
        if (typeof data === 'string') {
          errorMessage = data;
        } else {
          // Try to extract message from error response object
          errorMessage = data.error || data.message || errorMessage;
        }
      } else if (err.message) {
        errorMessage = err.message;
      }

      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
      <div className="auth-page">
        <div className="auth-card">
          <h1>Welcome Back</h1>
          <p>Secure banking dispute portal</p>

          {error && <p className="auth-error">{error}</p>}

          <form onSubmit={handleSubmit}>
            <input
                type="text"
                name="username"
                placeholder="Username"
                value={form.username}
                onChange={handleChange}
                required
            />

            <div className="password-field">
              <input
                  type={showPassword ? "text" : "password"}
                  name="password"
                  placeholder="Password"
                  value={form.password}
                  onChange={handleChange}
                  required
              />
              <label className="show-password">
                <input
                    type="checkbox"
                    checked={showPassword}
                    onChange={() => setShowPassword((v) => !v)}
                />
                Show password
              </label>
            </div>

            <button type="submit" disabled={loading}>
              {loading ? "Logging in..." : "Login"}
            </button>
          </form>

          <div className="auth-link">
            Don't have an account?<Link to="/register"> Register</Link>
          </div>
        </div>
      </div>
  );
}
