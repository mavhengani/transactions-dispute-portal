import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../api/axios";
import "../pages/Register.css";

export default function Register() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    username: "",
    password: "",
    confirmPassword: "",
    cellphone: "",
    gender: "",
    cardNumber: "",
  });

  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    if (form.password !== form.confirmPassword) {
      setError("Passwords do not match");
      return;
    }

    setLoading(true);

    try {
      const { confirmPassword, ...payload } = form;
      await api.post("/auth/register", payload);
      alert("Registration successful");
      navigate("/");
    } catch (err) {
      console.error(err);
      // Extract error message from response object or string
      let errorMessage = "Registration failed";

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
        <div className="auth-card register-card">
          <h1>Create Account</h1>

          {error && <p className="auth-error">{error}</p>}

          <form onSubmit={handleSubmit}>
            <input name="firstName" placeholder="First Name" onChange={handleChange} required />
            <input name="lastName" placeholder="Last Name" onChange={handleChange} required />
            <input name="username" placeholder="Username" onChange={handleChange} required />

            <div className="password-field">
              <input
                  type={showPassword ? "text" : "password"}
                  name="password"
                  placeholder="Password"
                  onChange={handleChange}
                  required
              />
              <input
                  type={showPassword ? "text" : "password"}
                  name="confirmPassword"
                  placeholder="Confirm Password"
                  onChange={handleChange}
                  required
              />
              <label className="show-password">
                <input
                    type="checkbox"
                    checked={showPassword}
                    onChange={() => setShowPassword((v) => !v)}
                />
                Show passwords
              </label>
            </div>

            <input name="cellphone" placeholder="Cellphone" onChange={handleChange} required />

            <select name="gender" onChange={handleChange} required>
              <option value="">Select Gender</option>
              <option value="Male">Male</option>
              <option value="Female">Female</option>
            </select>

            <input name="cardNumber" placeholder="Card Number" onChange={handleChange} required />

            <button type="submit" disabled={loading}>
              {loading ? "Creating..." : "Register"}
            </button>
          </form>

          <div className="auth-link">
            Already have an account?<Link to="/"> Login</Link>
          </div>
        </div>
      </div>
  );
}
